package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;

public class InitShoeDatabase {

    static InputView inputView = InputView.getInputView();
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
            3. Kolla Rapporter
            4. Avsluta programmet""");
            int answer = inputView.inputInt("Svara med Siffra", true);
            switch(answer){
                case 1 -> login();
                case 2 -> createUser();
                case 3 -> checkRapports();
                case 4 -> System.exit(0);
                default -> System.out.println("Felaktig Siffra");
            }
        } while (repeat);
    }
    private void login(){
        /*
        long ssn = inputView.inputLong("Vad är ditt personnummer?", true);
        String pass = inputView.inputString("Vad är ditt lösenord?",true);

         */
        //temp
        long ssn = 9901011234L;
        String pass = "123";
        ArrayList<Shoe> shoes = rep.getAllShoes();
        Customer customer = rep.getCustomer(ssn, pass);
        if(customer != null){
            System.out.println("Välkommen in " + customer.getFirstname() + " " + customer.getLastname());
            while (true) {
                System.out.println("""
                        Vad vill du göra?
                        1. Sök på skor
                        2. Lägg till ny beställning
                        3. Se en specifik order.
                        4. Visa dina ordrar.
                        5. Avsluta""");
                switch (inputView.inputInt("", false)) {
                    case 1 -> loggedIn.searchShoes(shoes);
                    case 2 -> loggedIn.newOrder(customer);
                    case 3 -> loggedIn.searchOrders(customer);
                    case 4 -> printHelp.printShoesFromList(rep.getOrderNumbers(customer), shoes);
                    case 5 -> System.exit(0);
                    default -> System.out.println("Felaktigt nummer");
                }
            }
        } else {
            System.out.println("Användare finns ej / fel lösenord");
        }


    }

    private void checkRapports(){
        ArrayList<Shoe> shoes = rep.getAllShoes();
        ArrayList<Customer> customers = rep.getAllCustomers();
        ArrayList<Orders> orders = rep.getAllOrders(shoes, customers);
        printHelp.printAllOrders(orders);
    }

    private void createUser(){

    }
}
