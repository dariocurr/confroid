package fr.uge.testconf.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import fr.uge.testconf.gui.ConfigurationVersionsActivity;

public class ConfigurationVersions extends Service {

    @Override
    public int onStartCommand(Intent incomingIntent, int flags, int startId) {
        Intent outComingIntent = new Intent(this.getApplicationContext(), ConfigurationVersionsActivity.class);
        outComingIntent.putExtra("versions", incomingIntent.getBundleExtra("versions"));
        startActivity(outComingIntent);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}