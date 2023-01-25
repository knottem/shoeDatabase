package se.na.shoedatabase.model;

import java.util.ArrayList;

public class Orders {

    int id;
    ArrayList<Shoe> shoes = new ArrayList<>();
    Customer customer = new Customer();


    public Orders(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Shoe> getShoes() {
        return shoes;
    }

    public void setShoes(ArrayList<Shoe> shoes) {
        this.shoes = shoes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
