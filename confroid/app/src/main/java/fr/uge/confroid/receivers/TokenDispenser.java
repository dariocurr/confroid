package fr.uge.confroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidUtils;

import java.util.*;

public class TokenDispenser extends BroadcastReceiver {

    private static final int TOKEN_LENGTH = 20;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Map<String, String>  DISPENSED_TOKENS = new HashMap<>();

    private static String getRandomToken(int count) {
        String token = "";
        while (count-- > 0) {
            token += (CHARACTERS.charAt((int)(Math.random() * CHARACTERS.length())));
        }
        return token;
    }

    public static String getToken(String key) {
        return DISPENSED_TOKENS.get(key);
    }

    @Override
    public void onReceive(Context context, Intent incomingIntent) {
        String receiver = incomingIntent.getStringExtra("receiver");
        String packageName = ConfroidUtils.getPackageName(receiver);
        if (!DISPENSED_TOKENS.keySet().contains(packageName)) {
            DISPENSED_TOKENS.put(packageName, getRandomToken(TOKEN_LENGTH));
        }
        Intent outgoingIntent = new Intent();
        outgoingIntent.setClassName(packageName, receiver);
        outgoingIntent.putExtra("token", DISPENSED_TOKENS.get(packageName));
        context.startService(outgoingIntent);
    }

}
