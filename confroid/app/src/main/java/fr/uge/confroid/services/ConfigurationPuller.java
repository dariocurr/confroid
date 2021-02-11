package fr.uge.confroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.utlis.ConfroidUtils;

public class ConfigurationPuller extends Service {

    @Override
    public int onStartCommand(Intent incomingIntent, int flags, int startId) {
        String name = incomingIntent.getStringExtra("name");
        String token = incomingIntent.getStringExtra("token");
        if (TokenDispenser.getToken(ConfroidUtils.getPackageName(name)).equalsIgnoreCase(token)) {
            String requestId = incomingIntent.getStringExtra("requestId");
            String version = incomingIntent.getStringExtra("version");
            String receiver = incomingIntent.getStringExtra("receiver");
            int expiration = incomingIntent.getIntExtra("expiration", 0);
            if (expiration > 0) {
                ConfigurationPusher.subscribe(name, new Subscription(receiver, expiration));
            }
            Intent outgoingIntent = new Intent();
            Bundle content;
            try {
                content = ConfroidManager.loadConfiguration(this.getApplicationContext(), name, Integer.parseInt(version));
            } catch (NumberFormatException ex) {
                if (version.equalsIgnoreCase("latest")) {
                    content = ConfroidManager.loadConfiguration(
                            this.getApplicationContext(), name, ConfigurationPusher.getLatestVersionNumber(name));
                } else {
                    // TODO loadConfigurationByTag
                    content = null;
                }
            }
            outgoingIntent.putExtra("content", content);
            outgoingIntent.putExtra("name", name);
            outgoingIntent.putExtra("requestId", requestId);
            outgoingIntent.putExtra("version", version);
            outgoingIntent.setClassName(ConfroidUtils.getPackageName(receiver), receiver);
            this.startService(outgoingIntent);
        } else {
            Log.e("TokenNotValidException","Token " + token + " isn't valid!");
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented (since we do not use RPC methods)");
    }

}
