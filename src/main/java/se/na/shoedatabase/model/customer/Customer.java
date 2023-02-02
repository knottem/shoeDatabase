package se.na.shoedatabase.model.customer;

public class Customer {

    private final int id;
    private final String firstname;
    private final String lastname;
    private final Long ssn;
    private final String pass;
    private final Address address;

    public Customer(int id, String firstname, String lastname, Long ssn, String pass, String address, int addressNumber, String city, int zipcode) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.pass = pass;
        this.address = new Address(address, addressNumber, city, zipcode);
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Long getSsn() {
        return ssn;
    }

    public Address getAddress() {
        return address;
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
