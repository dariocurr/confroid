package fr.uge.shopping.model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingPreferences {

    public Map<String, ShoppingInfo> shoppingInfo;

    public ShoppingPreferences() {
        this.shoppingInfo = new HashMap<>();
    }

}

