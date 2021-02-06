package fr.uge.confroid.services;

import android.content.Intent;
import android.os.Bundle;

public class ConfigurationPusher {

    public void pushConfiguration(Intent intent) {

        String name = intent.getStringExtra("name");
        Bundle bundle = intent.getBundleExtra("content");
        String tag =intent.getStringExtra("tag");

    };

}
