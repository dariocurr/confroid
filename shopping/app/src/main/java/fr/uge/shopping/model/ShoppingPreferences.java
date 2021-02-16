package fr.uge.shopping.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShoppingPreferences {

    public Map<String, ShoppingInfo> shoppingInfo;

    public ShoppingPreferences() {
        this.shoppingInfo = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingPreferences that = (ShoppingPreferences) o;
        return shoppingInfo.equals(that.shoppingInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(shoppingInfo);
    }

    @Override
    public String toString() {
        return "ShoppingPreferences{" +
                "shoppingInfo=" + shoppingInfo +
                '}';
    }
}

