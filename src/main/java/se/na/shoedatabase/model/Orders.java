package se.na.shoedatabase.model;

import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;

import java.util.ArrayList;

public class Orders {

    private final int id;
    private final ArrayList<Shoe> shoes = new ArrayList<>();
    private Customer customer;
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", shoes=" + shoes +
                ", customer=" + customer +
                '}';
    }
}
