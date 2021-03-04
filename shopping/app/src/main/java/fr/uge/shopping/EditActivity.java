package fr.uge.shopping;

import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.shopping.R;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;
import fr.uge.shopping.model.ShoppingInfo;
import fr.uge.shopping.model.ShoppingPreferences;

public class EditActivity extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    private EditText shoppingInfoEditName;
    private TextView addressEditTextView;
    private TextView billingEditTextView;
    private ImageButton editAddressButton;
    private ImageButton editBillingButton;
    private Button saveEditButton;
    private CheckBox favoriteEditCheckBox;

    private String infoName;
    private BillingDetails billing;
    private ShippingAddress address;
    private boolean favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.preferencesManager = PreferencesManager.getPreferencesManager(this.getApplicationContext());

        this.infoName = getIntent().getStringExtra(MainActivity.SHOPPING_INFO_NAME);
        this.billing = this.preferencesManager.getShoppingInfo(infoName).billing;
        this.address = this.preferencesManager.getShoppingInfo(infoName).address;
        this.favorite = this.preferencesManager.getShoppingInfo(infoName).favorite;
        //Log.i("edit123", preferencesManager.getShoppingInfo(name).toString());

        this.shoppingInfoEditName = findViewById(R.id.shoppingInfoEditName);
        this.shoppingInfoEditName.setText(infoName);

        this.addressEditTextView = findViewById(R.id.addressEditTextView);
        this.addressEditTextView.setText(this.address.toString());

        this.billingEditTextView = findViewById(R.id.billingEditTextView);
        this.billingEditTextView.setText(this.billing.toString());

        this.favoriteEditCheckBox = findViewById(R.id.favoriteEditCheckBox);
        this.favoriteEditCheckBox.setChecked(this.favorite);

        this.editAddressButton = findViewById(R.id.editAddressButton);
        this.editAddressButton.setOnClickListener( ev -> {
            //TODO launch address edit fragment
        });

        this.editBillingButton = findViewById(R.id.editBillingButton);
        this.editBillingButton.setOnClickListener( ev -> {
            //TODO launch billing edit fragment
        });

        this.saveEditButton = findViewById(R.id.saveEditButton);
        this.saveEditButton.setOnClickListener( ev -> {

            ShippingAddress address = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
            BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);

            this.preferencesManager.getShoppingInfoMap().put(
                    this.shoppingInfoEditName.getText().toString(),
                    new ShoppingInfo(address, billing, favoriteEditCheckBox.isChecked())
            );
            //TODO PUSH
        });


    }
}