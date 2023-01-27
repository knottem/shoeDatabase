package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.view.InputView;
import se.na.shoedatabase.view.PrintHelp;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                    } else {
                        System.out.println("Ingen sko tillagd");
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
        ArrayList<Orders> orders;
        orders = rep.getOrders(
                inputView.inputInt("Vilken order vill du se?, gå till visa dina ordrar för att se dina ordernummer", true),
                customer.getId());
        for (Orders order : orders) {
            if (!(order.getShoes().size() == 0)) {
                for (int j = 0; j < order.getShoes().size(); j++) {
                    System.out.println(order.getShoes().get(j).toString());
                }
            } else {
                System.out.println("Felaktigt ordernummer, försök igen.");
            }
            System.out.println();
        }

    }

    public void searchShoes(ArrayList<Shoe> shoes) {
        ArrayList<Shoe> tempShoes = null;
        Scanner scan = new Scanner(System.in);
        String answer;
        System.out.println("""
                Vad vill du söka på?
                1. Färg
                2. Märke
                3. Kategori""");
        switch (inputView.inputInt("Svara med siffra", true)) {
            case 1 -> {
                System.out.println("Skriv en färg:");
                answer = scan.nextLine();
                tempShoes = shoes.stream().filter(s -> s.getColor().equalsIgnoreCase(answer)).collect(Collectors.toCollection(ArrayList::new));
            }
            case 2 -> {
                System.out.println("Skriv ett Märke:");
                answer = scan.nextLine();
                tempShoes = shoes.stream().filter(s -> s.getBrand().equalsIgnoreCase(answer)).collect(Collectors.toCollection(ArrayList::new));
            }
            case 3 -> {
                System.out.println("Skriv en Kategori: ");
                answer = scan.nextLine();
                tempShoes = shoes.stream().filter(s -> s.getCategoriesNames().toString().contains(answer)).collect(Collectors.toCollection(ArrayList::new));
            }
            default -> System.out.println("Felaktigt nummer");
        }
        if(!(tempShoes.size() == 0)){
            printHelp.printShoes(tempShoes);
        } else {
            System.out.println("Hittade inga skor");
        }

    }
}
