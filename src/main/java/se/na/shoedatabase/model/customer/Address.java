package se.na.shoedatabase.model.customer;

public class Address {

    private final String address;
    private final int addressNumber;
    private final String city;
    private final int zipcode;

    public Address(String address, int addressNumber, String city, int zipcode) {
        this.address = address;
        this.addressNumber = addressNumber;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public String getCity() {
        return city;
    }

    public int getZipcode() {
        return zipcode;
    }

    @Override
    public String toString() {
        return "Adress: " + address + " " + addressNumber + ", " + zipcode + " " + city;
    }
}
