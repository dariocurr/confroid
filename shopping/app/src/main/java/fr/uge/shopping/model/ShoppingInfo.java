package fr.uge.shopping.model;

public class ShoppingInfo {

    public ShippingAddress address;
    public BillingDetail billing;
    public boolean favorite;

    public ShoppingInfo() {}

    public ShoppingInfo(ShippingAddress address, BillingDetail billing, boolean favorite) {
        this.address = address;
        this.billing = billing;
        this.favorite = favorite;
    }

}
