package se.na.shoedatabase.model;

import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.shoe.Shoe;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Orders {

    private final int id;
    private final ArrayList<Shoe> shoes = new ArrayList<>();
    private Customer customer;
    private final Timestamp timestamp;

    public Orders(int id, Timestamp timestamp) {
        this.id = id;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
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
