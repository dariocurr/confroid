package fr.uge.confroid.services;

import android.util.Log;

public class Subscription {

    private String subscriber;
    private long creationTime;
    private int expirationTime;

    public Subscription(String subscriber, int expirationTime) {
        this.subscriber = subscriber;
        this.creationTime = System.currentTimeMillis();
        this.expirationTime = expirationTime;
    }

    public boolean isExpired(long currentTime) {
        return ((currentTime - this.creationTime) / 1000) > this.expirationTime;
    }

    public String getSubscriber() {
        return subscriber;
    }
}
