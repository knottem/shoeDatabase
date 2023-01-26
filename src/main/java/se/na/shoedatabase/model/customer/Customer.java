package se.na.shoedatabase.model.customer;

public class Customer {

    int id;
    String firstname;
    String lastname;
    Long ssn;
    String pass;
    Address address = new Address();

    public Customer(int id, String firstname, String lastname, Long ssn, String pass, String address, int addressNumber, String city, int zipcode) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.pass = pass;
        this.address.setAddressNumber(addressNumber);
        this.address.setAddress(address);
        this.address.setCity(city);
        this.address.setZipcode(zipcode);
    }
    public Customer(){}

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", ssn=" + ssn +
                ", pass='" + pass + '\'' +
                ", address='" + address.getAddress() + '\'' +
                ", addressNumber=" + address.getAddressNumber() +
                ", city='" + address.getCity() + '\'' +
                ", zipcode=" + address.getZipcode() +
                '}';
    }
}
