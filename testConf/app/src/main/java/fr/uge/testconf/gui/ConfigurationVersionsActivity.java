package fr.uge.testconf.gui;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.testconf.R;

public class ConfigurationVersionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_versions);

        ((TextView) findViewById(R.id.textView)).setText(fromBundleToString(this.getIntent().getBundleExtra("versions")));
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