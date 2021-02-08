package fr.uge.confroid.services;

import android.content.Context;
import android.content.Intent;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.receivers.TokenDispenser;

public class ConfigurationVersions {

    public static void pullAllConfigurations(Context context, Intent intent) throws Exception {
        String name = intent.getStringExtra("name");
        String token = intent.getStringExtra("token");

        if (TokenDispenser.getDispensedTokens().get(name).equalsIgnoreCase(token)) {
            String requestId = intent.getStringExtra("requestId");
            String receiver = intent.getStringExtra("receiver");
            // TODO retrieve allConfiguration
            // TODO send intent to receiver
            /*
            Class cls = null;
            try {
                cls = Class.forName(receiver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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

        } else {
            // TODO raise tokenNotValidException
        }
    }

}
