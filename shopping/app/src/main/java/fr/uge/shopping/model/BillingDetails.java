package fr.uge.shopping.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import fr.uge.confroidutils.annotations.*;
import fr.uge.shopping.R;

import java.io.Serializable;
import java.util.Objects;

public class BillingDetails implements Serializable {

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
        this.cardHolder = "";
        this.cardNumber = "";
    }

    public BillingDetails(String cardHolder, String cardNumber, int expirationMonth, int expirationYear, int cryptogram) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.cryptogram = cryptogram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingDetails that = (BillingDetails) o;
        return expirationMonth == that.expirationMonth &&
                expirationYear == that.expirationYear &&
                cryptogram == that.cryptogram &&
                cardHolder.equals(that.cardHolder) &&
                cardNumber.equals(that.cardNumber);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(cardHolder, cardNumber, expirationMonth, expirationYear, cryptogram);
    }

    @Override
    public String toString() {
        return "N° "+cardNumber+" - "+expirationMonth+"\\"+expirationYear+"\n"+cardHolder+"\n"+cryptogram;
    }
}
