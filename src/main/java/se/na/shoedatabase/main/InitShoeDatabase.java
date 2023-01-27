package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;

public class InitShoeDatabase {

    static InputView inputView = new InputView();
    static Repository rep = new Repository();
    static PrintHelp printHelp = new PrintHelp();
    LoggedIn loggedIn = new LoggedIn();

    boolean repeat = true;

    public void Program(){

        System.out.println("Välkommen till skobutiken");
        do{
            System.out.println("""
            Vad vill du göra?
            1. Logga in
            2. Ny Kund
            3. Avsluta programmet""");
            int answer = inputView.inputInt("Svara med Siffra", true);
            switch(answer){
                case 1 -> login();
                //case 2 -> createUser();
                case 3 -> System.exit(0);
                default -> System.out.println("Felaktig Siffra");
            }
        } while (repeat);
    }
    private void login(){
        /*long ssn = inputView.inputLong("Vad är ditt personnummer?", true);
        System.out.println("Vad är ditt lösenord?");
        Scanner scan = new Scanner(System.in);
        String pass = scan.nextLine();

         */
        //temp
        long ssn = 9901011234L;
        String pass = "123";
        ArrayList<Shoe> shoes = rep.getAllShoes();
        Customer customer = rep.getCustomer(ssn, pass);
        if(customer != null){
            int orderId = 0;
            System.out.println("Välkommen in " + customer.getFirstname() + " " + customer.getLastname());
            while (true) {
                System.out.println("""
                        Vad vill du göra?
                        1. Lägg till ny beställning
                        2. Lägg till beställning till nuvarande beställning
                        3. Se en specifik order.
                        4. Visa dina ordrar.
                        5. Avsluta""");
                switch (inputView.inputInt("", false)) {
                    case 1 -> orderId = loggedIn.newOrder(customer);
                    case 2 -> {
                        shoes = rep.getAllShoes();
                        printHelp.printShoes(shoes);
                        int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
                        for (Shoe shoe : shoes) {
                            if (shoe.getId() == answer) {
                                System.out.println(shoe + "\när Du säker på att du vill lägga till denna sko till beställningnr " + orderId + "? 1 för ja, 2 för nej");
                                if (inputView.inputInt("", false) == 1) {
                                    rep.addOrder(orderId, customer.getId(), shoe.getId());
                                    System.out.println("Skon tillagt till order " + orderId);
                                }
                            }
                        }
                    }
                    case 3 -> {
                        ArrayList<Orders> orders;
                        int answer = inputView.inputInt("Vilken order vill du se?, gå till visa dina ordrar för att se dina ordernummer", true);
                        orders = rep.getOrders(answer, customer.getId());
                        for (Orders order : orders) {
                            if (!(order.getShoes().size() == 0)) {
                                for (int j = 0; j < order.getShoes().size(); j++) {
                                    System.out.println(order.getShoes().get(j).toString());
                                }
                            } else {
                                System.out.println("Felaktigt ordernummer, försök igen.");
                            }
                        }

                    }
                    case 4 -> printHelp.printOrdersList(rep.getOrderNumbers(customer), shoes);
                    case 5 -> System.exit(0);
                    default -> System.out.println("Felaktigt nummer");
                }
            }
        } else {
            System.out.println("Användare finns ej / fel lösenord");
        }


    }
}
