package fr.uge.confroid.services;

import android.content.Intent;
import android.os.Bundle;
import fr.uge.confroid.Configuration;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;

import java.util.Map;

public class ConfigurationPusher {

    public void pushConfiguration(Intent intent) {
        String name = intent.getStringExtra("name");
        Bundle bundle = intent.getBundleExtra("content");
        String token = bundle.getString("token");
        /*
        if (name.contains("/")) {
            String cellToEdit = name.split("/")[1];
            Configuration configuration = ConfroidManager.getLatestConfiguration(name);
            configuration.content.put(cellToEdit, bundle.get(cellToEdit));
        } else {
            String tag = intent.getStringExtra("tag");
        }
        */
        if (TokenDispenser.getDispensedTokens().get(name).equalsIgnoreCase(token)) {
            String tag = intent.getStringExtra("tag");
            ConfroidManager.addConfiguration(name, bundle, tag);
        }
    }

}
