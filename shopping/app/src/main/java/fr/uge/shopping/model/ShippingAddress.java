package fr.uge.shopping.model;

import fr.uge.shopping.R;
import fr.uge.shopping.annotations.Description;
import fr.uge.shopping.annotations.RegexValidator;

public class ShippingAddress {

    @Description(descriptionValue = R.string.addressName)
    @RegexValidator(regex = ". +")
    public String name;

    @Description(descriptionValue = R.string.addressStreet)
    @RegexValidator(regex = ". +")
    public String street;

    @Description(descriptionValue = R.string.addressCity)
    @RegexValidator(regex = ". +")
    public String city;

    @Description(descriptionValue = R.string.addressCountry)
    @RegexValidator(regex = ". +")
    public String country;

    public ShippingAddress() {}

    public ShippingAddress(String name, String street, String city, String country) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.country = country;
    }

}
