package fr.uge.confroid.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.Configuration;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.sqlite.ConfroidContract;
import fr.uge.confroid.sqlite.ConfroidDbHelper;

import java.util.Date;
import java.util.Map;

public class ConfigurationPusher {

    public void pushConfiguration(Context context, Intent intent) {
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
        /*if (TokenDispenser.getDispensedTokens().get(name).equalsIgnoreCase(token)) {
            String tag = intent.getStringExtra("tag");
            ConfroidManager.addConfiguration(context, name, bundle, tag);
        }*/
        //String tag = intent.getStringExtra("tag");
        String tag = "1";
        ConfroidManager.saveConfiguration(context, name, bundle, tag);
    }

}
