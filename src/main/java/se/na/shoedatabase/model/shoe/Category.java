package se.na.shoedatabase.model.shoe;

public class Category {

    private String categoryname;

    public Category(String categoryname) {
        this.categoryname = categoryname;
    }

    public Category() {
    }
    public String getCategoryname() {
        return categoryname;
    }
}
