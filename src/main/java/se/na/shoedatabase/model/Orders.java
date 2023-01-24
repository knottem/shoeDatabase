package se.na.shoedatabase.model;

import java.util.ArrayList;

public class Orders {

    int id;
    ArrayList<Shoe> shoes = new ArrayList<>();


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
}
