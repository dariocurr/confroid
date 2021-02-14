package fr.uge.client.model;

public class BillingDetail {

    public String cardHolder;
    public String cardNumber;
    public int expirationMonth;
    public int expirationYear;
    public int cryptogram;

    public BillingDetail() {}

    public BillingDetail(String cardHolder, String cardNumber, int expirationMonth, int expirationYear, int cryptogram) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cryptogram = cryptogram;
    }

}
