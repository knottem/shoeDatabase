package se.na.shoedatabase.model;

public class Customer {

    int id;
    String firstname;
    String lastname;
    Long ssn;
    String pass;
    String address;
    int addressNumber;
    String city;
    int zipcode;

    public Customer(int id, String firstname, String lastname, Long ssn, String pass, String address, int addressNumber, String city, int zipcode) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.pass = pass;
        this.address = address;
        this.addressNumber = addressNumber;
        this.city = city;
        this.zipcode = zipcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getSsn() {
        return ssn;
    }

    public void setSsn(Long ssn) {
        this.ssn = ssn;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", ssn=" + ssn +
                ", pass='" + pass + '\'' +
                ", address='" + address + '\'' +
                ", addressNumber=" + addressNumber +
                ", city='" + city + '\'' +
                ", zipcode=" + zipcode +
                '}';
    }
}
