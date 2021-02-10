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
            bundle.putString("name", getPackageName());
            bundle.putString("tag", "latest");
            bundle.putString("token", TokenPuller.getToken(this.getApplicationContext()));
            Bundle contentBundle = new Bundle();
            contentBundle.putString("text", ((EditText) findViewById(R.id.configurationEditText)).getText().toString());
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
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