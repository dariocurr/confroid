package fr.uge.client;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.saveConfigurationButton).setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", this.getPackageName());
            bundle.putString("tag", "TAG");
            Bundle contentBundle = new Bundle();
            contentBundle.putString("configuration", ((EditText) findViewById(R.id.configurationEditText)).getText().toString());
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            startService(intent);
        });

    }

}