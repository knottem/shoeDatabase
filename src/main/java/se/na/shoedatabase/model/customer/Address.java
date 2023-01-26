package se.na.shoedatabase.model.customer;

public class Address {

    private String address;
    private int addressNumber;
    private String city;
    private int zipcode;

    public Address(String address, int addressNumber, String city, int zipcode) {
        this.address = address;
        this.addressNumber = addressNumber;
        this.city = city;
        this.zipcode = zipcode;
    }
    public Address(){}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(int addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }
}
