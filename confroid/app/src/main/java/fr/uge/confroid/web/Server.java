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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {

    private static List<JSONObject> configurations;
    private MockWebServer server;
    private HttpUrl serverUrl;

    public Server() {
        configurations = new ArrayList<>();
    }

    /**
     * @throws IOException
     */
    public void start() throws IOException {
        this.server = new MockWebServer();
        this.server.enqueue(new MockResponse().setBody("you found me"));

        //server.url calls server.start() and it starts the server
        this.serverUrl = this.server.url("/");
        //this.server.start();
    }

    /**
     * @return url of server
     */
    public HttpUrl getUrl(){
        return serverUrl;
    }

    /**
     * @throws InterruptedException
     * @throws JSONException
     */
    public void saveConfiguration() throws InterruptedException, JSONException {
        //this.json = new JSONObject(String.valueOf(Objects.requireNonNull(this.server.takeRequest(1, TimeUnit.SECONDS)).getBody()));
        RecordedRequest request = server.takeRequest();
        //Log.e("body: ", request.getUtf8Body());

        JSONObject json = new JSONObject(request.getUtf8Body());
        configurations.add(json);
        Log.e("JSONFILE", json.toString());
    }

    /**
     * @return list of configurations
     */
    public final List<JSONObject> getConfigurations() {
        return configurations;
    }
}