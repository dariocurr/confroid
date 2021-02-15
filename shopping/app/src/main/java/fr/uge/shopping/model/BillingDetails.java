package fr.uge.shopping.model;

import fr.uge.shopping.R;
import fr.uge.shopping.annotations.*;

public class BillingDetails {

    @Description(descriptionValue = R.string.cardHolder)
    @RegexValidator(regex = ". +")
    public String cardHolder;

    @Description (descriptionValue = R.string.cardNumber)
    @RegexValidator (regex = "[0-9] {16}")
    @ClassValidator(clazz = CreditCardChecker.class)
    public String cardNumber;

    @Description (descriptionValue = R.string.expirationMonth)
    @RangeValidator(lowerBound = 1, upperBound = 12)
    public int expirationMonth;

    @Description (descriptionValue = R.string.expirationYear)
    @RangeValidator (lowerBound = 2020, upperBound = 2040)
    public int expirationYear;

    @Description (descriptionValue = R.string.cryptogram)
    @RangeValidator (lowerBound = 0, upperBound = 999)
    public int cryptogram;

    public BillingDetails() {
    }

    public BillingDetails(String cardHolder, String cardNumber, int expirationMonth, int expirationYear, int cryptogram) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cryptogram = cryptogram;
    }

}
