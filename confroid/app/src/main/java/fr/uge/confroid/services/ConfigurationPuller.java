package fr.uge.confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;
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

        //TokenDispenser.getDispensedTokens().get(name)
        if ("1".equalsIgnoreCase(token)) {
            String requestId = intent.getStringExtra("requestId");
            String version = intent.getStringExtra("version");
            String receiver = intent.getStringExtra("receiver");
            int expiration = intent.getIntExtra("expiration", 0);
            if (expiration > 0) {
                ConfigurationPusher.subscribe(name, new Subscription(receiver, expiration));
            }
            // TODO retrieve configuration
            //ConfroidManager.loadConfiguration(context, name, requestId, version, receiver);
            try {
                Intent i = new Intent().setClass(this.getApplicationContext(), Class.forName(receiver));
                startService(i);
            } catch (ClassNotFoundException e) {
                Log.e("ClassNotFoundException","Class " + receiver + " doesn't exists!");
            }
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
