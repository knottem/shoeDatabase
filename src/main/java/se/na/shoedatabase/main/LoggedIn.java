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
import java.util.stream.Collectors;

public class LoggedIn {

    ArrayList<Shoe> shoes;
    Repository rep = Repository.getRepository();
    PrintHelp printHelp = PrintHelp.getPrintHelp();
    InputView inputView = InputView.getInputView();

    ShoeSearchInterface colorSearch = (a, b) -> a.getColor().equalsIgnoreCase(b);
    ShoeSearchInterface brandSearch = (a, b) -> a.getBrand().equalsIgnoreCase(b);
    ShoeSearchInterface categorySearch = (a, b) -> a.getCategoriesNames().contains(b);
    ShoeSearchInterface sizeSearch = (a, b) -> Integer.toString(a.getSize()).equalsIgnoreCase(b);

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
                        3. Se en specifik order.
                        4. Visa dina ordrar.
                        5. Logga ut""");
                switch (inputView.inputInt("", false)) {
                    case 1 -> searchShoes();
                    case 2 -> newOrder(customer);
                    case 3 -> searchOrders(customer);
                    case 4 -> printHelp.printAllOrders(rep.getOrdersForCustomer(customer, shoes));
                    case 5 -> repeat = false;
                    default -> System.out.println("Felaktigt nummer");
                }
            }
        } else {
            System.out.println("Användare finns ej / fel lösenord");
        }
    }

    private void newOrder(Customer customer){
        int orderId = 0;
        boolean repeatorder = false;
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
                        repeatorder = true;
                    } else {
                        ArrayList<Orders> orders;
                        orders = rep.getOrders(orderId, customer);
                        System.out.println("Din beställning är gjord med: ");
                        printHelp.printAllOrders(orders);
                        repeatorder = false;
                    }
                }
            }
        } while(repeatorder);
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
