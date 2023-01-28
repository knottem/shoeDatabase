package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;

public class Rapports {

    Repository rep = Repository.getRepository();
    PrintHelp printHelp = PrintHelp.getPrintHelp();
    InputView inputView = InputView.getInputView();

    ArrayList<Shoe> shoes = rep.getAllShoes();
    ArrayList<Customer> customers = rep.getAllCustomers();
    ArrayList<Orders> orders = rep.getAllOrders(shoes, customers);

    public void checkRapports(){
        while(true){
            switch (inputView.inputInt("""
                    Vilken Rapport vill du titta på?
                    1. Visa alla kunder som har köpt av visst märke, färg eller storlek.
                    2. Visa alla kunder och sammanlagda ordrar varje kund lagt.
                    3. Visa hur mycket varje kund har beställt för
                    4. Visa beställningsvärde per ort
                    5. Visa Topplista över mest sålda skor
                    6. Avsluta""", true)){
                case 1 -> listAllBought();
                case 2 -> listCustomerOrders();
                case 3 -> listCustomersTotalSpending();
                case 4 -> listSpendingPerCity();
                case 5 -> listTopSellingShoes();
                case 6 -> System.exit(0);
                default -> System.out.println("Felaktigt siffra");
            }
        }
    }

    private void listAllBought(){

    }
    private void listCustomerOrders(){

    }
    private void listCustomersTotalSpending(){

    }
    private void listSpendingPerCity(){

    }
    private void listTopSellingShoes(){

    }
}
