package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.config.ConfigManager;
import carrot.ckl.logs.ChatLogger;
import org.bukkit.command.CommandSender;

public class ConfigCommand implements IExecutableCommand {
    @Override
    public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
        ParsedValue<String> command = ArgumentParser.ParseString(args, 0);
        if (command.failed()) {
            logger.LogInfo("Error with the config command. You can only reload atm");
            return;
        }

        ParsedValue<String> configName = ArgumentParser.ParseString(args, 1);
        if (configName.failed()) {
            logger.LogInfo("Error with the config name. there's mainConfig, chunkConfig");
            return;
        }

        if (command.value().equalsIgnoreCase("reload")) {
            String config = configName.value();
            if (config.equalsIgnoreCase("mainConfig")) {
                ConfigManager.reloadMainConfig();
            }
            else if (config.equalsIgnoreCase("chunkConfig")) {
                logger.LogInfo("Reloading the chunk config...");
                ConfigManager.reloadChunkConfig();
                ChunkCommands.fetchConfigInformation();
                logger.LogInfo("Reloaded!");
            }
            else {
                logger.LogInfo("That config doesn't exist");
            }
        }
    }
}
