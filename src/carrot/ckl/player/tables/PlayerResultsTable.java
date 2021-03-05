package carrot.ckl.player.tables;

import carrot.ckl.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class PlayerResultsTable {
    private final HashMap<String, ResultsCollection> resultsCollection;
    private static PlayerResultsTable instance;

    private static ConfigurationSection resultsSection;
    public static int ShowResultsAmount = 12;

    public PlayerResultsTable() {
        instance = this;
        resultsCollection = new HashMap<String, ResultsCollection>();
        fetchConfigInformation();
    }

    public static void fetchConfigInformation() {
        resultsSection = ConfigManager.mainConfig.getConfigurationSection("results");
        ShowResultsAmount = resultsSection.getInt("display results amount");
    }

    public ResultsCollection getResults(String name) {
        ResultsCollection collection = resultsCollection.get(name);
        if (collection == null) {
            collection = new ResultsCollection();
            resultsCollection.put(name, collection);
        }
        return collection;
    }

    public void updateResults(String name, ResultsCollection collection) {
        resultsCollection.put(name, collection);
    }

    public static PlayerResultsTable getInstance() {
        return instance;
    }
}
