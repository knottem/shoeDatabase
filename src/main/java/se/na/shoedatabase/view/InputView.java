package se.na.shoedatabase.view;

import java.util.InputMismatchException;
import java.util.Scanner;


public class InputView {

    public int inputInt(String text, boolean showtext) {
        while (true) {
            Scanner scan = new Scanner(System.in);
            if(showtext) {
                System.out.println(text);
            }
            try {
                return scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Förväntade mig ett nummer");
            } catch (NumberFormatException e) {
                System.out.println("Inte nummer");
            }
        }
    }

    public long inputLong(String text, boolean showtext){
        while (true){
            Scanner scan = new Scanner(System.in);
            if(showtext){
                System.out.println(text);
            }
            try {
                return scan.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Förväntade mig ett nummer");
            } catch (NumberFormatException e) {
                System.out.println("Inte nummer");
            }
        }
    }
}
