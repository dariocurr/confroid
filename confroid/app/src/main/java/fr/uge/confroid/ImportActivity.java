package fr.uge.confroid;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.R;
import fr.uge.confroid.gui.ImportAdapter;
import fr.uge.confroid.gui.ImportItem;
import fr.uge.confroid.services.ConfigurationPusher;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ImportActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImportAdapter importAdapter;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        content = getIntent().getExtras().getString("CONFIGURATIONS");

        ArrayList<ImportItem> items = new ArrayList<>();

        try {
            JSONObject configurations = new JSONObject(content);
            for (Iterator<String> it = configurations.keys(); it.hasNext(); ) {
                String key = it.next();
                JSONObject jsonObject = configurations.getJSONObject(key);
                items.add(new ImportItem(jsonObject.getString("name"), jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.import_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        importAdapter = new ImportAdapter(this, items);
        recyclerView.setAdapter(importAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (importAdapter.getSelectedItemCount() > 0) {
            menu.findItem(R.id.import_action).setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_action:
                if(!importConfigurations())
                    Toast.makeText(this, getString(R.string.import_error), Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean importConfigurations() {
        ArrayList<ImportItem> selectedItems = importAdapter.getSelectedItems();
        try {
            for(ImportItem item : selectedItems) {
                ConfigurationPusher.resetVersionNumber(item.getJson().getString("name"), getApplicationContext());
                Bundle contentBundle = ConfroidManagerUtils.getAllVersionsFromJsonToBundle(item.getJson());

                for(String keyBundle : contentBundle.keySet()){
                    Bundle bundle = new Bundle();
                    bundle.putString("name", item.getJson().getString("name"));
                    bundle.putString("token", item.getJson().getString("token"));
                    bundle.putInt("version", ConfigurationPusher.getNextVersionNumber(item.getJson().getString("name"), getApplicationContext()));

                    bundle.putBundle("content", contentBundle.getBundle(keyBundle).getBundle("content"));

                    ConfroidManager.saveConfiguration(this.getApplicationContext(), bundle);
                }
            }
            finish();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}