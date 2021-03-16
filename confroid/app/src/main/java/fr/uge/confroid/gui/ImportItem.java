package fr.uge.confroid.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ImportItem {
    private String name;
    private boolean selected;
    private String assetLocation;
    private transient Bitmap cachedBitmap;
    private JSONObject jsonObject;

    /**
     * @param name
     * @param jsonObject
     */
    public ImportItem(String name, JSONObject jsonObject) {
        this.name = name;
        this.selected = false;
        this.assetLocation = "logo.png";
        this.jsonObject = jsonObject;
    }

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return selected
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return assetlocation
     */
    public String getAssetLocation() {
        return this.assetLocation;
    }

    /**
     * @param assetLocation
     */
    public void setAssetLocation(String assetLocation) {
        this.assetLocation = assetLocation;
    }

    /**
     * @return jsonObject
     */
    public JSONObject getJson() {
        return jsonObject;
    }

    /**
     * @param jsonObject
     */
    public void setJson(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * @param context
     * @return Bitmap
     */
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
