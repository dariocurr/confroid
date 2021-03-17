package fr.uge.confroid.web;

import android.util.Log;
import okhttp3.*;

import java.io.IOException;

public class Client {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    /**
     * @param url
     * @param json
     * @return body string
     * @throws IOException
     */
    public String post(HttpUrl url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try  {
            Response response = client.newCall(request).execute();
            Log.e("test","CCCCCCCCCCCCCCCCCCCCCc");
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param username
     * @param password
     * @return json string with name and password
     */
    String loginJson(String username, String password) {
        return "{'user':["
                + "{'name':'" + username + "',"
                + "'password':'" + password +"'"
                + "}"
                + "]}";
    }


}