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

import java.util.*;

public class ConfigurationPusher extends Service {

    private static final Map<String, List<Subscription>> OBSERVERS = new HashMap<>();
    private static final Map<String, Integer> VERSION_NUMBER = new HashMap<>();
    private static final String UPDATE_OBSERVER_REQUEST_ID = "-1";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getBundleExtra("bundle");
        String name = bundle.getString("name");
        String token = bundle.getString("token");
        if (TokenDispenser.getToken(ConfroidManagerUtils.getPackageName(name)).equalsIgnoreCase(token)) {
            if (!bundle.containsKey("content") && bundle.containsKey("tag")) {
                ConfroidManager.updateTag(this.getApplicationContext(), ConfroidManagerUtils.getPackageName(name), bundle.get("tag").toString(), getLatestVersionNumber(name));
            } else {
                if (name.contains("/")) {
                    String contentToEdit = name.substring(name.indexOf("/") + 1);
                    name = ConfroidManagerUtils.getPackageName(name);
                    bundle.putString("name", name);
                    ConfroidManager.updateContent(this.getApplicationContext(), bundle, contentToEdit);
                    this.notifyObservers(name, Integer.valueOf(contentToEdit.substring(0, contentToEdit.indexOf("/"))));
                } else {
                    int newVersionNumber = getNextVersionNumber(name);
                    bundle.putInt("version", newVersionNumber);
                    ConfroidManager.saveConfiguration(this.getApplicationContext(), bundle);
                    this.notifyObservers(name, newVersionNumber);
                }
            }
        } else {
            Log.e("TokenNotValidException", "Token " + token + " isn't valid!");
        }
        return START_NOT_STICKY;
    }

    private void notifyObservers(String name, Integer versionNumber) {
        Bundle bundle = ConfroidManager.loadConfiguration(this.getApplicationContext(), name, versionNumber);
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("version", versionNumber.toString());
        intent.putExtra("content", bundle.getBundle("content"));
        intent.putExtra("requestId", UPDATE_OBSERVER_REQUEST_ID);
        List<Subscription> observers = OBSERVERS.get(name);
        for (Subscription subscription : getObservers(name)) {
            long currentTime = System.currentTimeMillis();
            if (subscription.isExpired(currentTime)) {
                observers.remove(subscription);
            } else {
                intent.setClassName(ConfroidManagerUtils.getPackageName(subscription.getSubscriber()), subscription.getSubscriber());
                this.startService(intent);
            }
        }
    }

    private List<Subscription> getObservers(String name) {
        List<Subscription> observers = new ArrayList<>();
        if (OBSERVERS.containsKey(name)) {
            observers = OBSERVERS.get(name);
        }
        return observers;
    }

    private static int getNextVersionNumber(String name) {
        if (!VERSION_NUMBER.containsKey(name)) {
            VERSION_NUMBER.put(name, -1);
        }
        int version = VERSION_NUMBER.get(name) + 1;
        VERSION_NUMBER.put(name, version);
        return version;
    }

    public static int getLatestVersionNumber(String name) {
        return VERSION_NUMBER.get(name);
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