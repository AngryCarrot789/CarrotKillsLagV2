package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements IExecutableCommand {
    public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
        ParsedValue<Integer> page = ArgumentParser.ParseInteger(args, 0);
        if (page.failed()) {
            page.setValue(1);
        }

        switch (page.value()) {
            case 1:
                logger.LogInfo("Formats: " + ChatColor.GREEN + "<required> [optional] (options1:options2:option3)");
                logger.LogAny(ChatColor.GREEN + ChatFormatting.TitliseContent(" Help - Basics and Info ", 55));
                logger.LogAny(ChatFormatting.FormatCommand("help", "(page:command)", "Displays help about CKL on a specific page or about a command's sub-commands"));
                logger.LogAny(ChatFormatting.FormatMainCommand("results",
                                                               "Contains commands for manipulating search results," +
                                                               " such as showing, clearing, teleporting, etc"));
                logger.LogAny(ChatColor.GREEN + ChatFormatting.Repeat('-', 52));
                break;
            case 2:
                logger.LogAny(ChatColor.GREEN + ChatFormatting.TitliseContent(" Help - Advanced Commands 1 ", 55));
                logger.LogAny(ChatFormatting.FormatMainCommand("tile",
                                                               "Contains commands for finding TileEntities. Dirt, Pistons, " +
                                                               "Doors are NOT TileEntities. Pulverizers are"));
                logger.LogAny(ChatFormatting.FormatMainCommand("chunk",
                                                               "Contains commands for doing stuff with chunks, " +
                                                               "like loading, getting positions, etc"));
                logger.LogAny(ChatColor.GREEN + ChatFormatting.Repeat('-', 52));
                break;
            case 3:
                logger.LogAny(ChatColor.GREEN + ChatFormatting.TitliseContent(" Help - Internal Commands ", 55));
                logger.LogAny(ChatFormatting.FormatCommand("config", "<config name>", "Reloads a specifig config"));
                logger.LogAny(ChatColor.GREEN + ChatFormatting.Repeat('-', 52));
                break;
        }
    }
}
