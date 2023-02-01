package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Encrypt;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Admin;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Address;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Rapports {

    Repository rep = Repository.getRepository();
    PrintHelp printHelp = PrintHelp.getPrintHelp();
    InputView inputView = InputView.getInputView();

    ArrayList<Shoe> shoes;
    ArrayList<Customer> customers;
    ArrayList<Orders> orders;

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
        String answer;
        switch (inputView.inputInt("""
                Vad vill du söka på?
                1. Märke
                2. Färg
                3. Storlek""", true)){
            case 1 -> {
                ArrayList<String> temp = new ArrayList<>();
                System.out.println("Skriv ett Märke:");
                answer = inputView.inputString("", false);
                temp.add("Personer som beställt skor med märket " +
                        answer.substring(0,1).toUpperCase() + answer.substring(1).toLowerCase() + "\n");
                orders.stream().filter(s -> {
                   for (int i = 0; i < s.getShoes().size(); i++) {
                       if(s.getShoes().get(i).getBrand().equalsIgnoreCase(answer)){
                           return true;
                       }
                   }
                   return false;
                }).forEach(a -> temp.add("Namn: " + a.getCustomer().getFirstname() + " " + a.getCustomer().getLastname() + "\n" +
                        a.getCustomer().getAddress().toString() + "\n"));
                temp.stream().distinct().forEach(System.out::println);
            }

            case 2 -> {
                ArrayList<String> temp = new ArrayList<>();
                System.out.println("Skriv en Färg:");
                answer = inputView.inputString("", false);
                temp.add("Personer som beställt skor med färgen " +
                        answer.substring(0,1).toUpperCase() + answer.substring(1).toLowerCase() + "\n");
                orders.stream().filter(s -> {
                    for (int i = 0; i < s.getShoes().size(); i++) {
                        if(s.getShoes().get(i).getColor().equalsIgnoreCase(answer)){
                            return true;
                        }
                    }
                    return false;
                }).forEach(a ->
                    temp.add("Namn: " + a.getCustomer().getFirstname() + " " + a.getCustomer().getLastname() + "\n" +
                            a.getCustomer().getAddress().toString() + "\n"));
                temp.stream().distinct().forEach(System.out::println);
            }

            case 3 -> {
                ArrayList<String> temp = new ArrayList<>();
                System.out.println("Skriv en Storlek:");
                int answerInt = inputView.inputInt("", false);
                temp.add("Personer som beställt skor med storleken " + answerInt + "\n");
                orders.stream().filter(s -> {
                    for (int i = 0; i < s.getShoes().size(); i++) {
                        if(s.getShoes().get(i).getSize() == answerInt){
                            return true;
                        }
                    }
                    return false;
                }).forEach(a -> temp.add("Namn: " + a.getCustomer().getFirstname() + " " + a.getCustomer().getLastname() + "\n" +
                        a.getCustomer().getAddress().toString() + "\n"));
                temp.stream().distinct().forEach(System.out::println);
            }
            default -> System.out.println("Felaktigt siffra");
        }
    }

    private void listCustomerOrdersX(){
        AtomicInteger totalOrders = new AtomicInteger();
        customers.forEach(c -> {
            totalOrders.set(0);
            orders.stream().filter(f -> f.getCustomer().getId() == c.getId()).forEach(o -> totalOrders.getAndIncrement());
            System.out.println("Namn: " + c.getFirstname() + " " + c.getLastname() + "\n" +
                    c.getAddress().toString() + "\nAntal ordrar: " + totalOrders + "\n");
        });
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
        ArrayList<Address> addresses = new ArrayList<>();
        orders.forEach(o -> {
            if(addresses.stream().noneMatch(s -> s.getCity().equals(o.getCustomer().getAddress().getCity()))){
                addresses.add(new Address(o.getCustomer().getAddress().getCity()));
            }
            addresses.stream().filter(f -> f.getCity().equals(o.getCustomer().getAddress().getCity())).forEach(a -> {
                for (int i = 0; i < o.getShoes().size() ; i++) {
                    a.setZipcode(a.getZipcode() + o.getShoes().get(i).getPrice()*o.getShoes().get(i).getQuantity());
                }
            });
        });
        addresses.sort((s1, s2) -> s2.getZipcode() - s1.getZipcode());
        System.out.println("\nMest sålda i städerna:\n");
        addresses.forEach(s -> System.out.println("Postort: " + s.getCity() + " Total Summa: " + s.getZipcode() + " kr\n"));
    }

    private void listTopSellingShoes(int antal, int totalsumma){
        ArrayList<Shoe> shoesList = new ArrayList<>();
        orders.forEach(o -> {
            for (int i = 0; i < o.getShoes().size(); i++) {
                int finalI = i;
                if(shoesList.stream().noneMatch(s -> s.getId() == o.getShoes().get(finalI).getId())) {
                   shoesList.add(o.getShoes().get(finalI));
                }
                shoesList.stream().filter(f -> f.getId() == o.getShoes().get(finalI).getId()).forEach(s -> s.setQuantity(s.getQuantity() + 1));
            }
        });
        if(totalsumma == 1){
            shoesList.sort((s1, s2) -> s2.getQuantity()*s2.getPrice() - s1.getQuantity()*s1.getPrice());
        } else {
            shoesList.sort((s1, s2) -> s2.getQuantity() - s1.getQuantity());
        }

        System.out.println("\nTop " + antal + " mest sålda skor:\n");
        shoesList.stream().limit(antal).forEach(s -> System.out.println(
                "Märke: " + s.getBrand() +
                "\nStorlek: " + s.getSize() +
                "\nFärg: " + s.getColor() +
                "\nKategori: " + s.getCategoriesNames() +
                "\nPris: " + s.getPrice() + " kr" +
                "\nTotal Summa: " + s.getPrice()*s.getQuantity() + " kr" +
                "\nAntal Sålda: " + s.getQuantity() + "\n"));
    }
}
