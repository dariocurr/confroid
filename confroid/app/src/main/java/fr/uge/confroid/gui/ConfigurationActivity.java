package fr.uge.confroid.gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.R;
import fr.uge.confroid.ConfroidManager;

import java.util.ArrayList;

public class ConfigurationActivity extends AppCompatActivity {
    private Spinner dropdownMenu;
    private TextView contentText;
    /*private Button editButton;
    private Button backButton;*/
    private String oldContentText;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        bundle = ConfroidManager.loadConfiguration(this.getApplicationContext(), getIntent().getExtras().getString("EXTRA_TEST_STRING"), 1);
        Log.i("bundle", bundle.toString());

        initContent();
        initVersionMenu();

        //initButtons();
    }

    private void initVersionMenu() {
        dropdownMenu = findViewById(R.id.versionList);

        ArrayList<String> items = new ArrayList<>(bundle.keySet());


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownMenu.setAdapter(adapter);

        dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String version = parent.getItemAtPosition(position).toString();
                //TODO
                contentText.setText(bundle.get(version).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing ??
            }
        });
    }

    private void initContent() {
        contentText = findViewById(R.id.contentView);

        //Bundle bundle = intent.getBundleExtra("content");

        //contentText.setText(bundle.get(dropdownMenu.getSelectedItem().toString()).toString());
    }

    /*public void setProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

    private void handleConfigSubmission() {
        contentText.setEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView progressText = dialog.findViewById(R.id.progress_text);
        progressText.setText(R.string.submission);

        //TODO PUSH CONF
        if(true) {
            Toast.makeText(this, this.getString(R.string.done)+"!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, this.getString(R.string.submit_error)+".", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();

    }

    /*private void initButtons() {
        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            contentText.setEnabled(true);
            //TODO save/cancel button
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_item:
                contentText.setText(oldContentText);
                //TODO confirm pop up?
                contentText.setEnabled(false);
                invalidateOptionsMenu();
                return true;

            case R.id.edit_item:
                oldContentText = contentText.getText().toString();
                contentText.setEnabled(true);
                invalidateOptionsMenu();
                return true;

            case R.id.save_item:
                handleConfigSubmission();
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.configuration_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        setMenuItemEnabled(menu.findItem(R.id.back_item), contentText.isEnabled());
        setMenuItemEnabled(menu.findItem(R.id.save_item), contentText.isEnabled());
        setMenuItemEnabled(menu.findItem(R.id.edit_item), !contentText.isEnabled());

        return super.onPrepareOptionsMenu(menu);
    }

    private void setMenuItemEnabled(MenuItem item, boolean state) {
        item.setVisible(state);
        item.setEnabled(state);
        item.setCheckable(state);
    }
}