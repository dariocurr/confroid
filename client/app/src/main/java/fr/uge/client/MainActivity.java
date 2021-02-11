package fr.uge.client;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.client.services.TokenPuller;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TokenPuller.askToken(this.getApplicationContext());

        findViewById(R.id.saveConfigurationButton).setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            Bundle contentBundle = new Bundle();
            contentBundle.putString("text", "text");
            bundle.putString("name", getPackageName());
            bundle.putString("tag", "latest");
            bundle.putString("token", TokenPuller.getToken(this.getApplicationContext()));
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            Log.i("Bundle123", bundle.toString());
            this.startService(intent);
        });

        findViewById(R.id.loadConfigurationButton).setOnClickListener(ev -> {
            Intent intent = new Intent();
            intent.putExtra("name", this.getPackageName());
            intent.putExtra("token", TokenPuller.getToken(this.getApplicationContext()));
            intent.putExtra("requestId", "1");
            intent.putExtra("version", "latest");
            intent.putExtra("receiver", "fr.uge.client.services.ConfigurationPuller");
            intent.putExtra("expiration", 60);
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
            this.startService(intent);
        });

    }

}