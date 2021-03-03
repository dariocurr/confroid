package fr.uge.shopping;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroidutils.ConfroidUtils;
import fr.uge.shopping.gui.ConfigurationAdapter;
import fr.uge.shopping.gui.ConfigurationItem;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;
import fr.uge.shopping.model.ShoppingInfo;
import fr.uge.shopping.model.ShoppingPreferences;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;


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
            /*ShoppingPreferences prefs = new ShoppingPreferences();
            ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
            ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
            BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
            prefs.shoppingInfo.put("home", new ShoppingInfo(address1, billing, true));
            prefs.shoppingInfo.put("work", new ShoppingInfo(address2, billing, false));
            confroidUtils.saveConfiguration(this.getApplicationContext(), "shoppingPreferences", prefs, "stable");*/
            confroidUtils.loadConfiguration(this.getApplicationContext(), "shoppingPreferences/stable", o -> initRecyclerView((ShoppingPreferences) o));
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


    }

    private void initRecyclerView(ShoppingPreferences prefs) {

        Log.i("load123", prefs.toString());

        ArrayList<ConfigurationItem> items = new ArrayList<>();
         prefs.shoppingInfo.keySet().forEach( key -> {
             items.add(new ConfigurationItem(key, prefs.shoppingInfo.get(key)));
             Log.i("load123", "Added item "+key);
         });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConfigurationAdapter(this, items);
        recyclerView.setAdapter(adapter);

        Log.i("load123", "number of elements "+adapter.getItemCount());
    }

}
