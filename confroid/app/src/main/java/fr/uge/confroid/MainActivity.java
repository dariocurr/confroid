package fr.uge.confroid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.services.ConfigurationPuller;
import fr.uge.confroid.services.ConfigurationPusher;
import fr.uge.confroid.utlis.ConfroidUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, List<String>> content = new HashMap<>();
        ArrayList<String> c = new ArrayList<>();
        c.add("contenuto");
        content.put("1", c);
        Intent intent = new Intent(getApplicationContext(), ConfroidManager.class);
        intent.putExtra("name", "ciao");
        intent.putExtra("content", ConfroidUtils.toBundle(content));

        ConfigurationPusher configurationPusher = new ConfigurationPusher();
        configurationPusher.pushConfiguration(getApplicationContext(), intent);


        Intent intent1 = new Intent(getApplicationContext(), this.getClass());

        intent1.putExtra("name", "ciao");
        intent1.putExtra("requestID", "1");
        intent1.putExtra("version", "1");
        intent1.putExtra("receiver", "fr.uge.confroid.MainActivity");
        intent1.putExtra("expiration", "1");

        ConfigurationPuller configurationPuller = new ConfigurationPuller();
        configurationPuller.pullConfiguration(getApplicationContext(), intent1);

        Intent receive = getIntent();

        Log.i("receive", receive.getStringExtra("name"));


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
}