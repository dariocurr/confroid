package fr.uge.shopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroidutils.ConfroidUtils;
import fr.uge.shopping.gui.ConfigurationAdapter;
import fr.uge.shopping.gui.ConfigurationItem;
import fr.uge.shopping.gui.RecyclerItemClickListener;
import fr.uge.shopping.model.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public final static String SHOPPING_INFO_NAME = "SHOPPING_INFO_NAME";
    //private ConfroidUtils confroidUtils;
    private Button saveConfigurationButton;
    private Button loadConfigurationButton;
    private Button addConfgirationButton;
    private PreferencesManager  preferencesManager;
    // private ShoppingPreferences shoppingPreferences;
    /*private Button loadVersionsButton;
    private Button updateTagButton;
    private Button editConfigurationButton;*/

    private RecyclerView recyclerView;
    private ConfigurationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.confroidUtils = new ConfroidUtils(this.getApplicationContext());
        this.preferencesManager = PreferencesManager.getPreferencesManager(this.getApplicationContext());

        this.saveConfigurationButton = findViewById(R.id.saveConfigurationButton);
        this.loadConfigurationButton = findViewById(R.id.loadConfigurationButton);
        this.addConfgirationButton = findViewById(R.id.addConfigurationButton);
        /*this.loadVersionsButton = findViewById(R.id.loadVersionsButton);
        this.updateTagButton = findViewById(R.id.updateTagButton);
        this.editConfigurationButton = findViewById(R.id.editConfigurationButton);*/
        initRecyclerView();

        this.saveConfigurationButton.setOnClickListener(ev -> {
            /*ShoppingPreferences prefs = new ShoppingPreferences();
            ShippingAddress address1 = new ShippingAddress("Bugdroid", "Bd Descartes", "Champs-sur-Marne", "France");
            ShippingAddress address2 = new ShippingAddress("Bugdroid", "Rue des tartes au nougat", "Lollipop City", "Oreo Country");
            BillingDetails billing = new BillingDetails("Bugdroid", "123456789", 12, 2021, 123);
            prefs.shoppingInfo.put("home", new ShoppingInfo(address1, billing, true));
            prefs.shoppingInfo.put("work", new ShoppingInfo(address2, billing, false));*/
            preferencesManager.init();
            preferencesManager.api().saveConfiguration(this.getApplicationContext(), "shoppingPreferences", preferencesManager.getPreferences(), "stable");
            //
        });

        this.loadConfigurationButton.setOnClickListener(ev -> {
            preferencesManager.api().loadConfiguration(this.getApplicationContext(), "shoppingPreferences/stable", o -> updateRecyclerView((ShoppingPreferences) o));
        });

        this.addConfgirationButton.setOnClickListener( ev -> {

        });
/*
        this.loadVersionsButton.setOnClickListener(ev -> {
            confroidUtils.getConfigurationVersions(this.getApplicationContext(), "fr.uge.shopping", o -> Log.e("CHECK", o + ""));
        });
        this.updateTagButton.setOnClickListener(ev -> {});
        this.editConfigurationButton.setOnClickListener(ev -> {});*/


    }

    private void initRecyclerView() {
        ArrayList<ConfigurationItem> items = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConfigurationAdapter(this, items);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                launchEditActivity(adapter.getItem(position).getName());
            }

            @Override public void onLongItemClick(View view, int position) {
                launchRemoveDialog(adapter.getItem(position).getName());
            }
        }));
        recyclerView.setAdapter(adapter);
    }

    private void updateRecyclerView(ShoppingPreferences prefs) {
        ArrayList<ConfigurationItem> items = new ArrayList<>();
        preferencesManager.setPreferences(prefs);
        preferencesManager.getShoppingInfoMap().keySet().forEach(key -> items.add(new ConfigurationItem(key, prefs.shoppingInfo.get(key))));
        adapter.setData(items);
        adapter.notifyDataSetChanged();
    }

    private void launchEditActivity(String key) {
        Intent intent = new Intent(getBaseContext(), EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SHOPPING_INFO_NAME, key);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void launchRemoveDialog(String key) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    preferencesManager.removeShoppingInfo(key);

                    preferencesManager.api().saveConfiguration(this, "shoppingPreferences", preferencesManager.getPreferences(), "stable");
                    preferencesManager.api().loadConfiguration(this, "shoppingPreferences/stable", o -> updateRecyclerView((ShoppingPreferences) o));
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.deleteConfirm)+" \""+key+"\"")
                .setPositiveButton(this.getString(R.string.yes), dialogClickListener)
                .setNegativeButton(this.getString(R.string.no), dialogClickListener)
                .show();
    }
}
