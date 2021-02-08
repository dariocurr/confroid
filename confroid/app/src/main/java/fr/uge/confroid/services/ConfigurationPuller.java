package fr.uge.confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

public class ConfigurationPuller {

    public static void pullConfiguration(Context context, Intent intent)  {
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
            // TODO retrieve configuration
            // TODO send configuration
        } else {
            // TODO raise tokenNotValidException
        }
    }

}
