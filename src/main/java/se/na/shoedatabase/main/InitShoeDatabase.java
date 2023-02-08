package se.na.shoedatabase.main;

import se.na.shoedatabase.dao.Encrypt;
import se.na.shoedatabase.dao.Repository;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.utility.PropertiesLoader;
import se.na.shoedatabase.view.InputView;

public class InitShoeDatabase {

    final InputView inputView = InputView.getInputView();
    final Repository rep = Repository.getRepository();

    public static PropertiesLoader properties = new PropertiesLoader();

    private final LoggedIn loggedIn = new LoggedIn();
    private final Reports reports = new Reports();

    public void Program(){

        System.out.println("Välkommen till skobutiken");
        while(true){
            System.out.println("""
            Vad vill du göra?
            1. Logga in
            2. Ny Kund
            3. Kolla Rapporter
            4. Avsluta programmet""");
            int answer = inputView.inputInt("Svara med Siffra", true);
            switch(answer){
                case 1 -> loggedIn.login();
                case 2 -> createUser();
                case 3 -> reports.login();
                case 4 -> System.exit(0);
                default -> System.out.println("Felaktig Siffra");
            }
        }
    }

    private void createUser() {
        String firstname = inputView.inputString("Förnamn?", true);
        String lastname = inputView.inputString("Efternamn?", true);
        Long ssn = inputView.inputLong("Personnummer? (10 siffror)", true);
        String adress = inputView.inputString("Gata du bor på?", true);
        int gatunummer = inputView.inputInt("gatunummer?", true);
        int postnummer = inputView.inputInt("Postnummer?", true);
        String postort = inputView.inputString("Postort?", true);
        while (true) {
        String password = inputView.inputString("Lösenord?", true);
        String password2 = inputView.inputString("Skriv lösenordet igen.", true);
        if (password2.equals(password)) {
            Customer customer = rep.insertNewCustomer(firstname, lastname, ssn, Encrypt.encryptSHA3(password), adress, gatunummer, postnummer, postort);
            if(customer != null){
                System.out.println("Skapande av kund gick bra\n");
            } else {
                System.out.println("Något gick fel, försök igen\n");
            }
            break;
        } else{
            System.out.println("Lösenordet var inte samma, försök igen");
        }
    }

    }
}
