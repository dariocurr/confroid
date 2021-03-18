package fr.uge.shopping;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.shopping.gui.ConfigurationAdapter;
import fr.uge.shopping.gui.RecyclerItemClickListener;
import fr.uge.shopping.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public final static String SHOPPING_INFO_NAME = "SHOPPING_INFO_NAME";

    private Button createConfigurationButton;
    private Button loadConfigurationButton;
    private Button addConfgirationButton;
    private PreferencesManager  preferencesManager;

    private RecyclerView recyclerView;
    private ConfigurationAdapter recyclerAdapter;

    private AlertDialog loadDialog;
    private Spinner selectVersion;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> versions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.preferencesManager = PreferencesManager.getPreferencesManager(this.getApplicationContext());

        this.loadConfigurationButton = findViewById(R.id.loadConfigurationButton);
        this.addConfgirationButton = findViewById(R.id.addConfigurationButton);
        this.addConfgirationButton.setEnabled(false);

        initRecyclerView();


        this.selectVersion = new Spinner(MainActivity.this);
        this.versions = new ArrayList<>();

        this.loadConfigurationButton.setOnClickListener(ev -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            this.spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, versions);
            selectVersion.setAdapter(this.spinnerAdapter);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            selectVersion.setLayoutParams(lp);

            alertDialogBuilder.setPositiveButton(getString(R.string.load), (dialog, which) -> {
                String version = selectVersion.getSelectedItem().toString();
                addConfgirationButton.setEnabled(true);
                preferencesManager.api().loadConfiguration(this.getApplicationContext(), version, o -> {
                    if( o instanceof ShoppingPreferences) {
                        updateRecyclerView((ShoppingPreferences) o);
                    }
                });
            });

            alertDialogBuilder.setNegativeButton(getString(R.string.back), (dialog, which) -> {
                dialog.cancel();
            });

            alertDialogBuilder.setOnCancelListener( dialog -> {
                this.versions.clear();
            });

            alertDialogBuilder.setOnDismissListener( dialog -> {
                this.versions.clear();
            });

            alertDialogBuilder.setView(selectVersion);
            alertDialogBuilder.setTitle(getString(R.string.selectVersion));
            alertDialogBuilder.setMessage(getString(R.string.selectVersionNumber));

            if(selectVersion.getParent() != null) {
                ((ViewGroup) selectVersion.getParent()).removeView(selectVersion);
            }
            loadDialog = alertDialogBuilder.create();
            loadDialog.show();

            this.preferencesManager.api().getConfigurationVersions(this.getApplicationContext(), o -> {
                if( o instanceof List) {
                    List<Integer> el = (List<Integer>) o;
                    if(!el.isEmpty()){
                        incrementVersion(el);
                    } else {
                        makeDialogForCreation();
                    }
                }
            });

        });

        this.addConfgirationButton.setOnClickListener( ev -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(getString(R.string.addPreference));
            alertDialog.setMessage(getString(R.string.addPreferenceName));

            final EditText prefName = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            prefName.setLayoutParams(lp);
            alertDialog.setView(prefName);

            alertDialog.setPositiveButton(getString(R.string.add), (dialog, which) -> {
                preferencesManager.getShoppingInfoMap().put(prefName.getText().toString(), new ShoppingInfo());
                syncApi();
                dialog.dismiss();
            });

            alertDialog.setNegativeButton(getString(R.string.back), (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog dialog = alertDialog.create();
            dialog.show();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

            prefName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!(s.toString().trim().length()==0)){
                        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.recyclerAdapter.getItemCount() > 0) {
            preferencesManager.api().loadConfiguration(this, "shoppingPreferences/stable", o -> {
                if(o instanceof ShoppingPreferences) {
                    updateRecyclerView((ShoppingPreferences) o);
                } else {
                    syncApi();
                }
            });
        }
    }

    private void initRecyclerView() {
        ArrayList<String> items = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new ConfigurationAdapter(this, items);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                launchEditActivity(recyclerAdapter.getItem(position));
            }

            @Override public void onLongItemClick(View view, int position) {
                launchRemoveDialog(recyclerAdapter.getItem(position));
            }
        }));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void updateRecyclerView(ShoppingPreferences prefs) {
        preferencesManager.setPreferences(prefs);
        ArrayList<String> items = new ArrayList<>(preferencesManager.getShoppingInfoMap().keySet());
        recyclerAdapter.setData(items);
        recyclerAdapter.notifyDataSetChanged();
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
                    syncApi();
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


    private void syncApi() {
        preferencesManager.api().saveConfiguration(this, "shoppingPreferences", preferencesManager.getPreferences(), "stable");
        preferencesManager.api().loadConfiguration(this, "latest", o -> {
            if( o instanceof ShoppingPreferences) {
                updateRecyclerView((ShoppingPreferences) o);
            }
        });
    }

    private void incrementVersion(List<Integer> list) {
        if (!list.isEmpty()) {
            Collections.reverse(list);
            list.forEach(e -> this.versions.add(String.valueOf(e)));
            this.versions.set(0, "latest");
            this.spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void makeDialogForCreation() {
        this.selectVersion.setEnabled(false);
        this.loadDialog.getButton(Dialog.BUTTON_POSITIVE).setText(getString(R.string.create));
        this.loadDialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener( ev -> {
            preferencesManager.init();
            preferencesManager.api().saveConfiguration(this.getApplicationContext(), "shoppingPreferences", preferencesManager.getPreferences(), "stable");
            this.loadDialog.dismiss();
            addConfgirationButton.setEnabled(true);
            preferencesManager.api().loadConfiguration(this.getApplicationContext(), "latest", o -> {
                if ( o instanceof ShoppingPreferences) {
                    updateRecyclerView((ShoppingPreferences) o);
                }
            });
        });

    }
}
