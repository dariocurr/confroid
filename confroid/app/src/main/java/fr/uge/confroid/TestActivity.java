package fr.uge.confroid;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.utlis.ConfroidUtils;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Intent receive = getIntent();

        Log.i("receiver123", receive.getStringExtra("name"));
        Log.i("receiver123", receive.getStringExtra("version"));
        Log.i("receiver123", ConfroidUtils.fromBundleToString(receive.getBundleExtra("content")));

        TextView textView = findViewById(R.id.textView);
        textView.setText(receive.getStringExtra("name"));
    }
}