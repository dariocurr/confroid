package fr.uge.client.model;

public class ShippingAddress {

    public String name;
    public String street;
    public String city;
    public String country;

    public ShippingAddress() {}

    public ShippingAddress(String name, String street, String city, String country) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.country = country;
    }

}
