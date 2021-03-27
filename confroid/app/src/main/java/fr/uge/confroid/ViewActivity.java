package fr.uge.confroid;

import android.app.AlertDialog;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.services.ConfigurationPusher;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ViewActivity extends AppCompatActivity {
    private Spinner dropdownMenu;
    private TextView contentText;
    private TextView datetext;
    private String oldContentText;
    private JSONObject configuration;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        configuration = ConfroidManager.loadAllVersionsJson(this, getIntent().getExtras().getString(MainActivity.EXTRA_CONFIGURATION_NAME));

        initContent();
        initVersionMenu();

        if(savedInstanceState != null) {
            if (savedInstanceState.getBoolean("EDIT_STATE")) {
                contentText.setEnabled(true);
                dropdownMenu.setEnabled(false);
                contentText.setText(savedInstanceState.getString("CONTENT_TEXT"));
                oldContentText = savedInstanceState.getString("OLD_CONTENT_TEXT");
            }
        } else {
            updateContent();
        }
    }

    /**
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("EDIT_STATE", contentText.isEnabled());
        outState.putString("CONTENT_TEXT", contentText.getText().toString());
        outState.putString("OLD_CONTENT_TEXT", oldContentText);
    }

    private void initVersionMenu() {
        dropdownMenu = findViewById(R.id.versionList);

        try {
            JSONObject content = configuration.getJSONObject("configurations");
            ArrayList<String> items = new ArrayList<>();
            content.keys().forEachRemaining(items::add);
            Collections.reverse(items);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdownMenu.setAdapter(adapter);

            dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        datetext.setText(
                                getString(R.string.created_on)+" "+content
                                        .getJSONObject(parent.getItemAtPosition(position).toString())
                                        .getString("date"));
                        if (!contentText.isEnabled()) {
                            updateContent();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initContent() {
        contentText = findViewById(R.id.contentView);
        datetext = findViewById(R.id.dateView);
    }

    private void updateContent() {
        try {
            contentText.setText(
                    configuration.getJSONObject("configurations")
                            .getJSONObject(String.valueOf(Integer.parseInt(dropdownMenu.getSelectedItem().toString())))
                            .getJSONObject("content").toString(2)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleConfigSubmission() {
        contentText.setEnabled(false);

        try {
            JSONObject upload = new JSONObject();
            upload.put("name", getIntent().getExtras().getString(MainActivity.EXTRA_CONFIGURATION_NAME));
            upload.put("version", ConfigurationPusher.getNextVersionNumber(getIntent().getExtras().getString(MainActivity.EXTRA_CONFIGURATION_NAME).replaceAll("_", "\\."), getApplicationContext()));

            upload.put("content", new JSONObject(contentText.getText().toString()));


            if(ConfroidManager.saveConfiguration(this, upload)) {
                Toast.makeText(this, this.getString(R.string.done)+"!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, this.getString(R.string.submit_error)+".", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        finish();
        startActivity(getIntent());
    }

    /**
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_item:
                contentText.setText(oldContentText);
                contentText.setEnabled(false);
                dropdownMenu.setEnabled(true);
                invalidateOptionsMenu();
                return true;

            case R.id.edit_item:
                AlertDialog alertDialog = new AlertDialog.Builder(ViewActivity.this).create();
                alertDialog.setTitle(getString(R.string.attention));
                alertDialog.setMessage(getString(R.string.editMessage));
                alertDialog.setIcon(R.drawable.ic_baseline_warning_24);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
                oldContentText = contentText.getText().toString();
                contentText.setEnabled(true);
                dropdownMenu.setEnabled(false);
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

    /**
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.configuration_menu, menu);
        return true;
    }

    /**
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        setMenuItemEnabled(menu.findItem(R.id.back_item), contentText.isEnabled());
        setMenuItemEnabled(menu.findItem(R.id.save_item), contentText.isEnabled());
        setMenuItemEnabled(menu.findItem(R.id.edit_item), !contentText.isEnabled());


        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * @param item
     * @param state
     */
    private void setMenuItemEnabled(MenuItem item, boolean state) {
        item.setVisible(state);
        item.setEnabled(state);
        item.setCheckable(state);
    }
}