package fr.uge.client;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("classe", this.getClass().getName());

        Intent intent = this.getIntent();
        Log.i("name", intent.getStringExtra("name"));
        Log.i("version", intent.getStringExtra("version"));
        Log.i("content", intent.getStringExtra("content"));
    }

}