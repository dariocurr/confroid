package fr.uge.shopping;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import fr.uge.confroidutils.ConfroidUtils;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;
import fr.uge.shopping.model.ShoppingInfo;
import fr.uge.shopping.model.ShoppingPreferences;


public class MainActivity extends AppCompatActivity {

    private ConfroidUtils confroidUtils;
    private Button saveConfigurationButton;
    private Button loadConfigurationButton;
    private Button loadVersionsButton;
    private Button updateTagButton;
    private Button editConfigurationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.confroidUtils = new ConfroidUtils(this.getApplicationContext());

        this.saveConfigurationButton = findViewById(R.id.saveConfigurationButton);
        this.loadConfigurationButton = findViewById(R.id.loadConfigurationButton);
        this.loadVersionsButton = findViewById(R.id.loadVersionsButton);
        this.updateTagButton = findViewById(R.id.updateTagButton);
        this.editConfigurationButton = findViewById(R.id.editConfigurationButton);

        this.saveConfigurationButton.setOnClickListener(ev -> {
            ShoppingPreferences prefs = new ShoppingPreferences();
            ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
            ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
            BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
            prefs.shoppingInfo.put("home", new ShoppingInfo(address1, billing, true));
            prefs.shoppingInfo.put("work", new ShoppingInfo(address2, billing, false));
            confroidUtils.saveConfiguration(this.getApplicationContext(), "shoppingPreferences", prefs, "stable");
        });

        this.loadConfigurationButton.setOnClickListener(ev -> {
            confroidUtils.loadConfiguration(this.getApplicationContext(), "shoppingPreferences/stable", o -> Log.e("CHECK", o + ""));
        });

        this.loadVersionsButton.setOnClickListener(ev -> {});
        this.updateTagButton.setOnClickListener(ev -> {});
        this.editConfigurationButton.setOnClickListener(ev -> {});

    }

}
