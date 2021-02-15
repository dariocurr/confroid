package fr.uge.confroidutils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import fr.uge.confroidutils.services.TokenPuller;

import java.util.List;

public class ConfroidUtils {

    void saveConfiguration(Context context, String name, Object value, String versionName) {
        Bundle bundle = new Bundle();
        bundle.putString("name", context.getPackageName());
        bundle.putString("tag", "latest");
        bundle.putString("token", TokenPuller.getToken());
        //bundle.putBundle("content", contentBundle);
        Intent intent = new Intent();
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
        intent.putExtra("bundle", bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    <T> void loadConfiguration(Context context, String name, String version, Consumer<T> callback) {
        TokenPuller.askToken(context);
        Intent intent = new Intent();
        intent.putExtra("name", context.getPackageName());
        intent.putExtra("token", TokenPuller.getToken());
        intent.putExtra("requestId", "1");
        intent.putExtra("version", version);
        intent.putExtra("receiver", "fr.uge.confroidutils.services.ConfigurationPuller");
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        };
    }

    <T> void subscribeConfiguration(Context context, String name, Consumer<T> callback) {

    }

    <T> void cancelConfigurationSubscription(Context context, Consumer<T> callback) {

    }

    void getConfigurationVersions(Context context, String name, Consumer<List<Version>> callback) {

    }

}
