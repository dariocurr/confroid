package fr.uge.confroid.web;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uge.confroid.MainActivity;
import fr.uge.confroid.R;
import fr.uge.confroid.utlis.ConfroidManagerUtils;
import fr.uge.confroid.utlis.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    Button registerBtn;
    Button loginBtn;
    EditText username;
    EditText password;
    boolean auth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);

        registerBtn = findViewById(R.id.register_page_button);
        registerBtn.setOnClickListener(b -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(b -> {
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            if(!usernameText.equals("") && !passwordText.equals("")) {
                try {
                    File database = new File(this.getFilesDir(),"database.json");
                    JSONObject databaseObj = new JSONObject(FileUtils.readFile(database));

                    JSONArray users = databaseObj.getJSONArray("users");

                    for (int i = 0 ; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        if(user.getString("username").equals(usernameText) && user.getString("password").equals(passwordText)){
                            auth = true;
                        }
                    }

                    if(!auth){
                        Toast alert = Toast.makeText(this, "Username or Password Incorrect! Retry", 2000);
                        alert.show();
                    }
                    else{
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("username", usernameText);
                        intent.putExtra("password", passwordText);
                        intent.putExtra("auth", "true");
                        startActivity(intent);
                    }

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
}