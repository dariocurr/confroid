package fr.uge.confroid.web;

import okhttp3.*;

import java.io.IOException;

public class Client {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(HttpUrl url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String loginJson(String username, String password) {
        return "{'user':["
                + "{'name':'" + username + "',"
                + "'password':'" + password +"'"
                + "}"
                + "]}";
    }


}