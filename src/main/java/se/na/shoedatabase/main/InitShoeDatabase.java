package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;

public class InitShoeDatabase {

    InputView inputView = InputView.getInputView();
    Repository rep = Repository.getRepository();
    PrintHelp printHelp = PrintHelp.getPrintHelp();
    private final LoggedIn loggedIn = new LoggedIn();
    private final Rapports rapports = new Rapports();

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
                case 3 -> rapports.checkRapports();
                case 4 -> System.exit(0);
                default -> System.out.println("Felaktig Siffra");
            }
        } while (true);
    }
    private void login(){
        /*
        long ssn = inputView.inputLong("Vad är ditt personnummer?", true);
        String pass = inputView.inputString("Vad är ditt lösenord?",true);

         */
        //temp
        long ssn = 9901011234L;
        String pass = "123";
        Customer customer = rep.getCustomer(ssn, pass);
        boolean repeat = true;
        if(customer != null){
            ArrayList<Shoe> shoes = rep.getAllShoes();
            System.out.println("Välkommen in " + customer.getFirstname() + " " + customer.getLastname());
            while (repeat) {
                System.out.println("""
                        Vad vill du göra?
                        1. Sök på skor
                        2. Lägg till ny beställning
                        3. Se en specifik order.
                        4. Visa dina ordrar.
                        5. Logga ut""");
                switch (inputView.inputInt("", false)) {
                    case 1 -> loggedIn.searchShoes(shoes);
                    case 2 -> loggedIn.newOrder(customer);
                    case 3 -> loggedIn.searchOrders(customer);
                    case 4 -> printHelp.printAllOrders(rep.getOrdersForCustomer(customer, shoes));
                    case 5 -> repeat = false;
                    default -> System.out.println("Felaktigt nummer");
                }
            }
        } else {
            System.out.println("Användare finns ej / fel lösenord");
        }


    }

    private void createUser(){

    }
}
