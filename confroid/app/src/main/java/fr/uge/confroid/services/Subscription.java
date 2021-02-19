package fr.uge.confroid.services;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Subscription {

    private final String subscriber;
    private final long creationTime;
    private final int expirationTime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return subscriber.equals(that.subscriber);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(subscriber);
    }
}
