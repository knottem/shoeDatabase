package se.na.shoedatabase;

import se.na.shoedatabase.model.Customer;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.Shoe;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Repository rep = new Repository();
        ArrayList<Shoe> shoes;
        Customer customer = rep.getCustomer(9901011234L, "123");
        if(customer != null){
            System.out.println(customer);
            Scanner scan = new Scanner(System.in);
            System.out.println("Vad vill du göra?\n1.Lägg till beställning");
            switch (scan.nextInt()){
                case 1 -> {
                    shoes = rep.listAllShoes();
                    for (Shoe shoe : shoes) {
                        System.out.println("Nr: " + shoe.getId() +
                                " Märke: " + shoe.getBrand() +
                                " Pris: " + shoe.getPrice());
                    }
                }
            }
        }
    }
}
