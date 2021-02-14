package fr.uge.shopping.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.function.Predicate;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CreditCardChecker implements Predicate<String> {

    @Override
    public boolean test(String cardNumber) {
        return true;
    }

}
