package fr.uge.confroid.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.utlis.ConfroidUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationPusher {

    private static final Map<String, List<Subscription>> OBSERVERS = new HashMap<>();

    public static void pushConfiguration(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("bundle");
        Log.e("content", "\n\n" + ConfroidUtils.fromBundleToString(bundle));
        String name = intent.getStringExtra(bundle.getString("name"));
        String token = intent.getStringExtra(bundle.getString("token"));
        //TokenDispenser.getDispensedTokens().get(name)
        if ("1".equalsIgnoreCase(token)) {
            String tag = intent.getStringExtra("tag");
            // TODO get content
            if (name.contains("/")) {
                String cellToEdit = name.split("/")[1];
                // TODO retrieve last configuration
                // TODO edit last configuration
            }
            // TODO save content
            ConfroidManager.saveConfiguration(context, name, bundle, tag);
            notifyObservers(name, null);
        } else {
            // TODO raise tokenNotValidException
        }
    }

    public static void notifyObservers(String name, Intent newConfiguration) {
        List<Subscription> observers = OBSERVERS.get(name);
        for (Subscription subscription : OBSERVERS.get(name)) {
            if (subscription.isExpired(System.currentTimeMillis())) {
                observers.remove(subscription);
            } else {
                // TODO send new intent to subscriber
            }
        }
    }

    public static void subscribe(String name, Subscription subscription) {
        if (!OBSERVERS.containsKey(name)) {
            OBSERVERS.put(name, new ArrayList<>());
        }
        OBSERVERS.get(name).add(subscription);
    }

}
