package se.na.shoedatabase.model.shoe;

import java.util.ArrayList;

public class Shoe {

    private final int id;
    private final int price;
    private final int size;
    private int quantity;
    private final Brand brand = new Brand();
    private final Color color = new Color();
    ArrayList<Category> categories = new ArrayList<>();


    public Shoe(int id, int price, String brand, String color, int size, int quantity) {
        this.id = id;
        this.price = price;
        this.brand.setBrand(brand);
        this.color.setColor(color);
        this.size = size;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public int getPrice() {
        return price;
    }

    public String getBrand() {
        return brand.getBrand();
    }

    public String getColor() {
        return color.getColor();
    }

    public int getSize() {
        return size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Märke: " + brand.getBrand() + " Färg: " + color.getColor() + " Storlek: " + size + " Kategori: " + getCategoriesNames() + " Pris: " + price;
    }

    public String toStringWithQuantity(){
        return "Märke: " + brand.getBrand() + " Färg: " + color.getColor() + " Storlek: " + size + " Kategori: " + getCategoriesNames() + " Pris: " + price + " Antal: " + quantity;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
    public StringBuilder getCategoriesNames(){
        StringBuilder cat = new StringBuilder();
        for(Category category : categories){
            cat.append(category.getCategoryname()).append(", ");
        }
        cat.setLength((cat.length()-2));
        return cat;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
