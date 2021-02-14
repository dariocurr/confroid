package fr.uge.confroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.utlis.ConfroidManagerUtils;

public class ConfigurationVersions extends Service {

    @Override
    public int onStartCommand(Intent incomingIntent, int flags, int startId) {
        String name = incomingIntent.getStringExtra("name");
        String token = incomingIntent.getStringExtra("token");
        if (TokenDispenser.getToken(ConfroidManagerUtils.getPackageName(name)).equalsIgnoreCase(token)) {
            String requestId = incomingIntent.getStringExtra("requestId");
            String receiver = incomingIntent.getStringExtra("receiver");
            Intent outgoingIntent = new Intent();
            Bundle content;
            content = ConfroidManager.loadAllVersionsBundle(this.getApplicationContext(), name);
            outgoingIntent.putExtra("name", name);
            outgoingIntent.putExtra("requestId", requestId);
            outgoingIntent.putExtra("versions", content);
            outgoingIntent.setClassName(ConfroidManagerUtils.getPackageName(receiver), receiver);
            this.startService(outgoingIntent);
        } else {
            Log.e("TokenNotValidException","Token " + token + " isn't valid!");
        }
        stopSelf();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
