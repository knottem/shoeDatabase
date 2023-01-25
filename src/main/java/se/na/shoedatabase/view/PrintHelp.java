package se.na.shoedatabase.view;

import se.na.shoedatabase.model.Shoe;

import java.util.ArrayList;

public class PrintHelp {

    public void printShoes(ArrayList<Shoe> shoes){
        System.out.printf("----------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n", "ID", "Märke", "Storlek", "Färg", "Mängd", "Pris");
        System.out.printf("----------------------------------------------------%n");
        for (Shoe shoe : shoes) {
            System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %4s |%n",
                    shoe.getId(), shoe.getBrand(), shoe.getSize(), shoe.getColor(), shoe.getQuantity(), shoe.getPrice());
        }
        System.out.printf("-----------------------------------------------------%n");
    }
}

