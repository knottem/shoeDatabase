package se.na.shoedatabase.main;

import se.na.shoedatabase.interfaces.OrderSearchInterface;
import se.na.shoedatabase.dao.Encrypt;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.interfaces.OrdersCustomerInterface;
import se.na.shoedatabase.model.Admin;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.*;
import java.util.stream.Collectors;

import static se.na.shoedatabase.main.InitShoeDatabase.properties;

public class Reports {

    final Repository rep = Repository.getRepository();
    final PrintHelp printHelp = PrintHelp.getPrintHelp();
    final InputView inputView = InputView.getInputView();

    ArrayList<Shoe> shoes;
    ArrayList<Customer> customers;
    ArrayList<Orders> orders;

    final OrderSearchInterface brandSearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> s.getBrand().equalsIgnoreCase(b));
    final OrderSearchInterface colorSearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> s.getColor().equalsIgnoreCase(b));
    final OrderSearchInterface categorySearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> s.getCategoriesNames().contains(b));
    final OrderSearchInterface sizeSearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> Integer.toString(s.getSize()).equalsIgnoreCase(b));

    final OrdersCustomerInterface totalSpendingSearch = (o, c) -> o.stream().filter(f -> f.getCustomer().getId() == c.getId()).flatMap(f -> f.getShoes().stream()).mapToDouble(s -> s.getPrice() * s.getQuantity()).sum();
    final OrdersCustomerInterface totalOrders = (o, c) -> o.stream().filter(f -> f.getCustomer().getId() == c.getId()).count();

    public void login(){
        Admin admin;
        if(properties.getTesting()) {
            admin = rep.getAdmin(properties.getTestLoginAdmin(), properties.getTestLoginAdminPass());
        } else {
            admin = rep.getAdmin(
                    inputView.inputString("Anv??ndarnamn?", true),
                    Encrypt.encryptSHA3(inputView.inputString("L??senord?", true)));
        }
        if(admin != null) {
            customers = rep.getAllCustomers();
            shoes = rep.getAllShoes();
            orders = rep.getAllOrders();
            boolean repeat = true;
            while (repeat) {
                switch (inputView.inputInt("""
                        Vilken Rapport vill du titta p???
                        1. Visa alla kunder som har k??pt av visst m??rke, f??rg eller storlek.
                        2. Visa alla kunder och sammanlagda ordrar varje kund lagt.
                        3. Visa hur mycket varje kund har best??llt f??r
                        4. Visa best??llningsv??rde per ort
                        5. Visa Topplista ??ver mest s??lda skor
                        6. Visa Alla ordrar
                        7. Logga ut""", true)) {
                    case 1 -> listAllBought();
                    case 2 -> {
                        if(inputView.inputInt("Sorterad lista eller inte, svara 1 f??r sorterad", true) == 1){
                            listCustomerOrdersSorted();
                        } else {
                            listCustomerOrders();
                        }
                    }
                    case 3 -> {
                        if (inputView.inputInt("Sorterad lista eller inte, svara 1 f??r sorterad", true) == 1) {
                            listCustomersTotalSpendingSorted();
                        } else {
                            listCustomersTotalSpending();
                        }
                    }
                    case 4 -> listSpendingPerCity();
                    case 5 -> listTopSellingShoes(
                            inputView.inputInt("Hur l??ng topplista vill du se?", true),
                            inputView.inputInt("Vill du se topplista p?? skor som s??lt f??r mest pengar totalt (1) eller  antal skor s??lda (2)  (svara 1, 2)", true));
                    case 6 -> printHelp.printAllOrders(orders);
                    case 7 -> repeat = false;
                    default -> System.out.println("Felaktigt siffra");
                }
            }
        } else {
            System.out.println("Felaktig inloggningsinformation");
        }
    }

    private void listAllBought(){
        switch (inputView.inputInt("""
                Vad vill du s??ka p???
                1. M??rke
                2. F??rg
                3. Storlek
                4. Kategori""", true)){
            case 1 -> customerSearch(inputView.inputString("Skriv ett m??rke:", true), brandSearch);
            case 2 -> customerSearch(inputView.inputString("Skriv en F??rg:", true), colorSearch);
            case 3 -> customerSearch(inputView.inputString("Skriv en storlek: ", true), sizeSearch);
            case 4 -> customerSearch(inputView.inputString("Skriv en kategori", true), categorySearch);
            default -> System.out.println("Felaktigt siffra");
        }
    }

    private void customerSearch(String answer, OrderSearchInterface osi){
        List<Customer> customerList = orders.stream().filter(s -> osi.search(s, answer)).map(Orders::getCustomer).distinct().toList();
        if(customerList.size() > 0){
            printHelp.printCustomers(customerList);
        } else {
            System.out.println("Inga personer hittades.\n");
        }
    }

    private void listCustomerOrders(){
        customers.forEach(c ->
            System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                    c.getAddress().toString() + "\nAntal ordrar: " + (int) totalOrders.total(orders, c) + "\n"));
    }

    private void listCustomerOrdersSorted() {
        customers.stream().sorted((c1, c2) -> Double.compare(totalOrders.total(orders, c2), totalOrders.total(orders, c1)))
                .forEach(c -> System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                            c.getAddress().toString() + "\nAntal ordrar: " + (int) totalOrders.total(orders, c) + "\n"));
    }

    private void listCustomersTotalSpending(){
        customers.forEach(c ->
            System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                    c.getAddress().toString() + "\nTotala Summa: " + totalSpendingSearch.total(orders, c) + " kr.\n"));
    }

    private void listCustomersTotalSpendingSorted() {
        customers.stream().sorted((c1, c2) -> Double.compare(totalSpendingSearch.total(orders, c2), totalSpendingSearch.total(orders, c1)))
                .forEach(c -> System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                        c.getAddress().toString() + "\nTotala Summa: " + totalSpendingSearch.total(orders, c) + " kr.\n"));
    }

    private void listSpendingPerCity(){
        Map<String, Integer> spendingPerCity = new HashMap<>();
        orders.forEach(o -> {
            String city = o.getCustomer().getAddress().getCity();
            int spending = o.getShoes().stream().mapToInt(shoe -> shoe.getPrice() * shoe.getQuantity()).sum();
            spendingPerCity.merge(city, spending, Integer::sum);
        });
        printHelp.printCities(spendingPerCity.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
    }

    private void listTopSellingShoes(int antal, int totalsumma){
        List<Shoe> shoesList = new ArrayList<>();
        orders.forEach(o ->
                o.getShoes().forEach(s -> {
                    if(shoesList.stream().noneMatch(n -> n.getId() == s.getId())) {
                        shoesList.add(new Shoe(s));
                    }
                    shoesList.stream().filter(f -> f.getId() == s.getId()).forEach(f -> f.setQuantity(f.getQuantity() + 1));
                })
        );
        if(totalsumma == 1){
            shoesList.sort((s1, s2) -> s2.getQuantity()*s2.getPrice() - s1.getQuantity()*s1.getPrice());
        } else {
            shoesList.sort((s1, s2) -> s2.getQuantity() - s1.getQuantity());
        }
        System.out.println("\nTop " + antal + " mest s??lda skor:\n");
        printHelp.printShoesSold(shoesList, antal);
    }
}
