package se.na.shoedatabase.main;

import se.na.shoedatabase.interfaces.OrderSearchInterface;
import se.na.shoedatabase.dao.Encrypt;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Admin;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.*;

public class Rapports {

    Repository rep = Repository.getRepository();
    PrintHelp printHelp = PrintHelp.getPrintHelp();
    InputView inputView = InputView.getInputView();

    ArrayList<Shoe> shoes;
    ArrayList<Customer> customers;
    ArrayList<Orders> orders;

    OrderSearchInterface brandSearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> s.getBrand().equalsIgnoreCase(b));
    OrderSearchInterface colorSearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> s.getColor().equalsIgnoreCase(b));
    OrderSearchInterface categorySearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> s.getCategoriesNames().contains(b));
    OrderSearchInterface sizeSearch = (a, b) ->  a.getShoes().stream().anyMatch(s -> Integer.toString(s.getSize()).equalsIgnoreCase(b));

    public void checkRapports(){
        Admin admin = rep.getAdmin(
                inputView.inputString("Användarnamn?", true),
                Encrypt.encryptSHA3(inputView.inputString("Lösenord?", true)));
        if(admin != null) {
            shoes = rep.getAllShoes();
            customers = rep.getAllCustomers();
            orders = rep.getAllOrders(shoes, customers);
            boolean repeat = true;
            while (repeat) {
                switch (inputView.inputInt("""
                        Vilken Rapport vill du titta på?
                        1. Visa alla kunder som har köpt av visst märke, färg eller storlek.
                        2. Visa alla kunder och sammanlagda ordrar varje kund lagt.
                        3. Visa hur mycket varje kund har beställt för
                        4. Visa beställningsvärde per ort
                        5. Visa Topplista över mest sålda skor
                        6. Visa Alla ordrar
                        7. Logga ut""", true)) {
                    case 1 -> listAllBought();
                    case 2 -> {
                        int svar = inputView.inputInt("Sorterad lista eller inte, svara 1 för sorterad", true);
                        if(svar == 1){
                            listCustomerOrdersSorted();
                        } else {
                            listCustomerOrders();
                        }
                    }
                    case 3 -> {
                        int svar = inputView.inputInt("Sorterad lista eller inte, svara 1 för sorterad", true);
                        if (svar == 1) {
                            listCustomersTotalSpendingSorted();
                        } else {
                            listCustomersTotalSpending();
                        }
                    }
                    case 4 -> listSpendingPerCity();
                    case 5 -> listTopSellingShoes(
                            inputView.inputInt("Hur lång topplista vill du se?", true),
                            inputView.inputInt("Vill du se topplista på skor som sålt för mest pengar totalt (1) eller  antal skor sålda (2)  (svara 1, 2)", true));
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
                Vad vill du söka på?
                1. Märke
                2. Färg
                3. Storlek
                4. Kategori""", true)){
            case 1 -> customerSearch(inputView.inputString("Skriv ett märke:", true), brandSearch);
            case 2 -> customerSearch(inputView.inputString("Skriv en Färg:", true), colorSearch);
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
                    c.getAddress().toString() + "\nAntal ordrar: " +
                    orders.stream().filter(f -> f.getCustomer().getId() == c.getId()).count() + "\n"));
    }

    private void listCustomerOrdersSorted() {
        customers.stream().sorted((c1, c2) -> {
                    long totalOrders1 = orders.stream().filter(f -> f.getCustomer().getId() == c1.getId()).count();
                    long totalOrders2 = orders.stream().filter(f -> f.getCustomer().getId() == c2.getId()).count();
                    return Long.compare(totalOrders2, totalOrders1);
                }).forEach(c ->
                    System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                            c.getAddress().toString() + "\nAntal ordrar: " +
                            orders.stream().filter(f -> f.getCustomer().getId() == c.getId()).count() + "\n"));
    }

    private void listCustomersTotalSpending(){
        customers.forEach(c ->
            System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                    c.getAddress().toString() + "\nTotala Summa: " + orders.stream().filter(f -> f.getCustomer().getId() == c.getId())
                    .flatMap(o -> o.getShoes().stream()).mapToDouble(s -> s.getPrice() * s.getQuantity()).sum() + " kr.\n"));
    }

    private void listCustomersTotalSpendingSorted() {
        customers.stream().sorted((c1, c2) -> {
                    double totalSpending1 = orders.stream().filter(f -> f.getCustomer().getId() == c1.getId())
                            .flatMap(o -> o.getShoes().stream()).mapToDouble(s -> s.getPrice() * s.getQuantity()).sum();
                    double totalSpending2 = orders.stream().filter(f -> f.getCustomer().getId() == c2.getId())
                            .flatMap(o -> o.getShoes().stream()).mapToDouble(s -> s.getPrice() * s.getQuantity()).sum();
                    return Double.compare(totalSpending2, totalSpending1);
                }).forEach(c ->
                System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                        c.getAddress().toString() + "\nTotala Summa: " + orders.stream().filter(f -> f.getCustomer().getId() == c.getId())
                        .flatMap(o -> o.getShoes().stream()).mapToDouble(s -> s.getPrice() * s.getQuantity()).sum() + " kr.\n"));
    }

    private void listSpendingPerCity(){
        Map<String, Integer> spendingPerCity = new HashMap<>();
        orders.forEach(o -> {
            String city = o.getCustomer().getAddress().getCity();
            int spending = o.getShoes().stream().mapToInt(shoe -> shoe.getPrice() * shoe.getQuantity()).sum();
            spendingPerCity.merge(city, spending, Integer::sum);
        });
        List<Map.Entry<String, Integer>> sortedSpendingPerCity = spendingPerCity.entrySet().stream().sorted((s1, s2) -> s2.getValue() - s1.getValue()).toList();
        printHelp.printCities(sortedSpendingPerCity);
    }

    private void listTopSellingShoes(int antal, int totalsumma){
        ArrayList<Shoe> shoesList = new ArrayList<>();
        orders.forEach(o ->
            o.getShoes().forEach(s -> {
                if(shoesList.stream().noneMatch(n -> n.getId() == s.getId())) {
                    shoesList.add(s);
                }
                shoesList.stream().filter(f -> f.getId() == s.getId()).forEach(f -> f.setQuantity(f.getQuantity() + 1));
            })
        );
        if(totalsumma == 1){
            shoesList.sort((s1, s2) -> s2.getQuantity()*s2.getPrice() - s1.getQuantity()*s1.getPrice());
        } else {
            shoesList.sort((s1, s2) -> s2.getQuantity() - s1.getQuantity());
        }
        System.out.println("\nTop " + antal + " mest sålda skor:\n");
        printHelp.printShoesSold(shoesList, antal);
    }
}
