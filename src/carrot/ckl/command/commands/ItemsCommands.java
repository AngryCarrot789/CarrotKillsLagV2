package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemsCommands implements IExecutableCommand {
    @Override
    public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
        ParsedValue<String> parsedSubCommand = ArgumentParser.ParseString(args, 0);
        if (args.length == 0 || parsedSubCommand.failed()) {
            logger.LogInfo("Avaliable commands: ");
            logger.LogAny(ChatFormatting.FormatCommand("load", "<x> <z> <radius> [world]", "Force loads chunks at and around the given coords"));
            logger.LogAny(ChatFormatting.FormatCommand("unload", "<x> <z> <radius> [world]", "Force unloads chunks at and around the given coords"));
            logger.LogAny(ChatFormatting.FormatCommand("loaded", "<x> <z> [world]", "Checks if the chunk at X and Z is loaded"));
            logger.LogAny(ChatFormatting.FormatCommand("tp", "<x> <z>", "Teleports you to the center of a chunk (safest Y pos)"));
            logger.LogAny(ChatFormatting.FormatCommand("pos", "", "Gets the chunk position of the chunk you're in"));
            return;
        }

        String[] subArgs = ArgumentParser.GetSubCommandArgs(args).toArray(new String[0]);
        String command = parsedSubCommand.value();
        if (command.equalsIgnoreCase("pos")) {
            if (!(sender instanceof Player)) {
                logger.LogInfo("You're not a player... mr console -_-");
            }
            else {
                Player player = (Player) sender;
                Location location = player.getLocation();
                int chunkX = location.getBlockX() >> 4;
                int chunkZ = location.getBlockZ() >> 4;
                logger.LogInfo("Chunk Position: " + ChatFormatting.ColouredXZ(chunkX, chunkZ));
            }
        }
        else {
            logger.LogInfo(
                    "That sub-command doesn't exist. " +
                    "Do " + ChatColor.GREEN + "/ckl items" + ChatColor.GOLD +
                    " for a list of sub-commands");
        }
    }
}
