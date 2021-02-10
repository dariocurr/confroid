package fr.uge.confroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, String> getDispensedTokens() {
        return DISPENSED_TOKENS;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String receiver = intent.getStringExtra("receiver");
        if (!DISPENSED_TOKENS.keySet().contains(receiver)) {
            String token = getRandomToken(TOKEN_LENGTH);
            DISPENSED_TOKENS.put(receiver, token);
        }
        try {
            Intent i = new Intent().setClass(context, Class.forName(receiver));
            context.startService(i);
        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException","Class " + receiver + " doesn't exists!");
        }
    }

}
