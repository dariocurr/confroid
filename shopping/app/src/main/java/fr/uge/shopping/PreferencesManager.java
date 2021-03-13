package fr.uge.shopping;

import android.content.Context;
import fr.uge.confroidutils.ConfroidUtils;
import fr.uge.shopping.model.BillingDetails;
import fr.uge.shopping.model.ShippingAddress;
import fr.uge.shopping.model.ShoppingInfo;
import fr.uge.shopping.model.ShoppingPreferences;

import java.util.Map;

public class PreferencesManager {
    private ShoppingPreferences preferences;
    private ConfroidUtils confroidUtils;

    private static PreferencesManager preferencesManager;

    public static PreferencesManager getPreferencesManager(Context context) {
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    private PreferencesManager(Context context) {
        this.confroidUtils = new ConfroidUtils(context);
        this.preferences = new ShoppingPreferences();
    }

    public void init() {
        ShippingAddress address = new ShippingAddress();
        BillingDetails billing = new BillingDetails();
        preferences.shoppingInfo.put("default", new ShoppingInfo(address, billing, true));
    }

    public ShoppingPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(ShoppingPreferences preferences) {
        this.preferences = preferences;
    }

    public Map<String, ShoppingInfo> getShoppingInfoMap() {
        return preferences.shoppingInfo;
    }

    public ShoppingInfo getShoppingInfo(String key) {
        return preferences.shoppingInfo.get(key);
    }

    public void removeShoppingInfo(String key) {
        this.preferences.shoppingInfo.remove(key);
    }

    public ConfroidUtils api() {
        return this.confroidUtils;
    }
}
