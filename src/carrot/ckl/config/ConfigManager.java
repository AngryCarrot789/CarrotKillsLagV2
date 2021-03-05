package carrot.ckl.config;

import carrot.ckl.CarrotKillsLag;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;

public class ConfigManager {
    public static Config mainConfig;
    public static Config chunkConfig;

    public static void initialise() {
        try {
            mainConfig = new Config(CarrotKillsLag.getInstance(), "config.yml");
        }
        catch (Exception e) {
            logFailedInitialiseConfig("mainConfig.yml");
            e.printStackTrace();
        }
        try {
            chunkConfig = new Config(CarrotKillsLag.getInstance(), "chunkConfig.yml");
        }
        catch (Exception e) {
            logFailedInitialiseConfig("chunkConfig.yml");
            e.printStackTrace();
        }

        reloadMainConfig();
        reloadChunkConfig();
    }

    public static void reloadMainConfig() {
        try {
            mainConfig.Load();
        }
        catch (Exception e) {
            logFailedLoadConfig("config.yml");
            e.printStackTrace();
        }
    }

    public static void reloadChunkConfig() {
        try {
            chunkConfig.Load();
        }
        catch (Exception e) {
            logFailedLoadConfig("chunkConfig.yml");
            e.printStackTrace();
        }
    }

    private static void logFailedInitialiseConfig(String config) {
        ChatLogger.LogPlugin("Failed to initialise " + ChatFormatting.Apostrophise(config));
    }

    private static void logFailedLoadConfig(String config) {
        ChatLogger.LogPlugin("Failed to load " + ChatFormatting.Apostrophise(config));
    }
}
