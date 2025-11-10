package com.yuki920.levelhead;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class LevelHeadConfig {

    private static Configuration config;

    public static boolean showOwnLevel = true;
    public static boolean adjustOwnLevel = false;
    public static boolean prestigeFormat = true;
    public static String apiKey = "";

    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    public static void loadConfig() {
        config.load();
        showOwnLevel = config.getBoolean("showOwnLevel", "general", true, "Show your own Level Head");
        adjustOwnLevel = config.getBoolean("adjustOwnLevel", "general", false, "Adjust your own Level Head position");
        prestigeFormat = config.getBoolean("prestigeFormat", "general", true, "Use prestige format for Level Head");
        apiKey = config.getString("apiKey", "general", "", "Your Hypixel API key");
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void saveConfig() {
        config.get("general", "showOwnLevel", true).set(showOwnLevel);
        config.get("general", "adjustOwnLevel", false).set(adjustOwnLevel);
        config.get("general", "prestigeFormat", true).set(prestigeFormat);
        config.get("general", "apiKey", "").set(apiKey);
        config.save();
    }
}
