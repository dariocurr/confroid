package fr.uge.confroid.receivers;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class TokenDispenser {

    private static final int TOKEN_LENGTH = 20;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Map<String, String>  DISPENSED_TOKENS = new HashMap<>();

    public static String getToken(Intent intent) {
        String receiver = intent.getStringExtra("receiver");
        if (!DISPENSED_TOKENS.keySet().contains(receiver)) {
            String token = getRandomToken(TOKEN_LENGTH);
            DISPENSED_TOKENS.put(receiver, token);
        }
        // TODO DISPENSED_TOKENS.get(receiver) send to receiver
        return null;
    }

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

}
