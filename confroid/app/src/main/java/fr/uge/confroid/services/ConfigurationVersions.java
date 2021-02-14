package fr.uge.confroid.services;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import fr.uge.confroid.ConfroidManager;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.receivers.TokenDispenser;
import fr.uge.confroid.utlis.ConfroidManagerUtils;

public class ConfigurationVersions extends Service {

    @Override
    public int onStartCommand(Intent incomingIntent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = createNotificationChannel("my_service", "My Background Service");
            Notification notification = new NotificationCompat.Builder(this, channelId).build();
            startForeground(1337, notification);
        }

        String name = incomingIntent.getStringExtra("name");
        String token = incomingIntent.getStringExtra("token");
        if (TokenDispenser.getToken(ConfroidManagerUtils.getPackageName(name)).equalsIgnoreCase(token)) {
            String requestId = incomingIntent.getStringExtra("requestId");
            String receiver = incomingIntent.getStringExtra("receiver");
            Intent outgoingIntent = new Intent();
            Bundle content;
            content = ConfroidManager.loadAllVersionsBundle(this.getApplicationContext(), name);
            outgoingIntent.putExtra("name", name);
            outgoingIntent.putExtra("requestId", requestId);
            outgoingIntent.putExtra("versions", content);
            outgoingIntent.setClassName(ConfroidManagerUtils.getPackageName(receiver), receiver);
            this.startService(outgoingIntent);
        } else {
            Log.e("TokenNotValidException","Token " + token + " isn't valid!");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
        return START_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
