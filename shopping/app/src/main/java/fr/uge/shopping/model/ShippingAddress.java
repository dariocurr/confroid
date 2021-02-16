package fr.uge.shopping.model;

import android.os.Build;
import androidx.annotation.RequiresApi;
import fr.uge.shopping.R;
import fr.uge.shopping.api.annotations.Description;
import fr.uge.shopping.api.annotations.RegexValidator;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingAddress that = (ShippingAddress) o;
        return name.equals(that.name) &&
                street.equals(that.street) &&
                city.equals(that.city) &&
                country.equals(that.country);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(name, street, city, country);
    }

    @Override
    public String toString() {
        return "ShippingAddress{" +
                "name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
