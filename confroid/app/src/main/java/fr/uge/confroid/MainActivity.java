package fr.uge.confroid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.gui.ConfigurationActivity;
import fr.uge.confroid.gui.MyRecyclerViewAdapter;
import fr.uge.confroid.services.ConfigurationPuller;
import fr.uge.confroid.services.ConfigurationPusher;
import fr.uge.confroid.services.ConfigurationVersions;
import fr.uge.confroid.utlis.ConfroidUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    private MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        HashMap<String, List<String>> content = new HashMap<>();
        ArrayList<String> c = new ArrayList<>();
        c.add("content");
        content.put("1", c);
        Intent intent = new Intent(getApplicationContext(), ConfroidManager.class);
        intent.putExtra("name", "ciao");
        intent.putExtra("content", ConfroidUtils.toBundle(content));

        ConfigurationPusher configurationPusher = new ConfigurationPusher();
        configurationPusher.pushConfiguration(getApplicationContext(), intent);


        Intent intent1 = new Intent(getApplicationContext(), this.getClass());

        intent1.putExtra("name", "ciao");
        intent1.putExtra("requestId", "1");
        intent1.putExtra("version", "1");
        intent1.putExtra("receiver", "fr.uge.confroid.MainActivity");
        intent1.putExtra("expiration", "1");

        ConfigurationPuller configurationPuller = new ConfigurationPuller();
        Intent receive = new Intent();
        //TODO
        try {
            receive = configurationPuller.pullConfiguration(getApplicationContext(), intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("receive1", "name = " + receive.getStringExtra("name"));
        Log.i("receive1", "requestID = " + receive.getStringExtra("requestId"));
        Log.i("receive1", "version = " + receive.getStringExtra("version"));

        Bundle contentBundle = receive.getBundleExtra("content");
        for (String key: contentBundle.keySet())
        {
            Log.i("receive1", key + " = \"" + contentBundle.get(key) + "\"");
        }


        Intent intent2 = new Intent(getApplicationContext(), this.getClass());

        intent2.putExtra("name", "ciao");
        intent2.putExtra("requestId", "1");
        intent2.putExtra("receiver", "fr.uge.confroid.MainActivity");

        ConfigurationVersions configurationVersions = new ConfigurationVersions();
        Intent receive1 = new Intent();
        try {
            receive1 = configurationVersions.loadVersions(getApplicationContext(), intent2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("receive2", "name = " + receive1.getStringExtra("name"));
        Log.i("receive2", "requestID = " + receive1.getStringExtra("requestId"));

        Bundle contentBundle1 = receive1.getBundleExtra("content");
        for (String key: contentBundle1.keySet())
        {
            Log.i("receive2", key + " = \"" + contentBundle1.get(key) + "\"");
        }




        /* TEST SQLITE DB SAVE AND LOAD A CONFIGURATION */
        /*ArrayList<Map<String, Object>> content = new ArrayList<>();
        HashMap<String, Object> entry = new HashMap<>();
        entry.put("1", "ciao");
        content.add(entry);
        HashMap<String, Object> map = new HashMap<>();
        map.put("1", content);

         */

        //ConfroidUtils.saveConfiguration(getApplicationContext(), "fr.uge.calculator", map, "0");

        //ConfroidUtils.loadConfiguration(getApplicationContext(), "fr.uge.calculator", "0", null);
    }

    private void initRecyclerView() {
        //-------------------- RECYCLER VIEW ----------------------
        // data to populate the RecyclerView with
        ArrayList<String> configurations = new ArrayList<>();
        configurations.add("Item 1");
        configurations.add("Item 2");

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

}