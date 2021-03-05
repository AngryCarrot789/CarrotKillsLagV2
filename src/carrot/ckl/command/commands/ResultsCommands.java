package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.IExecutableSubCommands;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.PlayerResultsTable;
import carrot.ckl.player.tables.ResultsCollection;
import carrot.ckl.player.tables.base.ITeleportable;
import carrot.ckl.player.tables.base.ResultRow;
import carrot.ckl.player.tables.base.ResultsSection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.TreeMap;

public class ResultsCommands extends IExecutableSubCommands {
    public ResultsCommands() {
        subCommandMap = new TreeMap<String, IExecutableCommand>();
        subCommandMap.put("show", new ShowSubCommand());
        subCommandMap.put("tp", new TeleportSubCommand());
        subCommandMap.put("clear", new ClearSubCommand());
        subCommandMap.put("remove", new RemoveSubCommand());
        subCommandMap.put("advlookup", new AdvancedLookupSubCommand());
    }

    @Override
    public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
        ParsedValue<String> parsedSubCommand = ArgumentParser.ParseString(args, 0);
        if (args.length == 0 || parsedSubCommand.failed()) {
            displayHelp(logger);
            return;
        }

        String[] subArgs = ArgumentParser.GetSubCommandArgs(args).toArray(new String[0]);
        String command = parsedSubCommand.value();

        IExecutableCommand executableCommand = subCommandMap.get(command);
        if (executableCommand == null) {
            logger.LogInfo(
                    "That sub-command doesn't exist. " +
                    "Do " + ChatColor.GREEN + "/ckl items" + ChatColor.GOLD +
                    " for a list of sub-commands");
        }
        else {
            executableCommand.Execute(sender, logger, subArgs);
        }
    }

    @Override
    public void displayHelp(ChatLogger logger) {
        logger.LogInfo("Avaliable commands: ");
        logger.LogAny(ChatFormatting.FormatCommand("results show", "<page>", "Shows " + PlayerResultsTable.ShowResultsAmount + " results on the given page"));
        logger.LogAny(ChatFormatting.FormatCommand("results tp", "<results number>", "Teleports you a result (if it contains coordinates)"));
        logger.LogAny(ChatFormatting.FormatCommand("results clear", "", "Clears all the search results"));
        logger.LogAny(ChatFormatting.FormatCommand("results remove", "<(p:r)> <(page number:result number)> ",
                                                   "Removes the specific result, or the specific page. " +
                                                   "The result numbers move down as you remove results, " +
                                                   "To remove results 2, 5, 9, you should remove 9, then 5 then 2. "));
        logger.LogAny(ChatFormatting.FormatCommand("results advlookup",
                                                   "<look for> <ignore capitals: t/f>",
                                                   "(Replace spaces with _ in <look for>) Goes through every result and checks if the content " +
                                                   "contains <look for>. It then clears the old results and adds the results that were found"));
    }

    private static class ShowSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ResultsCollection resultsCollection = PlayerResultsTable.getInstance().getResults(sender.getName());
            ParsedValue<Integer> pageNumber = ParsedValue.ParseInteger(args, 0);
            if (pageNumber.failed()) {
                pageNumber.setValue(1);
            }

            ResultsSection section = resultsCollection.getPage(pageNumber.value() - 1);
            section.writeNumbered(logger);
        }
    }

    private static class TeleportSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> result = ParsedValue.ParseInteger(args, 0);
            if (!(sender instanceof Player)) {
                logger.LogInfo("You're not a player... mr console ;)");
                return;
            }
            ResultsCollection resultsCollection = PlayerResultsTable.getInstance().getResults(sender.getName());
            if (result.failed() || result.value() < 1) {
                result.setValue(1);
            }

            if (result.value() > resultsCollection.resultsCount()) {
                logger.LogInfo("That result doesn't exist! The number is too big");
                return;
            }

            ResultRow row = resultsCollection.getRows().get(result.value() - 1);
            if (row instanceof ITeleportable) {
                ((ITeleportable) row).teleport((Player) sender);
            }
            else {
                logger.LogInfo("Cannot teleport to that result!");
            }
        }
    }

    private static class ClearSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ResultsCollection resultsCollection = PlayerResultsTable.getInstance().getResults(sender.getName());
            ParsedValue<Integer> pageNumber = ParsedValue.ParseInteger(args, 0);
            if (pageNumber.failed()) {
                resultsCollection.clearResults();
                resultsCollection.updateHeader("No Results to be displayed");
            }
        }
    }

    private static class RemoveSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<String> type = ParsedValue.ParseString(args, 0);
            ParsedValue<Integer> number = ParsedValue.ParseInteger(args, 1);

            ResultsCollection resultsCollection = PlayerResultsTable.getInstance().getResults(sender.getName());
            if (type.failed()) {
                logger.LogInfo("Error with the type. Can be either 'p' or 'r' (for page or result)");
                return;
            }
            if (type.value().equalsIgnoreCase("p")) {
                resultsCollection.removePage(number.value() - 1);
                logger.LogInfo("Removed page " + ChatColor.GREEN + number.value());
            }
            else if (type.value().equalsIgnoreCase("r")) {
                resultsCollection.getRows().remove(number.value() - 1);
                logger.LogInfo("Removing result " + ChatColor.GREEN + number.value());
            }
            else {
                logger.LogInfo("That type is unknown");
            }
        }
    }

    private static class AdvancedLookupSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<String> lookup = ParsedValue.ParseString(args, 0);
            ParsedValue<Boolean> ignoreCases = ParsedValue.ParseBoolean(args, 1);
            boolean toLower = ignoreCases.failed() || ignoreCases.value();
            if (lookup.failed()) {
                logger.LogInfo("Error with the lookup content");
                return;
            }

            String lookupContent = lookup.value().replace('_', ' ');
            if (toLower) {
                lookupContent = lookupContent.toLowerCase();
            }

            ResultsCollection resultsCollection = PlayerResultsTable.getInstance().getResults(sender.getName());
            ArrayList<ResultRow> resultsCopy = new ArrayList<ResultRow>(resultsCollection.getRows());
            for(ResultRow row : resultsCopy) {
                String content = toLower ? row.getContent().toLowerCase() : row.getContent();
                if (content.contains(lookupContent)) {
                    resultsCollection.addRow(row);
                }
            }
        }
    }
}
