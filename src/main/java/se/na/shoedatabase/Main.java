package se.na.shoedatabase;

import se.na.shoedatabase.model.Customer;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Shoe;
import se.na.shoedatabase.view.InputView;

import java.util.ArrayList;

public class Main {

    static InputView inputView = new InputView();
    public static void main(String[] args) {
        Repository rep = new Repository();
        ArrayList<Shoe> shoes;
        Customer customer = rep.getCustomer(9901011234L, "123");
        if(customer != null){
            System.out.println(customer);
            System.out.println("Vad vill du göra?\n1.Lägg till beställning");
            switch (inputView.inputInt("", false)){
                case 1 -> {
                    shoes = rep.listAllShoes();
                    System.out.printf("--------------------------------------------------%n");
                    System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", "ID", "Märke", "Storlek", "Färg", "Mängd", "Pris" );
                    System.out.printf("--------------------------------------------------%n");
                    for (Shoe shoe : shoes) {
                        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", shoe.getId(), shoe.getBrand(), shoe.getSize(), shoe.getColor(), shoe.getQuantity(), shoe.getPrice());
                    }
                    System.out.printf("---------------------------------------------------%n");
                    int answer = inputView.inputInt("Skriv nummer på viken sko du vill beställa:", true);
                    for (Shoe shoe : shoes) {
                        if (shoe.getId() == answer) {
                            System.out.println(shoe + "\när Du säker på att du vill beställa denna sko? 1 för ja, 2 för nej");
                            if(inputView.inputInt("", false) == 1){
                                System.out.println(rep.addOrder(0, customer.getId(), shoe.getId()));
                            }
                        }
                    }

                }
                case 2 -> {

                }
            }
        }
    }
}
