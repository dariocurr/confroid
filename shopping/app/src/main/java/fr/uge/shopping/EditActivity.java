package fr.uge.shopping;

import android.app.FragmentManager;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import fr.uge.shopping.R;
import fr.uge.shopping.gui.EditAddressFragment;
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            EditAddressFragment fragment = new EditAddressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("name", this.infoName);
            fragment.setArguments(bundle);
            ft.replace(R.id.fragment_frame, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        this.editBillingButton = findViewById(R.id.editBillingButton);
        this.editBillingButton.setOnClickListener( ev -> {
            //TODO launch billing edit fragment
        });

        this.saveEditButton = findViewById(R.id.saveEditButton);
        this.saveEditButton.setOnClickListener( ev -> {

            this.favorite = favoriteEditCheckBox.isChecked();

            Log.i("push123", "BEFORE:\n"+this.preferencesManager.getShoppingInfoMap().toString());

            if(!this.shoppingInfoEditName.getText().toString().equals(this.infoName)) {
                this.preferencesManager.getShoppingInfoMap().remove(this.infoName);
            }

            this.preferencesManager.getShoppingInfoMap().put(
                    this.shoppingInfoEditName.getText().toString(),
                    new ShoppingInfo(this.address, this.billing, this.favorite)
            );

            Log.i("push123", "AFTER:\n"+this.preferencesManager.getShoppingInfoMap().toString());

            //TODO PUSH VIA API
            this.preferencesManager.api().saveConfiguration(
                    this.getApplicationContext(),
                    "shoppingPreferences",
                    this.preferencesManager.getPreferences(),
                    "stable"
            );
            finish();
        });


    }

    public void updateAddress(ShippingAddress address) {
        this.address = address;
        this.addressEditTextView.setText(address.toString());
    }

}