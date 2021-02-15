package fr.uge.confroid.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class ImportItem {
    private String name;
    private boolean selected;
    private String assetLocation;
    private transient Bitmap cachedBitmap;

    public ImportItem(String name) {
        this.name = name;
        this.selected = false;
        this.assetLocation = "logo.png";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAssetLocation() {
        return assetLocation;
    }

    public void setAssetLocation(String assetLocation) {
        this.assetLocation = assetLocation;
    }

    public Bitmap getBitmap(Context context) {
        if (cachedBitmap == null) {
            InputStream is = null;
            try {
                is = context.getAssets().open(assetLocation);
                cachedBitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                try {
                    is.close();
                } catch (IOException e2) {
                }
            }
        }
        return cachedBitmap;
    }
}
