package fr.uge.client;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
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
            bundle.putString("token", "1");
            bundle.putString("version", "1");
            Bundle contentBundle = new Bundle();
            contentBundle.putString("configuration", ((EditText) findViewById(R.id.configurationEditText)).getText().toString());
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);

            startForegroundService(intent);
        });

        findViewById(R.id.loadConfigurationButton).setOnClickListener(ev -> {
            Intent intent = new Intent();

            intent.putExtra("name", this.getPackageName());
            intent.putExtra("tag", "TAG");
            intent.putExtra("requestId", "1");
            intent.putExtra("version", "1");
            intent.putExtra("token", "1");
            intent.putExtra("expiration", 60);

            Log.i("packageName", this.getPackageName());

            intent.putExtra("receiver", this.getPackageName());

            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
            startForegroundService(intent);
        });

    }

}