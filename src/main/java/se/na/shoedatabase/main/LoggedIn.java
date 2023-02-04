package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Encrypt;
import se.na.shoedatabase.interfaces.ShoeSearchInterface;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LoggedIn {

    ArrayList<Shoe> shoes;
    final Repository rep = Repository.getRepository();
    final PrintHelp printHelp = PrintHelp.getPrintHelp();
    final InputView inputView = InputView.getInputView();

    final ShoeSearchInterface colorSearch = (a, b) -> a.getColor().equalsIgnoreCase(b);
    final ShoeSearchInterface brandSearch = (a, b) -> a.getBrand().equalsIgnoreCase(b);
    final ShoeSearchInterface categorySearch = (a, b) -> a.getCategoriesNames().contains(b);
    final ShoeSearchInterface sizeSearch = (a, b) -> Integer.toString(a.getSize()).equalsIgnoreCase(b);

    public void login(){
        Customer customer = rep.getCustomer(inputView.inputLong("Vad är ditt personnummer?", true), Encrypt.encryptSHA3(inputView.inputString("Vad är ditt lösenord?",true)));
        if(customer != null){
            ArrayList<Shoe> shoes = rep.getAllShoes();
            System.out.println("Välkommen in " + customer.getFirstname() + " " + customer.getLastname());
            boolean repeat = true;
            while (repeat) {
                System.out.println("""
                        Vad vill du göra?
                        1. Sök på skor
                        2. Lägg till ny beställning
                        3. Lägg till skor till en befintligt beställning.
                        4. Se en specifik order.
                        5. Visa dina ordrar.
                        6. Logga ut""");
                switch (inputView.inputInt("", false)) {
                    case 1 -> searchShoes();
                    case 2 -> newOrder(customer);
                    case 3 -> changeOrder(customer);
                    case 4 -> searchOrders(customer);
                    case 5 -> printHelp.printAllOrders(rep.getOrdersForCustomer(customer, shoes));
                    case 6 -> repeat = false;
                    default -> System.out.println("Felaktigt nummer");
                }
            }
        } else {
            System.out.println("Användare finns ej / fel lösenord");
        }
    }

    private void newOrder(Customer customer){
        int orderId = 0;
        boolean repeatOrder = false;
        do {
            shoes = rep.getAllShoes();
            printHelp.printShoes(shoes);
            int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
            for (Shoe shoe : shoes) {
                if (shoe.getId() == answer) {
                    System.out.println(shoe + "\nÄr du säker på att du vill beställa denna sko? 1 för ja, 2 för nej");
                    if (inputView.inputInt("", false) == 1) {
                        if(orderId == 0) {
                            orderId = rep.addOrder(0, customer.getId(), shoe.getId());
                        } else
                            rep.addOrder(orderId, customer.getId(), shoe.getId());
                        if (orderId > 0) {
                            System.out.println("Skon tillagt till order " + orderId);
                        }
                    } else {
                        System.out.println("Ingen sko tillagd");
                    }
                    if (inputView.inputInt("Vill du lägga till fler skor i denna beställning? 1 för ja, 2 för nej", true) == 1) {
                        repeatOrder = true;
                    } else {
                        ArrayList<Orders> orders;
                        orders = rep.getOrders(orderId, customer);
                        System.out.println("Din beställning är gjord med: ");
                        printHelp.printAllOrders(orders);
                        repeatOrder = false;
                    }
                }
            }
        } while(repeatOrder);
    }

    private void changeOrder(Customer customer){
        int orderId;
        Date time = new Date(System.currentTimeMillis() - 3600 * 1000);
        shoes = rep.getAllShoes();
        List<Orders> orders = rep.getOrdersForCustomer(customer, shoes).stream().filter(o -> o.getTimestamp().after(time)).toList();
        if(orders.size() > 0){
            System.out.println("Ordrar som är skapade mindre än 1 timme sedan.");
            printHelp.printAllOrders(orders);
            if(orders.size() == 1){
                orderId = orders.get(0).getId();
                printHelp.printShoes(shoes);
                int answer = inputView.inputInt("Skriv nummer på viken sko du vill lägga till till denna order:", true);
                Shoe shoe = shoes.stream().filter(s -> s.getId() == answer).findFirst().orElse(null);
                if(shoe != null) {
                    rep.addOrder(orderId, customer.getId(), shoe.getId());
                    System.out.println("Skon tillagt till order " + orderId + "\n");
                } else {
                    System.out.println("Felaktigt nummer på sko");
                }
            } else {
                System.out.println("Vilken order vill du ändra på?");
            }
        } else {
            System.out.println("Du har inga ordrar du kan ändra på");
        }
    }

    private void searchOrders(Customer customer){
        ArrayList<Orders> orders = rep.getOrders(
                inputView.inputInt("Vilken order vill du se?, gå till visa dina ordrar för att se dina ordernummer", true),
                customer);
        if(orders.get(0).getShoes().size() > 0){
            printHelp.printAllOrders(orders);
        } else {
            System.out.println("Felaktigt ordernummer, försök igen.\n");
        }
    }

    private void searchShoes() {
        shoes = rep.getAllShoes();
        System.out.println("""
                Vad vill du söka på?
                1. Färg
                2. Märke
                3. Kategori
                4. Storlek""");
        switch (inputView.inputInt("Svara med siffra", true)) {
            case 1 -> searchShoesByInterface(inputView.inputString("Skriv en färg:", true), colorSearch);
            case 2 -> searchShoesByInterface(inputView.inputString("Skriv ett Märke:", true), brandSearch);
            case 3 -> searchShoesByInterface(inputView.inputString("Skriv en Kategori: ", true), categorySearch);
            case 4 -> searchShoesByInterface(inputView.inputString("Skriv en storlek: ", true), sizeSearch);
            default -> System.out.println("Felaktigt nummer");
        }
    }

    private void searchShoesByInterface(String answer, ShoeSearchInterface ssi){
        ArrayList<Shoe> temp = shoes.stream().filter(s -> ssi.search(s, answer)).collect(Collectors.toCollection(ArrayList::new));
        if(temp.size() > 0){
            printHelp.printShoes(temp);
        } else {
            System.out.println("Hittade inga skor");
        }
    }
}
