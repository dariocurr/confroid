package fr.uge.confroid.web;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.R;
import fr.uge.confroid.utlis.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    EditText username;
    EditText password;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username_register);
        password = findViewById(R.id.password_register);

        registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(b -> {

            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();

            if(!usernameText.equals("") && !passwordText.equals("")){
                File database = new File(getFilesDir(), "web.database.json");
                String accountsJson = FileUtils.readFile(database);
                try {
                    Log.e("ACCOUNTS", accountsJson);
                    JSONObject accounts = new JSONObject(accountsJson);
                    JSONArray users = accounts.getJSONArray("users");

                    JSONObject newUser = new JSONObject();
                    newUser.put("username", usernameText);
                    newUser.put("password", passwordText);

                    users.put(newUser);

                    File databaseFinal = new File(this.getFilesDir(),"web.database.json");
                    FileUtils.writeFile(databaseFinal, accounts.toString());

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast alert = Toast.makeText(this, "Make sure to write username and password!", 2000);
                alert.show();
            }
        });
    }

    /**
     * @param inFile
     * @return
     */
    public String loadDataFromAssets(String inFile) {
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