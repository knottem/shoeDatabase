package se.na.shoedatabase.view;

import se.na.shoedatabase.model.shoe.Shoe;
import java.util.ArrayList;

public class PrintHelp {

    public void printShoes(ArrayList<Shoe> shoes){
        System.out.printf("----------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n", "Nr", "Märke", "Storlek", "Färg", "Mängd", "Kategori", "Pris");
        System.out.printf("----------------------------------------------------------------------------%n");
        shoes.stream().filter(s -> s.getQuantity() > 0).forEach(s -> {
            StringBuilder categories = new StringBuilder();
            for (int i = 0; i < s.getCategories().size(); i++) {
                categories.append(s.getCategories().get(i).getCategoryname()).append(", ");
                if(i == s.getCategories().size()-1){
                    categories.setLength((categories.length()-2));
                }
            }
            System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n",
                    s.getId(), s.getBrand(), s.getSize(), s.getColor(), s.getQuantity(), categories, s.getPrice());
        });
        System.out.printf("----------------------------------------------------------------------------%n");
    }
}


