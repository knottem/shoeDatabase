package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;

public class LoggedIn {

    ArrayList<Shoe> shoes;
    Repository rep = new Repository();
    PrintHelp printHelp = new PrintHelp();
    InputView inputView = new InputView();

    public int newOrder(Customer customer){
        int orderId = 0;
        boolean repeatorder = false;
        do {
            shoes = rep.getAllShoes();
            printHelp.printShoes(shoes);
            int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
            for (Shoe shoe : shoes) {
                if (shoe.getId() == answer) {
                    System.out.println(shoe.toString() + "\när Du säker på att du vill beställa denna sko? 1 för ja, 2 för nej");
                    if (inputView.inputInt("", false) == 1) {
                        if(orderId == 0) {
                            orderId = rep.addOrder(0, customer.getId(), shoe.getId());
                        } else
                            rep.addOrder(orderId, customer.getId(), shoe.getId());
                        if (orderId > 0) {
                            System.out.println("Skon tillagt till order " + orderId);
                        }
                    }
                    if (inputView.inputInt("Vill du lägga till fler skor i denna beställning? 1 för ja, 2 för nej", true) == 1) {
                        repeatorder = true;
                    } else {
                        ArrayList<Orders> orders;
                        orders = rep.getOrders(orderId, customer.getId());
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Din beställning är gjord med: ");
                        for (Orders order : orders) {
                            for (int j = 0; j < order.getShoes().size(); j++) {
                                System.out.println(order.getShoes().get(j).toStringWithQuantity());
                            }
                        }
                        System.out.println("-----------------------------------------------------");
                        repeatorder = false;
                    }
                }
            }
        } while(repeatorder);
        return orderId;
    }

    public void searchOrders(Customer customer){

    }
}
