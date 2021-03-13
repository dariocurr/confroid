package fr.uge.shopping.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;

public class ShoppingInfo {

    public ShippingAddress address;
    public BillingDetails billing;
    public boolean favorite;

    public ShoppingInfo() {
        this.address = new ShippingAddress();
        this.billing = new BillingDetails();
        this.favorite = false;
    }

    public ShoppingInfo(ShippingAddress address, BillingDetails billing, boolean favorite) {
        this.address = address;
        this.billing = billing;
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingInfo that = (ShoppingInfo) o;
        return favorite == that.favorite &&
                address.equals(that.address) &&
                billing.equals(that.billing);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(address, billing, favorite);
    }

    @Override
    public String toString() {
        return "ShoppingInfo{" +
                "address=" + address +
                ", billing=" + billing +
                ", favorite=" + favorite +
                '}';
    }
}
