package fr.uge.shopping.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.function.Predicate;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CreditCardChecker implements Predicate<String> {

    @Override
    public boolean test(String creditCardNumber) {
        int n = creditCardNumber.length() - 1;
        int sum = Integer.parseInt(creditCardNumber.substring(n));
        for (int i = n - 1; i >= 0; i--) {
            int number = Integer.parseInt(creditCardNumber.substring(i, i + 1));
            if ((n - i) % 2 == 1) {
                number *= 2;
                if (number > 9) {
                    number -= 9;
                }
            }
            sum += number;
        }
        return (sum % 10 == 0);
    }

}
