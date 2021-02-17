package fr.uge.confroidutils.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import fr.uge.confroidutils.ConfroidUtils;

public class ConfigurationVersions extends Service {

    private static ConfroidUtils CONFROID_UTILS;

    public static void setConfroidUtils(ConfroidUtils confroidUtils) {
        ConfigurationVersions.CONFROID_UTILS = confroidUtils;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CONFROID_UTILS.onReceiveConfigurationVersions(intent);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}