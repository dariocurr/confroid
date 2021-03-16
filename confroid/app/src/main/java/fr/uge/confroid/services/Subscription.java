package fr.uge.confroid.services;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Subscription {

    private final String subscriber;
    private final long creationTime;
    private final int expirationTime;

    /**
     * @param subscriber
     * @param expirationTime
     * contructor
     */
    public Subscription(String subscriber, int expirationTime) {
        this.subscriber = subscriber;
        this.creationTime = System.currentTimeMillis();
        this.expirationTime = expirationTime;
    }

    /**
     * @param currentTime
     * @return boolean expired or not
     */
    public boolean isExpired(long currentTime) {
        return ((currentTime - this.creationTime) / 1000) > this.expirationTime;
    }

    /**
     * @return give subscriber
     */
    public String getSubscriber() {
        return subscriber;
    }

    /**
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return subscriber.equals(that.subscriber);
    }

    /**
     * hashcode
     * @return hash
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(subscriber);
    }
}
