package fr.uge.confroid.receivers;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class TokenDispenser {

    private static final Map<String, String>  DISPENSED_TOKENS = new HashMap<>();
    private static final int TOKEN_LENGTH = 20;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String getToken(Intent intent) {
        String receiver = intent.getStringExtra("receiver");
        if (!DISPENSED_TOKENS.keySet().contains(receiver)) {
            String token = getRandomToken(TOKEN_LENGTH);
            DISPENSED_TOKENS.put(receiver, token);
        }
        return DISPENSED_TOKENS.get(receiver);
    }

    private static String getRandomToken(int count) {
        String token = "";
        while (count-- > 0) {
            token += (CHARACTERS.charAt((int)(Math.random() * CHARACTERS.length())));
        }
        return token;
    }

}
