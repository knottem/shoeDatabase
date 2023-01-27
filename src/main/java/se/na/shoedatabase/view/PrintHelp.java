package se.na.shoedatabase.view;

import se.na.shoedatabase.model.shoe.Shoe;
import java.util.ArrayList;

public class PrintHelp {

    public void printShoes(ArrayList<Shoe> shoes){
        System.out.printf("----------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n", "Nr", "Märke", "Storlek", "Färg", "Mängd", "Kategori", "Pris");
        System.out.printf("----------------------------------------------------------------------------%n");
        shoes.stream().filter(s -> s.getQuantity() > 0).forEach(s ->
            System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n",
                    s.getId(), s.getBrand(), s.getSize(), s.getColor(), s.getQuantity(), s.getCategoriesNames(), s.getPrice()));
        System.out.printf("----------------------------------------------------------------------------%n");
    }

    public void printOrdersList(ArrayList<Integer> numbers, ArrayList<Shoe> shoes){
        System.out.printf("------------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %5s | %-20s | %4s | %3s |%n", "OrderNr", "Märke", "Storlek", "Färg", "Kategori", "Pris", "Mängd");
        System.out.printf("------------------------------------------------------------------------------%n");
        for (int i = 0; i < numbers.size(); i+=3) {
            System.out.printf("| %-7s |", numbers.get(i));
            int finalI = i+1;
            shoes.stream().filter(s -> s.getId() == numbers.get(finalI)).forEach(s ->
                System.out.printf(" %-9s | %7s | %5s | %-20s | %4s ",
                        s.getBrand(), s.getSize(), s.getColor(), s.getCategoriesNames(), s.getPrice()));
            System.out.printf("|%6s |%n", numbers.get(i+2));
        }
        System.out.printf("------------------------------------------------------------------------------%n");
    }
}


