package carrot.ckl.config;

import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Config extends YamlConfiguration {
    private File file;
    private Plugin plugin;
    private String name;

    public Config(Plugin plugin, String name) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.name = name;
        Load();
    }

    public void SaveConfig() throws IOException {
        save(this.file);
        ChatLogger.LogPlugin("Saved " + ChatFormatting.Apostrophise(file.getName()) + "!");
    }

    public boolean TrySave() {
        if (file == null) {
            ChatLogger.LogPlugin("Config file handle was null.... somehow");
            return false;
        }
        try {
            SaveConfig();
            ChatLogger.LogPlugin("Saved " + ChatFormatting.Apostrophise(file.getName()) + "!");
            return true;
        }
        catch (IOException ex) {
            ChatLogger.LogPlugin("Failed to save config " + ChatFormatting.Apostrophise(file.getName()));
            ex.printStackTrace();
            return false;
        }
    }

    public void Load() throws IOException, InvalidConfigurationException {
        ChatLogger.LogPlugin("Loading " + ChatFormatting.Apostrophise(name) + "...");
        this.file = new File(plugin.getDataFolder(), name);
        String formatName = ChatFormatting.Apostrophise(name);
        if (file.exists()) {
            load(this.file);
            ChatLogger.LogPlugin("Loaded!");
        }
        else {
            if (!plugin.getDataFolder().exists()) {
                ChatLogger.LogPlugin("Plugin data folder not found, Creating it");
                if (!plugin.getDataFolder().mkdir()) {
                    ChatLogger.LogPlugin("Failed to create directory!");
                    return;
                }
            }

            ChatLogger.LogPlugin("Trying to find a default " + formatName + "...");
            InputStream defaultConfig = plugin.getResource(name);
            if (defaultConfig == null) {
                ChatLogger.LogPlugin("Default config file not found, creating an empty config file");
                if (file.createNewFile()) {
                }
                else {
                    ChatLogger.LogPlugin("Failed to create empty config");
                }
            }
            else {
                ChatLogger.LogPlugin("Default " + formatName + " found! Saving to the data folder");
                plugin.saveResource(name, false);
                this.load(this.file);
                ChatLogger.LogPlugin("Loaded config!");
            }
        }
    }

    public boolean TryLoad() {
        try {
            Load();
            return true;
        }
        catch (Exception e) {
            ChatLogger.LogPlugin("Failed to save config " + ChatFormatting.Apostrophise(file.getName()));
            e.printStackTrace();
            return false;
        }
    }
}
