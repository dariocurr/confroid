package fr.uge.confroid;

import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        configuration = ConfroidManager.loadAllVersionsJson(this, getIntent().getExtras().getString(MainActivity.EXTRA_CONFIGURATION_NAME));
        /*try {
            Log.i("all123", configuration.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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
                    //?
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

        //DIALOG WITH PROGRESS BAR, MAYBE FOR ASYNC CALLS LIKE HTTP REQUESTS
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView progressText = dialog.findViewById(R.id.progress_text);
        progressText.setText(R.string.submission);*/

        try {
            JSONObject upload = new JSONObject();
            upload.put("name", getIntent().getExtras().getString("EXTRA_TEST_STRING"));
            upload.put("version", ConfigurationPusher.getNextVersionNumber(getIntent().getExtras().getString("EXTRA_TEST_STRING").replaceAll("_", "\\."), getApplicationContext()));

            upload.put("content", new JSONObject(contentText.getText().toString()));


            if(ConfroidManager.saveConfiguration(this, upload)) {
                Toast.makeText(this, this.getString(R.string.done)+"!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, this.getString(R.string.submit_error)+".", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //dialog.dismiss();

        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_item:
                contentText.setText(oldContentText);
                //TODO confirm pop up?
                contentText.setEnabled(false);
                dropdownMenu.setEnabled(true);
                invalidateOptionsMenu();
                return true;

            case R.id.edit_item:
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