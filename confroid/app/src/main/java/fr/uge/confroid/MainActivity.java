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
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.gui.ConfigurationActivity;
import fr.uge.confroid.gui.MyRecyclerViewAdapter;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
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

        try {
            initRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        findViewById(R.id.newFileButton).setOnClickListener(ev -> {
            newFile();
        });
        */

        findViewById(R.id.exportConfigurationsButton).setOnClickListener(ev -> {
            saveFile();
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
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), ConfigurationActivity.class);
        Bundle conf = new Bundle();
        conf.putString("EXTRA_TEST_STRING", adapter.getItem(position));
        intent.putExtras(conf);
        startActivity(intent);
    }

    /*
    public void newFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, ".json");
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }
     */

    public void saveFile() {
        Intent createIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        createIntent.setType("application/json");
        createIntent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent saveIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        saveIntent.setType("application/json");
        saveIntent.addCategory(Intent.CATEGORY_OPENABLE);
        createIntent.putExtra(Intent.EXTRA_TITLE, "confroid_configurations.json");
        startActivityForResult(createIntent, CREATE_REQUEST_CODE);
        startActivityForResult(saveIntent, SAVE_REQUEST_CODE);
    }

    public void openFile()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            Uri currentUri = null;
            if (requestCode == SAVE_REQUEST_CODE) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    writeFileContent(currentUri);
                }
            } else if (requestCode == OPEN_REQUEST_CODE) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    try {
                        String content = readFileContent(currentUri);
                    } catch (IOException e) {
                        // Handle error here
                    }
                }
            }
        }
    }

    public void writeFileContent(Uri uri) {
        try{
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");

            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());


            JSONObject configurationsJsonObject = ConfroidManager.getAllConfigurations(this.getApplicationContext());;

            fileOutputStream.write(configurationsJsonObject.toString().getBytes());

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFileContent(Uri uri) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        String content = total.toString();
        return content;
    }


}