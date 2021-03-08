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
    private JSONObject database;
    private File databaseFile;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Server() throws JSONException {
        this.databaseFile = new File("./database.json");
        if (databaseFile.exists())
            Log.e("file", "il file esiste");
        else Log.e("file", "il file non esiste");
        Log.e("fileeeee:", String.valueOf(databaseFile.length()));
        //FileUtils.writeFile(new File("database.json"),"ciao");
        //this.database = new JSONObject(Objects.requireNonNull(FileUtils.readFile(databaseFile)));
        Log.e("json:", String.valueOf(this.database));
    }

    public void start() throws IOException {
        this.server = new MockWebServer();
        this.server.enqueue(new MockResponse().setBody("you found me"));
        this.server.start();
    }

    public HttpUrl getUrl(){
        this.serverUrl = this.server.url("/");
        return serverUrl;
    }

    public void saveRequest() throws InterruptedException, JSONException {
        //this.json = new JSONObject(String.valueOf(Objects.requireNonNull(this.server.takeRequest(1, TimeUnit.SECONDS)).getBody()));
        RecordedRequest request = server.takeRequest();
        //Log.e("body: ", request.getUtf8Body());
        json = new JSONObject(request.getUtf8Body());
    }

    public final JSONObject getJson() {
        return json;
    }
}