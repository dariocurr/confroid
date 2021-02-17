package fr.uge.shopping;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import fr.uge.confroidutils.ConfroidUtils;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;
import fr.uge.shopping.model.ShoppingInfo;
import fr.uge.shopping.model.ShoppingPreferences;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShoppingPreferences prefs = new ShoppingPreferences();
        ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
        ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
        BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
        prefs.shoppingInfo.put("home", new ShoppingInfo(address1, billing, true));
        prefs.shoppingInfo.put("work", new ShoppingInfo(address2, billing, false));
        ConfroidUtils confroidUtils = new ConfroidUtils(this.getApplicationContext());
        confroidUtils.saveConfiguration(this.getApplicationContext(), "shoppingPreferences", prefs, "stable");
        confroidUtils.loadConfiguration(this.getApplicationContext(), "shoppingPreferences/stable", o -> Log.e("CHECK", prefs.equals(o) + ""));

    }

}
