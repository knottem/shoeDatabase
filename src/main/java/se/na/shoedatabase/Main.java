package se.na.shoedatabase;

import se.na.shoedatabase.model.Customer;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.Shoe;
import se.na.shoedatabase.view.InputView;

import java.util.ArrayList;

public class Main {

    static InputView inputView = new InputView();

    public static void main(String[] args) {
        Repository rep = new Repository();
        ArrayList<Shoe> shoes;
        Customer customer = rep.getCustomer(9901011234L, "123");
        if (customer != null) {
            int orderid = 0;
            System.out.println(customer);
            while (true) {
                System.out.println("Vad vill du göra?\n1. Lägg till ny beställning\n2. Lägg till beställning till nuvarande beställning\n3. Se din beställning\n4. Avsluta");
                switch (inputView.inputInt("", false)) {
                    case 1 -> {
                        shoes = rep.getAllShoes();
                        System.out.printf("----------------------------------------------------%n");
                        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", "ID", "Märke", "Storlek", "Färg", "Mängd", "Pris");
                        System.out.printf("----------------------------------------------------%n");
                        for (Shoe shoe : shoes) {
                            System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", shoe.getId(), shoe.getBrand(), shoe.getSize(), shoe.getColor(), shoe.getQuantity(), shoe.getPrice());
                        }
                        System.out.printf("-----------------------------------------------------%n");
                        int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
                        for (Shoe shoe : shoes) {
                            if (shoe.getId() == answer) {
                                System.out.println(shoe + "\när Du säker på att du vill beställa denna sko? 1 för ja, 2 för nej");
                                if (inputView.inputInt("", false) == 1) {
                                    orderid = rep.addOrder(0, customer.getId(), shoe.getId());
                                    if (orderid > 0) {
                                        System.out.println("Skon tillagt till order " + orderid);
                                    }
                                }
                            }
                        }

                    }
                    case 2 -> {
                        shoes = rep.getAllShoes();
                        System.out.printf("----------------------------------------------------%n");
                        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", "ID", "Märke", "Storlek", "Färg", "Mängd", "Pris");
                        System.out.printf("----------------------------------------------------%n");
                        for (Shoe shoe : shoes) {
                            System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", shoe.getId(), shoe.getBrand(), shoe.getSize(), shoe.getColor(), shoe.getQuantity(), shoe.getPrice());
                        }
                        System.out.printf("-----------------------------------------------------%n");
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
                }
            }
        }
    }
}
