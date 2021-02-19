package fr.uge.client;

import android.content.Intent;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.client.services.TokenPuller;

public class MainActivity extends AppCompatActivity {

    private Button saveConfigurationButton;
    private Button loadConfigurationButton;
    private Button loadVersionsButton;
    private Button updateTagButton;
    private Button editConfigurationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TokenPuller.askToken(this.getApplicationContext());
        this.saveConfigurationButton = findViewById(R.id.saveConfigurationButton);
        this.loadConfigurationButton = findViewById(R.id.loadConfigurationButton);
        this.loadVersionsButton = findViewById(R.id.loadVersionsButton);
        this.updateTagButton = findViewById(R.id.updateTagButton);
        this.editConfigurationButton = findViewById(R.id.editConfigurationButton);
        this.saveConfigurationButton.setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            Bundle contentBundle = new Bundle();
            Bundle innerContentBundle = new Bundle();
            EditText configurationEditText = findViewById(R.id.configurationEditText);
            innerContentBundle.putString("a", String.valueOf(configurationEditText.getText()));
            contentBundle.putBundle("abc", innerContentBundle);
            bundle.putString("name", getPackageName());
            bundle.putString("tag", "latest");
            bundle.putString("token", TokenPuller.getToken());
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(intent);
            } else {
                this.startService(intent);
            }

        });
        this.loadConfigurationButton.setOnClickListener(ev -> {
            Intent intent = new Intent();
            intent.putExtra("name", this.getPackageName());
            intent.putExtra("token", TokenPuller.getToken());
            intent.putExtra("requestId", "1");
            intent.putExtra("version", "0");
            intent.putExtra("receiver", "fr.uge.client.services.ConfigurationPuller");
            intent.putExtra("expiration", 60);
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(intent);
            } else {
                this.startService(intent);
            }
        });
        this.loadVersionsButton.setOnClickListener(ev -> {
            Intent intent = new Intent();
            intent.putExtra("name", this.getPackageName());
            intent.putExtra("token", TokenPuller.getToken());
            intent.putExtra("requestId", "1");
            intent.putExtra("receiver", "fr.uge.client.services.ConfigurationVersions");
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationVersions");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(intent);
            } else {
                this.startService(intent);
            }
        });
        this.updateTagButton.setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", getPackageName());
            bundle.putString("tag", "dario");
            bundle.putString("token", TokenPuller.getToken());
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(intent);
            } else {
                this.startService(intent);
            }
        });
        this.editConfigurationButton.setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            Bundle contentBundle = new Bundle();
            contentBundle.putString("A", "B");
            bundle.putString("name", getPackageName() + "/0/abc");
            bundle.putString("token", TokenPuller.getToken());
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(intent);
            } else {
                this.startService(intent);
            }
        });

        /* PRINT IN THE TEXTVIEW ALL THE VERSIONS ******
        Intent inComingIntent = getIntent();
        Bundle versionsBundle = new Bundle();
        versionsBundle = inComingIntent.getBundleExtra("versions");
        TextView textView = findViewById(R.id.configurationTextView);

        if(versionsBundle != null)
            textView.setText(fromBundleToString(versionsBundle));
         */

    }

    public void disableButtons() {
        this.saveConfigurationButton.setEnabled(false);
        this.loadConfigurationButton.setEnabled(false);
        this.loadVersionsButton.setEnabled(false);
        this.updateTagButton.setEnabled(false);
        this.editConfigurationButton.setEnabled(false);
    }

    public void enableButtons() {
        this.saveConfigurationButton.setEnabled(true);
        this.loadConfigurationButton.setEnabled(true);
        this.loadVersionsButton.setEnabled(true);
        this.updateTagButton.setEnabled(true);
        this.editConfigurationButton.setEnabled(true);
    }

}