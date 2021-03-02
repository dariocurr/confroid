package fr.uge.shopping.gui;

import fr.uge.shopping.model.ShoppingInfo;

public class ConfigurationItem {
    private String name;
    private ShoppingInfo info;

    public ConfigurationItem(String name, ShoppingInfo info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShoppingInfo getInfo() {
        return info;
    }

    public void setInfo(ShoppingInfo info) {
        this.info = info;
    }
}
