package fr.uge.confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.receivers.TokenDispenser;

public class ConfigurationPuller {

    public void pullConfiguration(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        //String token = intent.getStringExtra("token");
        String token = "1";
        String requestID = intent.getStringExtra("requestID");
        String version = intent.getStringExtra("version");
        String receiver = intent.getStringExtra("receiver");
        String expiration = intent.getStringExtra("expiration");

        /*
        if (name.contains("/")) {
            String cellToEdit = name.split("/")[1];
            Configuration configuration = ConfroidManager.getLatestConfiguration(name);
            configuration.content.put(cellToEdit, bundle.get(cellToEdit));
        } else {
            String tag = intent.getStringExtra("tag");
        }
        */

        Class cls = null;
        try {
            cls = Class.forName(receiver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if ("1".equalsIgnoreCase(token)) {
            //Intent intentToApp = new Intent(null, cls);
            Intent intentToApp = new Intent(Intent.ACTION_SEND);
            intentToApp.setClassName(cls.getPackage().getName(),
                    receiver);

            intentToApp.putExtra("requestId", requestID);
            intentToApp.putExtra("name", name);
            intentToApp.putExtra("version", version);

            Bundle content = ConfroidManager.loadConfiguration(context, name, requestID, version);

            intentToApp.putExtra("content", content);
            intentToApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intentToApp);

        }
    }
}
