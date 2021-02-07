package fr.uge.confroid.services;

import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import fr.uge.confroid.Configuration;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;

import java.util.Map;

public class ConfigurationPusher {

    public void pushConfiguration(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        Bundle bundle = intent.getBundleExtra("content");
        //String token = bundle.getString("token");
        String token = "1";
        /*
        if (name.contains("/")) {
            String cellToEdit = name.split("/")[1];
            Configuration configuration = ConfroidManager.getLatestConfiguration(name);
            configuration.content.put(cellToEdit, bundle.get(cellToEdit));
        } else {
            String tag = intent.getStringExtra("tag");
        }
        */
        if ("1".equalsIgnoreCase(token)) {
            String tag = intent.getStringExtra("tag");
            ConfroidManager.addConfiguration(context, name, bundle, tag);
        }
    }

}
