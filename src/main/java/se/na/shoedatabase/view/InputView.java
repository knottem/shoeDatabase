package se.na.shoedatabase.view;

import java.util.InputMismatchException;
import java.util.Scanner;


public class InputView {

    Scanner scan = new Scanner(System.in);
    public int inputInt(String text, boolean showtext) {
        while (true) {
            if(showtext) {
                System.out.println(text);
            }
            try {
                int temp = scan.nextInt();
                scan.nextLine();
                return temp;
            } catch (InputMismatchException e) {
                System.out.println("Förväntade mig ett nummer");
            } catch (NumberFormatException e) {
                System.out.println("Inte nummer");
            }
        }
    }

    public long inputLong(String text, boolean showtext){
        while (true){
            if(showtext){
                System.out.println(text);
            }
            try {
                long temp = scan.nextLong();
                scan.nextLine();
                return temp;
            } catch (InputMismatchException e) {
                System.out.println("Förväntade mig ett nummer");
            } catch (NumberFormatException e) {
                System.out.println("Inte nummer");
            }
        }
    }

    public String inputString(String text, boolean showtext){
        if(showtext){
            System.out.println(text);
        }
        return scan.nextLine();
    }
}
