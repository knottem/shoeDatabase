package se.na.shoedatabase;

import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static InputView inputView = new InputView();
    static Repository rep = new Repository();
    static PrintHelp printHelp = new PrintHelp();

    boolean repeat = true;


    private void Program(){

        System.out.println("Välkommen till skobutiken");
        do{
            System.out.println("Vad vill du göra?\n1. Logga in\n2. Ny Kund\n3. Avsluta programmet");
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
        Customer customer = rep.getCustomer(ssn, pass);
        if(customer != null){
            int orderid = 0;
            ArrayList<Shoe> shoes;
            System.out.println(customer);
            while (true) {
                System.out.println("Vad vill du göra?\n1. Lägg till ny beställning\n2. Lägg till beställning till nuvarande beställning\n3. Se din beställning\n4. Avsluta");
                switch (inputView.inputInt("", false)) {
                    case 1 -> {
                        orderid = 0;
                        boolean repeatorder = false;
                        do {
                            shoes = rep.getAllShoes();
                            printHelp.printShoes(shoes);
                            int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
                            for (Shoe shoe : shoes) {
                                if (shoe.getId() == answer) {
                                    System.out.println(shoe + "\när Du säker på att du vill beställa denna sko? 1 för ja, 2 för nej");
                                    if (inputView.inputInt("", false) == 1) {
                                        if(orderid == 0) {
                                            orderid = rep.addOrder(0, customer.getId(), shoe.getId());
                                        } else
                                            rep.addOrder(orderid, customer.getId(), shoe.getId());
                                        if (orderid > 0) {
                                            System.out.println("Skon tillagt till order " + orderid);
                                        }
                                    }
                                    if (inputView.inputInt("Vill du lägga till fler skor i denna beställning? 1 för ja, 2 för nej", true) == 1) {
                                        repeatorder = true;
                                    } else {
                                        ArrayList<Orders> orders;
                                        orders = rep.getOrders(orderid, customer.getId());
                                        System.out.println("-----------------------------------------------------");
                                        System.out.println("Din beställning är gjord med: ");
                                        for (int i = 0; i < orders.size(); i++) {
                                            for (int j = 0; j < orders.get(i).getShoes().size(); j++) {
                                                System.out.println(orders.get(i).getShoes().get(j).toStringWithQuantity());
                                            }
                                        }
                                        System.out.println("-----------------------------------------------------");
                                        repeatorder = false;
                                    }
                                }
                            }
                        } while(repeatorder);
                    }
                    case 2 -> {
                        shoes = rep.getAllShoes();
                        printHelp.printShoes(shoes);
                        int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
                        for (Shoe shoe : shoes) {
                            if (shoe.getId() == answer) {
                                System.out.println(shoe + "\när Du säker på att du vill lägga till denna sko till beställningnr " + orderid + "? 1 för ja, 2 för nej");
                                if (inputView.inputInt("", false) == 1) {
                                    rep.addOrder(orderid, customer.getId(), shoe.getId());
                                    System.out.println("Skon tillagt till order " + orderid);
                                }
                            }
                        }
                    }
                    case 3 -> {
                        ArrayList<Orders> orders;
                        orders = rep.getOrders(orderid, customer.getId());
                        for (int i = 0; i < orders.size(); i++) {
                            for (int j = 0; j < orders.get(i).getShoes().size(); j++) {
                                System.out.println(orders.get(i).getShoes().get(j).toString());
                            }
                        }

                    }
                    case 4 -> System.exit(0);
                    default -> System.out.println("Felaktigt nummer");
                }
            }
        } else {
            System.out.println("Användare finns ej / fel lösenord");
        }


    }

    public static void main(String[] args) {
        Main m = new Main();
        m.Program();
    }
}
