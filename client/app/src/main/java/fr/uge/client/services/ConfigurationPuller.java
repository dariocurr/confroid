package fr.uge.client.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

public class ConfigurationPuller extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("content", intent.getBundleExtra("content").toString());
        Log.e("content", intent.getStringExtra("name"));
        Log.e("content", intent.getStringExtra("requestId"));
        Log.e("content", intent.getStringExtra("version"));
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
