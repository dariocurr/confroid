package fr.uge.shopping;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroidutils.ConfroidUtils;
import fr.uge.shopping.gui.ConfigurationAdapter;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;
import fr.uge.shopping.model.ShoppingInfo;
import fr.uge.shopping.model.ShoppingPreferences;
import org.json.JSONException;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ConfroidUtils confroidUtils;
    private Button saveConfigurationButton;
    /*private Button loadConfigurationButton;
    private Button loadVersionsButton;
    private Button updateTagButton;
    private Button editConfigurationButton;*/

    private ConfigurationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.confroidUtils = new ConfroidUtils(this.getApplicationContext());



        this.saveConfigurationButton = findViewById(R.id.saveConfigurationButton);
        /*this.loadConfigurationButton = findViewById(R.id.loadConfigurationButton);
        this.loadVersionsButton = findViewById(R.id.loadVersionsButton);
        this.updateTagButton = findViewById(R.id.updateTagButton);
        this.editConfigurationButton = findViewById(R.id.editConfigurationButton);*/

        this.saveConfigurationButton.setOnClickListener(ev -> {
            ShoppingPreferences prefs = new ShoppingPreferences();
            ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
            ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
            BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
            prefs.shoppingInfo.put("home", new ShoppingInfo(address1, billing, true));
            prefs.shoppingInfo.put("work", new ShoppingInfo(address2, billing, false));
            confroidUtils.saveConfiguration(this.getApplicationContext(), "shoppingPreferences", prefs, "stable");
        });
        /*
        this.loadConfigurationButton.setOnClickListener(ev -> {
            confroidUtils.loadConfiguration(this.getApplicationContext(), "shoppingPreferences/stable", o -> Log.e("CHECK", o + ""));
        });

        this.loadVersionsButton.setOnClickListener(ev -> {
            confroidUtils.getConfigurationVersions(this.getApplicationContext(), "fr.uge.shopping", o -> Log.e("CHECK", o + ""));
        });
        this.updateTagButton.setOnClickListener(ev -> {});
        this.editConfigurationButton.setOnClickListener(ev -> {});*/

        //-----------------------------------------EMA---------------------------------------
        // qua funziona, ma non al primo colpo. Devo "refreshare" l'attività tipo ruotando lo schermo.
        // Bisogna comunque risolvere il problema che al primo colpo non va ma è secondario al momento
        confroidUtils.loadConfiguration(this.getApplicationContext(), "shoppingPreferences/stable", o -> Log.e("load123", o + ""));
        try {
            initRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerView() throws JSONException {
        ShoppingPreferences p = new ShoppingPreferences();

        confroidUtils.loadConfiguration(this.getApplicationContext(),
                "shoppingPreferences/stable",
                o ->  p.shoppingInfo = ((ShoppingPreferences) o).shoppingInfo); //<-- non va questa assegnazione

        //Qua stampa sempre vuoto
        Log.i("load123", p.toString());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new ConfigurationAdapter(this, configurations);
        //adapter.setClickListener(this);
        //recyclerView.setAdapter(adapter);
    }

}
