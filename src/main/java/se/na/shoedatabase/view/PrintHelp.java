package se.na.shoedatabase.view;

import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class PrintHelp {

    private static PrintHelp printhelp;

    public static PrintHelp getPrintHelp(){
       if(printhelp == null){
           printhelp = new PrintHelp();
       }
       return printhelp;
    }

    public void printShoes(ArrayList<Shoe> shoes){
        shoes.sort(Comparator.comparingInt(Shoe::getId));
        System.out.printf("----------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n", "Nr", "Märke", "Storlek", "Färg", "Mängd", "Kategori", "Pris");
        System.out.printf("----------------------------------------------------------------------------%n");
        shoes.stream().filter(s -> s.getQuantity() > 0).forEach(s ->
            System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n",
                    s.getId(), s.getBrand(), s.getSize(), s.getColor(), s.getQuantity(), s.getCategoriesNames(), s.getPrice()));
        System.out.printf("----------------------------------------------------------------------------%n");
    }

    public void printShoesSold(ArrayList<Shoe> shoes, int antal){
        System.out.printf("-----------------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %6s | %-20s | %4s | %5s | %7s |%n", "Nr", "Märke", "Storlek", "Färg", "Kategori", "Pris",  "Mängd", "Totalt");
        System.out.printf("-----------------------------------------------------------------------------------%n");
        shoes.stream().limit(antal).forEach(s ->
                System.out.printf("| %-2s | %-9s | %7s | %6s | %-20s | %4s | %5s | %7s |%n",
                        s.getId(), s.getBrand(), s.getSize(), s.getColor(), s.getCategoriesNames(), s.getPrice(),  s.getQuantity(), s.getQuantity()*s.getPrice()));
        System.out.printf("-----------------------------------------------------------------------------------%n");
    }

    public void printShoesFromList(ArrayList<Integer> numbers, ArrayList<Shoe> shoes){
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

    public void printAllOrders(ArrayList<Orders> orders){
        System.out.printf("---------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n", "Nr", "Märke", "Storlek", "Färg", "Mängd", "Kategori", "Pris");
        System.out.printf("---------------------------------------------------------------------------%n");
        orders.forEach(o -> {
            int total = 0;
            for (int i = 0; i < o.getShoes().size() ; i++) {
                System.out.printf("| %-2s | %-9s | %7s | %6s | %5s | %-20s | %4s |%n",
                        o.getShoes().get(i).getId(),
                        o.getShoes().get(i).getBrand(),
                        o.getShoes().get(i).getSize(),
                        o.getShoes().get(i).getColor(),
                        o.getShoes().get(i).getQuantity(),
                        o.getShoes().get(i).getCategoriesNames(),
                        o.getShoes().get(i).getPrice());
                total = total + o.getShoes().get(i).getPrice() * o.getShoes().get(i).getQuantity();

            }
            System.out.printf("| %-71s |%n", "Kund: " + o.getCustomer().getFirstname() + " " + o.getCustomer().getLastname()
                    + ", Ordernr: " + o.getId() + ", Summa: " + total + " kr");
            System.out.printf("| %-71s |%n", "Datum: " + o.getTimestamp());
            System.out.printf("---------------------------------------------------------------------------%n");
        });
    }

    public void printCustomers(List<Customer> customerList) {
        System.out.printf("----------------------------------------------------------------------------------------------------%n");
        System.out.printf("| %-2s | %-16s | %-12s | %-18s | %-10s | %-10s | %-10s |%n", "Id", "Namn", "Personnummer", "Gatuadress", "Gatunummer", "Postnr", "Postort");
        System.out.printf("----------------------------------------------------------------------------------------------------%n");
        customerList.forEach(c ->
                System.out.printf("| %-2s | %-16s | %-12s | %-18s | %-10s | %-10s | %-10s |%n",
                        c.getId(),
                        c.getFirstname() + " " + c.getLastname(),
                        c.getSsn(),
                        c.getAddress().getAddress(),
                        c.getAddress().getAddressNumber(),
                        c.getAddress().getZipcode(),
                        c.getAddress().getCity())
        );
        System.out.printf("----------------------------------------------------------------------------------------------------%n\n");
    }

    public void printCities(Map<String, Integer> spendingPerCity) {
        System.out.printf("-----------------------------------%n");
        System.out.printf("| %-31s |%n","Mest sålda i städerna");
        System.out.printf("-----------------------------------%n");
        System.out.printf("| %-16s | %12s |%n", "Stad", "Summa");
        System.out.printf("-----------------------------------%n");
        spendingPerCity.forEach((key, value) -> System.out.printf("| %-16s | %12s |%n", key, value + " kr"));
        System.out.printf("-----------------------------------%n\n");
    }
}


