package se.na.shoedatabase.view;

import java.util.InputMismatchException;
import java.util.Scanner;


public class InputView {

    private static InputView inputView;
    final Scanner scan = new Scanner(System.in);

    public static InputView getInputView() {
        if (inputView == null) {
            inputView = new InputView();
        }
        return inputView;
    }
    public int inputInt(String text, boolean showtext) {
        while (true) {
            if(showtext) {
                System.out.println(text);
            }
            try {
                return Integer.parseInt(scan.nextLine());
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
                return Long.parseLong(scan.nextLine());
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
