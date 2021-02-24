package fr.uge.confroid.receivers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidManagerUtils;

public class TokenDispenser extends BroadcastReceiver {

    private static MessageDigest MESSAGE_DIGEST;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            Log.e("SHA-256 not defined!", "");
        }
    }

    public static String getToken(String receiver) {
        MESSAGE_DIGEST.update(receiver.getBytes());
        return new String(MESSAGE_DIGEST.digest());
    }

    @Override
    public void onReceive(Context context, Intent incomingIntent) {
        String receiver = incomingIntent.getStringExtra("receiver");
        String appPackage = incomingIntent.getStringExtra("appPackage");
        Intent outgoingIntent = new Intent();
        outgoingIntent.setClassName(appPackage, receiver);//fr.uge.confroidutils - fr.uge.shopping
        outgoingIntent.putExtra("token", getToken(appPackage));
        Log.i("TOKENREC", receiver);
        context.startService(outgoingIntent);
    }

}
