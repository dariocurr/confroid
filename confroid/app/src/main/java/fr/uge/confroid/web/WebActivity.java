package fr.uge.confroid.web;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.R;
import org.json.JSONException;

import java.net.URISyntaxException;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        try {
            Server server = new Server();
        } catch (JSONException  e) {
            e.printStackTrace();
        }
    }
}