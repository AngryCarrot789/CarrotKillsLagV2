package carrot.ckl.command;

import carrot.ckl.PermissionsHelper;
import carrot.ckl.command.commands.*;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.SortedMap;
import java.util.TreeMap;

public class MainCommandExecutor implements CommandExecutor {
    public ChatLogger logger = new ChatLogger(null);

    private final SortedMap<String, IExecutableCommand> CommandMap;

    public MainCommandExecutor() {
        CommandMap = new TreeMap<String, IExecutableCommand>();
        CommandMap.put("help", new HelpCommand());
        CommandMap.put("result", new ResultsCommands());
        CommandMap.put("chunk", new ChunkCommands());
        CommandMap.put("tile", new TileCommands());
        CommandMap.put("getid", new LookingAtBlockCommand());
        CommandMap.put("config", new ConfigCommand());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        if (!PermissionsHelper.IsConsoleOrHasPermsOrOp(sender, "carrot.ckl")) {
            sender.sendMessage(ChatColor.RED + "You dont have access to these commands");
            return true;
        }
        logger.setSender(sender);
        if (strings.length == 0) {
            logger.LogInfo("------------ CarrotKillsLag v2 ------------");
            logger.LogInfo(ChatColor.GREEN + "  A plugin for reducing lag");
            logger.LogInfo(ChatColor.BLUE + "  Version 1.0 - made by Carrot/Ther");
            logger.LogInfo("  Do " + ChatColor.GREEN + "/ckl " + ChatColor.DARK_GREEN + "help" + ChatColor.GOLD + " to display help");
            logger.LogInfo("-------------------------------------------");
        }
        else {
            String command = strings[0];
            IExecutableCommand executableCommand = CommandMap.get(command);
            if (executableCommand == null) {
                logger.LogInfoWithPrefix("That command does not exist");
            }
            else {
                String[] args = ArgumentParser.GetCommandArgs(strings).toArray(new String[0]);
                try {
                    executableCommand.Execute(sender, logger, args);
                }
                catch (Exception e) {
                    ChatLogger.LogPlugin("Exception while executing command " + ChatFormatting.Apostrophise(command));
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
