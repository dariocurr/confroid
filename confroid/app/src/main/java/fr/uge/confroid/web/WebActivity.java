package fr.uge.confroid.web;

import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.R;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class WebActivity extends AppCompatActivity {

    Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Button connection = findViewById(R.id.start_connection_button);
        connection.setOnClickListener(b ->{
            Thread thread = new Thread(() -> {
                try {
                    String json = LoadData("database.json");
                    //Log.e("json",json);
                    server = new Server(json);
                    //Log.e("database",server.getDatabase().toString());
                    server.start();
                    Client client = new Client();
                    String postLogin = client.loginJson("toto","toto");
                    //server.saveRequest();
                    String response = client.post(server.getUrl(),postLogin);
                    //Log.e("risposta",response);
                } catch (JSONException | IOException  e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        });

        //Log.e("loginJson",client.loginJson("toto","toto") );


        /*String json = LoadData("database.json");
        try {
            NetworkThread thread = new NetworkThread(json);
            new Thread(thread).start();
        } catch (JSONException  | IOException | InterruptedException e ) {
            e.printStackTrace();
        }*/
    }

    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }
}