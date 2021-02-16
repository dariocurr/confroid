package fr.uge.confroidutils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.util.Consumer;
import fr.uge.confroidutils.services.TokenPuller;

import java.util.List;

public class ConfroidUtils {

    private static int requestId = 1;

    void saveConfiguration(Context context, String name, Object object, String versionName) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("tag", versionName);
        bundle.putString("token", TokenPuller.getToken());
        //bundle.putBundle("content", fromObjectToBundle(object, 1));
        Intent intent = new Intent();
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
        intent.putExtra("bundle", bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    <T> void loadConfiguration(Context context, String version, Consumer<T> callback) {
        TokenPuller.askToken(context);
        Intent intent = new Intent();
        intent.putExtra("name", context.getPackageName());
        intent.putExtra("token", TokenPuller.getToken());
        intent.putExtra("requestId", requestId++);
        intent.putExtra("version", version);
        intent.putExtra("receiver", "fr.uge.confroidutils.services.ConfigurationPuller");
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        // WAIT FOR INTENT
        // TODO use consumer
    }

    <T> void subscribeConfiguration(Context context, Consumer<T> callback) {
        Intent intent = new Intent();
        intent.putExtra("name", context.getPackageName());
        intent.putExtra("token", TokenPuller.getToken());
        intent.putExtra("requestId", requestId++);
        intent.putExtra("version", "latest");
        intent.putExtra("receiver", "fr.uge.confroidutils.services.ConfigurationPuller");
        intent.putExtra("expiration", Integer.MAX_VALUE);
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        // TODO use consumer
    }

    <T> void cancelConfigurationSubscription(Context context, Consumer<T> callback) {
        Intent intent = new Intent();
        intent.putExtra("name", context.getPackageName());
        intent.putExtra("token", TokenPuller.getToken());
        intent.putExtra("requestId", requestId++);
        intent.putExtra("version", "latest");
        intent.putExtra("receiver", "fr.uge.confroidutils.services.ConfigurationPuller");
        intent.putExtra("expiration", -1);
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        // TODO use consumer
    }

    void getConfigurationVersions(Context context, String name, Consumer<List<Version>> callback) {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("token", TokenPuller.getToken());
        intent.putExtra("requestId", requestId++);
        intent.putExtra("receiver", "fr.uge.confroidutils.services.ConfigurationVersions");
        intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationVersions");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        // TODO use consumer
    }

    <T> void editObject (Context context, T originalObject, Consumer <T> callback) {}

    <T> void updateObject (Context context, String name, String versionName, Consumer <T> callback) {}

}
