package fr.uge.confroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.receivers.TokenDispenser;

public class ConfigurationPuller {

    public Intent pullConfiguration(Context context, Intent intent) throws Exception {
        String name = intent.getStringExtra("name");
        String token = intent.getStringExtra("token");
        String requestId = intent.getStringExtra("requestId");
        String version = intent.getStringExtra("version");
        String receiver = intent.getStringExtra("receiver");
        String expiration = intent.getStringExtra("expiration");

        Class cls = null;
        try {
            cls = Class.forName(receiver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //if (TokenDispenser.getDispensedTokens().get(name).equalsIgnoreCase(token)) {
            /*
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
            */

        //} else {
            //throw new Exception("Token not valid!");
        //}
        return ConfroidManager.loadConfiguration(context, name, requestId, version);
    }
}
