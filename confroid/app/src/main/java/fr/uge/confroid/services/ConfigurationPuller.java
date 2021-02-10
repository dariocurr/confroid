package fr.uge.confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.utlis.ConfroidUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationPuller extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("name");
        String token = intent.getStringExtra("token");
        if (TokenDispenser.getDispensedTokens().get(name).equalsIgnoreCase(token)) {
            String requestId = intent.getStringExtra("requestId");
            String version = intent.getStringExtra("version");
            String receiver = intent.getStringExtra("receiver");
            int expiration = intent.getIntExtra("expiration", 0);
            if (expiration > 0) {
                ConfigurationPusher.subscribe(name, new Subscription(receiver, expiration));
            }
            Intent configuration = new Intent();
            Bundle content = ConfroidManager.loadConfiguration(this.getApplicationContext(), name, version);
            intent.putExtra("content", content);
            intent.putExtra("name", name);
            intent.putExtra("requestId", requestId);
            intent.putExtra("version", version);
            // TODO sent intent to service
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
