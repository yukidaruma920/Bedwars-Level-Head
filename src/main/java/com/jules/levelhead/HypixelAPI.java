package com.jules.levelhead;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class HypixelAPI {

    private static final String API_URL = "https://api.hypixel.net/player?key=%s&uuid=%s";
    private static final Map<UUID, PlayerData> playerDataCache = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();

    public static CompletableFuture<PlayerData> getPlayerData(UUID uuid) {
        if (playerDataCache.containsKey(uuid)) {
            PlayerData data = playerDataCache.get(uuid);
            if (System.currentTimeMillis() - data.getTimestamp() < 600000) { // 10 minute cache
                return CompletableFuture.completedFuture(data);
            }
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(String.format(API_URL, LevelHeadConfig.apiKey, uuid.toString()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                reader.close();

                if (json.has("player")) {
                    PlayerData data = new PlayerData(json.getAsJsonObject("player"));
                    playerDataCache.put(uuid, data);
                    return data;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public static class PlayerData {
        private final JsonObject playerJson;
        private final long timestamp;

        public PlayerData(JsonObject playerJson) {
            this.playerJson = playerJson;
            this.timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }

        public int getBedwarsLevel() {
            if (playerJson.has("achievements") && playerJson.getAsJsonObject("achievements").has("bedwars_level")) {
                return playerJson.getAsJsonObject("achievements").get("bedwars_level").getAsInt();
            }
            return 0;
        }
    }
}
