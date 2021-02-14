package fr.uge.confroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.gui.ConfigurationActivity;
import fr.uge.confroid.gui.MyRecyclerViewAdapter;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
import fr.uge.confroid.utlis.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    private MyRecyclerViewAdapter adapter;
    private static final int CREATE_REQUEST_CODE = 0;
    private static final int OPEN_REQUEST_CODE = 1;
    private static final int SAVE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("ONCREATE", "ON CREATE");

        try {
            initRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        findViewById(R.id.exportConfigurationsButton).setOnClickListener(ev -> {
            createAndSaveFile();
        });

        findViewById(R.id.importConfigurationsButton).setOnClickListener(ev -> {
            openFile();
        });
    }

    private void initRecyclerView() throws JSONException {
        //-------------------- RECYCLER VIEW ----------------------
        // data to populate the RecyclerView with
        List<String> configurations = ConfroidManager.loadAllConfigurationsNames(this.getApplicationContext());
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, configurations);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        Log.i("ONSTOP", "ONRESUME");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("ONSTOP", "ONPAUSE");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("ONSTOP", "ONSTOP");
        super.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), ConfigurationActivity.class);
        Bundle conf = new Bundle();
        conf.putString("EXTRA_TEST_STRING", adapter.getItem(position));
        intent.putExtras(conf);
        startActivity(intent);
    }

    private void createAndSaveFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "confroid_configurations.json");
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Surprisingly, old Android versions does NOT support "application/json"
        intent.setType("*/*");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                writeFileContent(resultData.getData());
                Toast.makeText(this, "Write file successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "File not saved!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == OPEN_REQUEST_CODE) {
            if (resultData != null) {
                String content = readFileContent(resultData.getData());
                Log.e("backup content", content);
                // TODO restoreConfigurationsFromString
                try {

                    JSONObject configurations = new JSONObject(content);
                    Log.i("CONFIGURATI", configurations.toString());
                    for (Iterator<String> it = configurations.keys(); it.hasNext(); ) {
                        String key = it.next();
                        JSONObject jsonObject = configurations.getJSONObject(key);

                        Bundle contentBundle = ConfroidManagerUtils.getAllVersionsFromJsonToBundle(jsonObject);
                        //bundle.putBundle("content", contentBundle);
                        Log.i("CONFIGURATI", contentBundle.toString());

                        for(String keyBundle : contentBundle.keySet()){
                            Bundle bundle = new Bundle();
                            Bundle versionBundle = new Bundle();
                            bundle.putString("name", jsonObject.getString("name"));
                            bundle.putString("token", jsonObject.getString("token"));
                            bundle.putInt("version", Integer.parseInt(keyBundle));

                            bundle.putBundle("content", contentBundle.getBundle(keyBundle));

                            ConfroidManager.saveConfiguration(this.getApplicationContext(), bundle);
                        }
                        finish();
                        startActivity(getIntent());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void writeFileContent(Uri uri) {
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            JSONObject configurationsJsonObject = ConfroidManager.getAllConfigurations(this.getApplicationContext());
            outputStream.write(configurationsJsonObject.toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", "");
        } catch (IOException e) {
            Toast.makeText(this, "Fail to write file!", Toast.LENGTH_SHORT).show();
        }
    }

    private String readFileContent(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            String content = total.toString();
            return content;
        } catch (IOException e) {
            Toast.makeText(this, "Fail to read file!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}