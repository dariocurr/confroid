package fr.uge.shopping.model;

import fr.uge.shopping.R;

public class BillingDetail {

    @Description(R.string.cardHolder)
    @RegexValidator (". +")
    public String cardHolder;

    @Description (R.string.cardNumber)
    @RegexValidator ("[0-9] {16}")
    @ClassValidator (CreditCardChecker.class)
    public String cardNumber;

    @Description (R.string.expirationMonth)
    @RangeValidator (1, 12)
    public int expirationMonth;

    @Description (R.string.expirationYear)
    @RangeValidator (2020, 2040)
    public int expirationYear;

    @Description (R.string.cryptogram)
    @RangeValidator (0, 999)
    public int cryptogram;

    // TODO: constructor
}
