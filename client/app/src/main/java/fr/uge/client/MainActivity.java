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
            Bundle innerContentBundle = new Bundle();
            EditText configurationEditText = findViewById(R.id.configurationEditText);
            innerContentBundle.putString("a", String.valueOf(configurationEditText.getText()));
            contentBundle.putBundle("abc", innerContentBundle);
            bundle.putString("name", getPackageName());
            bundle.putString("tag", "latest");
            bundle.putString("token", TokenPuller.getToken(this.getApplicationContext()));
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);

            this.startForegroundService(intent);
        });

        findViewById(R.id.loadConfigurationButton).setOnClickListener(ev -> {
            Intent intent = new Intent();
            intent.putExtra("name", this.getPackageName());
            intent.putExtra("token", TokenPuller.getToken(this.getApplicationContext()));
            intent.putExtra("requestId", "1");
            intent.putExtra("version", "0");
            intent.putExtra("receiver", "fr.uge.client.services.ConfigurationPuller");
            intent.putExtra("expiration", 60);
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
            this.startForegroundService(intent);
        });

        findViewById(R.id.loadVersionsButton).setOnClickListener(ev -> {
            Intent intent = new Intent();
            intent.putExtra("name", this.getPackageName());
            intent.putExtra("token", TokenPuller.getToken(this.getApplicationContext()));
            intent.putExtra("requestId", "1");
            intent.putExtra("receiver", "fr.uge.client.services.ConfigurationVersions");
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationVersions");
            this.startForegroundService(intent);
        });

        findViewById(R.id.updateTagButton).setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", getPackageName());
            bundle.putString("tag", "dario");
            bundle.putString("token", TokenPuller.getToken(this.getApplicationContext()));
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            Log.i("Bundle123", bundle.toString());
            this.startForegroundService(intent);
        });

        findViewById(R.id.editConfigurationButton).setOnClickListener(ev -> {
            Bundle bundle = new Bundle();
            Bundle contentBundle = new Bundle();
            contentBundle.putString("A", "B");
            bundle.putString("name", getPackageName() + "/0/abc");
            bundle.putString("token", TokenPuller.getToken(this.getApplicationContext()));
            bundle.putBundle("content", contentBundle);
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            Log.i("Bundle123", bundle.toString());
            this.startForegroundService(intent);
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

    public static String fromBundleToString(Bundle bundle) {
        return fromBundleToString(bundle, 0);
    }

    private static String fromBundleToString(Bundle bundle, int tabNumber) {
        String content = "";
        for (String key : bundle.keySet()) {
            for (int i = 0; i < tabNumber; i++) {
                content += "\t";
            }
            content += key + ": ";
            Object contentObject = bundle.get(key);
            if (contentObject instanceof Bundle) {
                content += "\n" + fromBundleToString((Bundle) contentObject, tabNumber + 1);
            } else {
                content += contentObject.toString();
            }
            content += "\n";
        }
        return content;
    }

}