package fr.uge.confroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.utlis.ConfroidUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ConfroidUtils {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* TEST SQLITE DB SAVE AND LOAD A CONFIGURATION */
        ArrayList<Map<String, Object>> content = new ArrayList<>();
        HashMap<String, Object> entry = new HashMap<>();
        entry.put("1", "ciao");
        content.add(entry);
        HashMap<String, Object> map = new HashMap<>();
        map.put("1", content);

        saveConfiguration(getApplicationContext(), "fr.uge.calculator", map, "0");

        loadConfiguration(getApplicationContext(), "fr.uge.calculator", "0", null);


    }
}