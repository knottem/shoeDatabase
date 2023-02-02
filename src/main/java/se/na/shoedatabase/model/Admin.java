package se.na.shoedatabase.model;

public class Admin {

    private final int id;
    private final String name;
    private final String password;

    public Admin(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }

}
