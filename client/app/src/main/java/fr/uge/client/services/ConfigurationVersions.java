package fr.uge.client.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import fr.uge.client.MainActivity;

public class ConfigurationVersions extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("contentVersions", intent.getBundleExtra("versions").toString());
        Log.e("contentVersions", intent.getStringExtra("name"));
        Log.e("contentVersions", intent.getStringExtra("requestId"));
        Intent outComingIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        outComingIntent.putExtra("versions", intent.getBundleExtra("versions"));
        outComingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(outComingIntent);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}