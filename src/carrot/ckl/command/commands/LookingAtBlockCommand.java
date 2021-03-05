package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.logs.ChatLogger;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LookingAtBlockCommand implements IExecutableCommand {
    @Override
    public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!(sender instanceof Player)) {
            logger.LogInfo("You're not a player... mr console -_-");
            return;
        }

        ParsedValue<Integer> distance = ArgumentParser.ParseInteger(args, 0);
        if (distance.failed()) {
            distance.setValue(50);
        }

        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, distance.value());
        if (block == null || block.isEmpty()) {
            logger.LogInfo("You're not looking at anything, just air. Add a distant on the end of the command to look further");
        }
        else {
            logger.LogInfo("Block ID: " + ChatColor.GREEN + block.getTypeId() + ChatColor.GOLD + ", Block Data: " + ChatColor.LIGHT_PURPLE + block.getData());
        }
    }
}
