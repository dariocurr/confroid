package fr.uge.confroid;

import android.os.Bundle;

public class Configuration {

    public String version;
    public Bundle content;
    public String tag;

    public Configuration(String version, Bundle content, String tag) {
        this.version = version;
        this.content = content;
        this.tag = tag;
    }
}
