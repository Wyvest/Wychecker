package net.wyvest.wychecker.utils;

import club.sk1er.mods.core.util.Multithreading;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class APICaller {
    public static String version;

    public static void getVersion() {
        OkHttpClient client = new OkHttpClient();
        Multithreading.runAsync(() -> {
            Request request = new Request.Builder()
                    .url("https://wyvest.net/checker.json")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        String myRespones = response.body().string();
                        if (!myRespones.contains("error")) {
                            JsonResponse Jresponse = new Gson().fromJson(myRespones, JsonResponse.class);
                            version = Jresponse.version;
                        }
                    }
                }
            });
        });
    }
}
