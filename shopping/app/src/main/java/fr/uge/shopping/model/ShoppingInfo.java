package fr.uge.shopping.model;

public class ShoppingInfo {

    public ShippingAddress address;
    public BillingDetails billing;
    public boolean favorite;

    public ShoppingInfo() {}

    public ShoppingInfo(ShippingAddress address, BillingDetails billing, boolean favorite) {
        this.address = address;
        this.billing = billing;
        this.favorite = favorite;
    }

}
