package fr.uge.confroid.web;

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.json.JSONException;

import java.io.IOException;

public class NetworkThread implements Runnable {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public NetworkThread(String json) throws JSONException, IOException, InterruptedException {
        Server server = new Server();
        server.start();
        Client client = new Client();
        String postLogin = client.loginJson("toto","toto");
        server.saveRequest();
    }

    @Override
    public void run() {

    }
}
