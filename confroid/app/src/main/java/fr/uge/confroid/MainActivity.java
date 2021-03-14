package fr.uge.confroid;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonArray;
import fr.uge.confroid.gui.ConfigurationAdapter;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
import fr.uge.confroid.utlis.FileUtils;
import fr.uge.confroid.web.Client;
import fr.uge.confroid.web.LoginActivity;
import fr.uge.confroid.web.Server;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConfigurationAdapter.ItemClickListener{
    private ConfigurationAdapter adapter;
    private static final int CREATE_REQUEST_CODE = 0;
    private static final int OPEN_REQUEST_CODE = 1;
    private static final int OPEN_REQUEST_CODE_1 = 3;
    public static final int CHOOSE_REQUEST_CODE = 2;
    public static final String EXTRA_CONFIGURATION_NAME = "EXTRA_CONFIGURATION_NAME";
    private static boolean auth = false;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONObject databaseObj = new JSONObject();
        try {
            JSONObject user1 = new JSONObject();
            user1.put("username", "admin");
            user1.put("password", "admin");

            JSONArray users = new JSONArray();
            users.put(user1);
            databaseObj.put("users", users);

            File database = new File(this.getFilesDir(),"database.json");
            FileUtils.writeFile(database, databaseObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            initRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initRecyclerView() throws JSONException {
        //-------------------- RECYCLER VIEW ----------------------
        // data to populate the RecyclerView with
        List<String> configurations = ConfroidManager.loadAllConfigurationsNames(this.getApplicationContext());
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.configuration_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConfigurationAdapter(this, configurations);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void refreshConfigurationsList() {
        adapter.setData(ConfroidManager.loadAllConfigurationsNames(this.getApplicationContext()));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        refreshConfigurationsList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), ViewActivity.class);
        Bundle conf = new Bundle();
        conf.putString(EXTRA_CONFIGURATION_NAME, adapter.getItem(position));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_device:
                openFile();
                return true;

            case R.id.import_server:
                String authentication = getIntent().getStringExtra("auth");
                if(authentication != null && authentication.equals("true")) {
                    auth = true;
                }
                if(auth) {
                    Server server = new Server();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                server.start();
                                JSONObject jsonObject = ConfroidManager.getAllConfigurations(getBaseContext());


                                Intent intent = new Intent(getBaseContext(), ImportActivity.class);
                                // Surprisingly, old Android versions does NOT support "application/json"
                                //intent.setType("*/*");
                                intent.putExtra("CONFIGURATIONS", jsonObject.toString());
                                startActivityForResult(intent, OPEN_REQUEST_CODE_1);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();


                    //TODO import from server

                    return true;
                }
                else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            case R.id.export_device:
                createAndSaveFile();
                return true;

            case R.id.export_server:
                authentication = getIntent().getStringExtra("auth");
                if(authentication != null && authentication.equals("true")) {
                    auth = true;
                }
                if(auth) {
                    Server server = new Server();
                    Client client = new Client();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                server.start();

                                client.post(server.getUrl(), ConfroidManager.getAllConfigurations(getBaseContext()).toString());
                                server.saveRequest();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();


                    // TODO export to server
                    return true;
                }
                else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

            case R.id.refresh_item:
                refreshConfigurationsList();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                writeFileContent(resultData.getData());
                Toast.makeText(this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.file_not_saved), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == OPEN_REQUEST_CODE) {
            if (resultData != null) {
                String content = readFileContent(resultData.getData());

                Intent intent = new Intent(getBaseContext(), ImportActivity.class);
                intent.putExtra("CONFIGURATIONS", content);

                startActivity(intent);
            }
        }
        else if (requestCode == OPEN_REQUEST_CODE_1) {
            Log.e("SONO QUI", "QUIII");
            if (resultData != null) {
                String content = resultData.getStringExtra("configurations");

                Intent intent = new Intent(getBaseContext(), ImportActivity.class);
                intent.putExtra("CONFIGURATIONS", content);

                startActivity(intent);

                /*try {
                    JSONObject configurations = new JSONObject(content);
                    for (Iterator<String> it = configurations.keys(); it.hasNext(); ) {
                        String key = it.next();
                        JSONObject jsonObject = configurations.getJSONObject(key);

                        File file = new File(this.getApplicationContext().getFilesDir(), jsonObject.getString("name").replaceAll("\\.", "_") + ".json");
                        file.delete();
                        ConfigurationPusher.resetVersionNumber(jsonObject.getString("name"), getApplicationContext());

                        Bundle contentBundle = ConfroidManagerUtils.getAllVersionsFromJsonToBundle(jsonObject);

                        for(String keyBundle : contentBundle.keySet()){
                            Bundle bundle = new Bundle();
                            bundle.putString("name", jsonObject.getString("name"));
                            bundle.putString("token", jsonObject.getString("token"));
                            bundle.putInt("version", ConfigurationPusher.getNextVersionNumber(jsonObject.getString("name"), getApplicationContext()));

                            bundle.putBundle("content", contentBundle.getBundle(keyBundle).getBundle("content"));

                            ConfroidManager.saveConfiguration(this.getApplicationContext(), bundle);
                        }
                        finish();
                        startActivity(getIntent());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

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
            Toast.makeText(this, getString(R.string.write_error), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, getString(R.string.read_error), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

}