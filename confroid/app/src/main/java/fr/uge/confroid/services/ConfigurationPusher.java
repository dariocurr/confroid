package fr.uge.confroid.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationPusher extends Service {

    private static final Map<String, List<Subscription>> OBSERVERS = new HashMap<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("bundle");
        //Log.e("content", "\n\n" + ConfroidUtils.fromBundleToString(bundle));
        String name = bundle.getString("name");
        String token = bundle.getString("token");;
        //TokenDispenser.getDispensedTokens().get(name)
        if ("1".equalsIgnoreCase(token)) {
            String tag = bundle.getString("tag");
            // TODO get content
            if (name.contains("/")) {
                String cellToEdit = name.split("/")[1];
                // TODO retrieve last configuration
                // TODO edit last configuration
            }
            // TODO save content
            //ConfroidManager.saveConfiguration(context, name, bundle, tag);
            notifyObservers(name, null);
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
                try {
                    newConfiguration.setClass(this.getApplicationContext(), Class.forName(subscription.getSubscriber()));
                    startService(newConfiguration);
                } catch (ClassNotFoundException e) {
                    Log.e("ClassNotFoundException","Class " + subscription.getSubscriber() + " doesn't exists!");
                }
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
}
