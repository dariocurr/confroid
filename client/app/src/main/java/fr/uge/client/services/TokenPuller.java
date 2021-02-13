package fr.uge.client.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

public class TokenPuller extends Service {

    private static String TOKEN;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TOKEN = intent.getStringExtra("token");
        Log.e("TOKEN", TOKEN);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void askToken(Context context) {
        Intent intent = new Intent();
        intent.putExtra("receiver", "fr.uge.client.services.TokenPuller");
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.receivers.TokenDispenser");
        context.sendBroadcast(intent);
    }

    public static String getToken(Context context) {
        return TOKEN;
    }

}
