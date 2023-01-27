package se.na.shoedatabase.model;

import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;

import java.util.ArrayList;

public class Orders {

    private final int id;
    private final ArrayList<Shoe> shoes = new ArrayList<>();
    private final Customer customer = new Customer();
    public Orders(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public ArrayList<Shoe> getShoes() {
        return shoes;
    }
    public Customer getCustomer() {
        return customer;
    }

}
