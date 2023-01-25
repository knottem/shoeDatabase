package se.na.shoedatabase.model;

public class Shoe {

    int id;
    int price;
    String brand;
    String color;
    int size;
    int quantity;

    public Shoe(int id, int price, String brand, String color, int size, int quantity) {
        this.id = id;
        this.price = price;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Märke: " + brand + " Färg: " + color + " Storlek: " + size + " Pris: " + price;
    }

    public String toStringWithQuantity(){
        return "Märke: " + brand + " Färg: " + color + " Storlek: " + size + " Pris: " + price + " Antal: " + quantity;
    }
}
