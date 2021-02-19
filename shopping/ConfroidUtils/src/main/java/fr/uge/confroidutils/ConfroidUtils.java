package fr.uge.confroidutils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.core.util.Consumer;
import fr.uge.confroidutils.converters.FromBundleToObjectConverter;
import fr.uge.confroidutils.converters.FromObjectToBundleConverter;
import fr.uge.confroidutils.services.ConfigurationPuller;
import fr.uge.confroidutils.services.ConfigurationVersions;
import fr.uge.confroidutils.services.TokenPuller;

import java.util.ArrayList;
import java.util.List;

public class ConfroidUtils {

    private static int requestId;
    private List<Consumer> callbacks;

    public ConfroidUtils(Context context) {
        TokenPuller.askToken(context);
        this.callbacks = new ArrayList<>();
        requestId = 1;
        ConfigurationPuller.setConfroidUtils(this);
        ConfigurationVersions.setConfroidUtils(this);
    }

    public void saveConfiguration(Context context, String name, Object object, String versionName) {
        String token = TokenPuller.getToken();
        if (token != null) {
            Bundle bundle = new Bundle();
            bundle.putString("name", context.getPackageName());
            bundle.putString("tag", name + "\\" + versionName);
            bundle.putString("token", token);
            bundle.putBundle("content", FromObjectToBundleConverter.convert(object));
            Intent intent = new Intent();
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPusher");
            intent.putExtra("bundle", bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } else {
            Log.e("Token Error", "Token didn't arrive yet");
        }
    }

    public <T> void loadConfiguration(Context context, String version, Consumer<T> callback) {
        String token = TokenPuller.getToken();
        if (token != null) {
            Intent intent = new Intent();
            intent.putExtra("name", context.getPackageName());
            intent.putExtra("token", token);
            intent.putExtra("requestId", requestId++);
            intent.putExtra("version", version);
            intent.putExtra("receiver", "fr.uge.confroidutils.services.ConfigurationPuller");
            intent.setClassName("fr.uge.confroid", "fr.uge.confroid.services.ConfigurationPuller");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
            this.callbacks.add(callback);
        } else {
            Log.e("Token Error", "Token didn't arrive yet");
        }
    }

    public <T> void subscribeConfiguration(Context context, Consumer<T> callback) {
        String token = TokenPuller.getToken();
        if (token != null) {
            Intent intent = new Intent();
            intent.putExtra("name", context.getPackageName());
            intent.putExtra("token", token);
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
            this.callbacks.add(callback);
        } else {
            Log.e("Token Error", "Token didn't arrive yet");
        }
    }

    public <T> void cancelConfigurationSubscription(Context context, Consumer<T> callback) {
        String token = TokenPuller.getToken();
        if (token != null) {
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
            this.callbacks.add(callback);
        } else {
            Log.e("Token Error", "Token didn't arrive yet");
        }
    }

    public void getConfigurationVersions(Context context, String name, Consumer<List<Version>> callback) {
        String token = TokenPuller.getToken();
        if (token != null) {
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
            this.callbacks.add(callback);
        } else {
            Log.e("Token Error", "Token didn't arrive yet");
        }
    }

    public <T> void editObject (Context context, T originalObject, Consumer <T> callback) {}

    public <T> void updateObject (Context context, String name, String versionName, Consumer <T> callback) {}

    public void onReceiveConfigurationPuller(Intent intent) {
        this.callbacks.get(0).accept(FromBundleToObjectConverter.convert(intent.getBundleExtra("content")));
        this.callbacks.remove(0);
    }

    public void onReceiveConfigurationVersions(Intent intent) {
        Bundle versionBundle = intent.getBundleExtra("versions");
        Log.e("versions", versionBundle.toString());
        //callback.accept();
        this.callbacks.remove(0);
    }

}
