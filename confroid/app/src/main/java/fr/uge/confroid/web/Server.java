package fr.uge.confroid.web;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import fr.uge.confroid.utlis.FileUtils;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Server {

    static JSONObject json;
    private MockWebServer server;
    private HttpUrl serverUrl;

    public Server() {

    }

    public void start() throws IOException {
        this.server = new MockWebServer();
        //this.server.enqueue(new MockResponse().setBody("you found me"));
        this.server.start();
    }

    public HttpUrl getUrl(){
        this.serverUrl = this.server.url("/");
        return serverUrl;
    }

    public void saveRequest() throws InterruptedException, JSONException {
        Log.e("SAVE","SAVE");
        //this.json = new JSONObject(String.valueOf(Objects.requireNonNull(this.server.takeRequest(1, TimeUnit.SECONDS)).getBody()));
        RecordedRequest request = server.takeRequest();
        //Log.e("body: ", request.getUtf8Body());
        json = new JSONObject(request.getUtf8Body());
        Log.e("SAVE","1");
        Log.e("JSONFILE", json.toString());
    }

    public final JSONObject getJson() {
        return json;
    }
}