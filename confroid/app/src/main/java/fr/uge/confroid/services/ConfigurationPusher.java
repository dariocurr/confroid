package fr.uge.confroid.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationPusher extends JobIntentService {

    private static final Map<String, List<Subscription>> OBSERVERS = new HashMap<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getBundleExtra("bundle");
        String name = bundle.getString("name");
        String token = bundle.getString("token");
        //TokenDispenser.getDispensedTokens().get(name)
        if ("1".equalsIgnoreCase(token)) {
            /*
            if (name.contains("/")) {
                String cellToEdit = name.split("/")[1];
                // TODO retrieve last configuration
                // TODO edit last configuration
            }
            */
            ConfroidManager.saveConfiguration(this.getApplicationContext(), bundle);
            //this.notifyObservers(name, intent);
        } else {
            Log.e("TokenNotValidException","Token " + token + " isn't valid!");
        }
        return START_NOT_STICKY;
    }

    public void notifyObservers(String name, Intent newConfiguration) {
        List<Subscription> observers = OBSERVERS.get(name);
        for (Subscription subscription : OBSERVERS.get(name)) {
            if (subscription.isExpired(System.currentTimeMillis())) {
                observers.remove(subscription);
            } else {
                // TODO send intent to service
            }
        }
    }

    public static void subscribe(String name, Subscription subscription) {
        if (!OBSERVERS.containsKey(name)) {
            OBSERVERS.put(name, new ArrayList<>());
        }
        OBSERVERS.get(name).add(subscription);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented (since we do not use RPC methods)");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
