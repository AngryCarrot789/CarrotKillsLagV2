package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.IExecutableSubCommands;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.config.ConfigManager;
import carrot.ckl.helpers.AreaSafety;
import carrot.ckl.helpers.MathsHelper;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.PlayerResultsTable;
import carrot.ckl.player.tables.ResultsCollection;
import carrot.ckl.player.tables.results.chunk.ChunkLoadedResultRow;
import carrot.ckl.worlds.CoolEffects;
import carrot.ckl.worlds.WorldHelper;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.TreeMap;

public class ChunkCommands extends IExecutableSubCommands {
    public static int minChunkX = -500;
    public static int maxChunkX = 500;
    public static int minChunkZ = -500;
    public static int maxChunkZ = 500;
    public static int maxRadius = 5;

    public ChunkCommands() {
        fetchConfigInformation();
        subCommandMap = new TreeMap<String, IExecutableCommand>();
        subCommandMap.put("load", new LoadSubCommand());
        subCommandMap.put("unload", new UnloadSubCommand());
        subCommandMap.put("loaded", new LoadedSubCommand());
        subCommandMap.put("tp", new TeleportSubCommand());
        subCommandMap.put("cpos", new ChunkPositionSubCommand());
        subCommandMap.put("rpos", new RegionPositionSubCommand());
    }

    public static void fetchConfigInformation() {
        minChunkX = ConfigManager.chunkConfig.getInt("minimum chunk X");
        maxChunkX = ConfigManager.chunkConfig.getInt("maximum chunk X");
        minChunkZ = ConfigManager.chunkConfig.getInt("minimum chunk Z");
        maxChunkZ = ConfigManager.chunkConfig.getInt("maximum chunk Z");
        maxRadius = ConfigManager.chunkConfig.getInt("maximum radius");
    }

    public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
        ParsedValue<String> parsedSubCommand = ParsedValue.ParseString(args, 0);
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
                    "Do " + ChatColor.GREEN + "/ckl chunk" + ChatColor.GOLD +
                    " for a list of sub-commands");
        }
        else {
            executableCommand.Execute(sender, logger, subArgs);
        }
    }

    @Override
    public void displayHelp(ChatLogger logger) {
        logger.LogInfo("Avaliable sub-commands: ");
        logger.LogAny(ChatFormatting.FormatCommand("load", "<x> <z> <radius> [world]", "Force loads chunks at and around the given coords"));
        logger.LogAny(ChatFormatting.FormatCommand("unload", "<x> <z> <radius:0> [world]", "Force unloads chunks at and around the given coords"));
        logger.LogAny(ChatFormatting.FormatCommand("loaded", "<x> <z> [world]", "Checks if the chunk at X and Z is loaded"));
        logger.LogAny(ChatFormatting.FormatCommand("tp", "<x> <z> [world]", "Teleports you to the center of a chunk (safest Y pos)"));
        logger.LogAny(ChatFormatting.FormatCommand("cpos", "", "Gets the chunk position you're at, or at the given X and Z coords"));
        logger.LogAny(ChatFormatting.FormatCommand("rpos", "", "Gets the region position you're at, or at the given X and Z coords"));
    }

    public static void forceChunks(
            CommandSender sender,
            ParsedValue<Integer> pX,
            ParsedValue<Integer> pZ,
            ParsedValue<Integer> pR,
            ParsedValue<String> pW,
            boolean load,
            ChatLogger logger) {
        World world = null;
        String loadString = load ? "load" : "unload";
        if (pX.failed()) {
            logger.LogInfo("Error with the X coordinates");
            return;
        }
        if (pZ.failed()) {
            logger.LogInfo("Error with the Z coordinates");
            return;
        }
        if (pR.failed()) {
            logger.LogInfo("Error with the radius");
            return;
        }
        else if (pR.value() > maxRadius) {
            logger.LogInfo("Chunk " + loadString + " radius is too big (max " + maxRadius + ")");
            return;
        }
        else if (pR.value() < 0) {
            logger.LogInfo("Negative radius... really? lol");
            return;
        }
        if (pW.failed()) {
            if (sender instanceof Player) {
                world = ((Player) sender).getWorld();
            }
            else {
                logger.LogInfo("The world is invalid/unloaded, and you're not a player");
                return;
            }
        }
        else {
            world = WorldHelper.GetWorldFromName(pW.value());
            if (world == null) {
                logger.LogInfo("The world doesn't exist, or is unloaded");
                return;
            }
        }

        int centerX = pX.value();
        int centerZ = pZ.value();
        int radius = pR.value();
        if (MathsHelper.isOutside(centerX, minChunkX, maxChunkX) || MathsHelper.isOutside(centerZ, minChunkZ, maxChunkZ)) {
            logger.LogInfo("Why are you trying to " + loadString + " chunks past the world border...");
            return;
        }

        ResultsCollection collection = PlayerResultsTable.getInstance().getResults(sender.getName());
        collection.clearResults();
        collection.updateHeader("Chunk Locations | Is Loaded");

        int total = 0;
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                total++;
                Chunk chunk = world.getChunkAt(x, z);
                if (chunk != null) {
                    try {
                        if (load) {
                            boolean didLoad = chunk.load();
                            collection.addRow(new ChunkLoadedResultRow(world, x, z, didLoad));
                        }
                        else {
                            boolean didUnload = chunk.unload();
                            collection.addRow(new ChunkLoadedResultRow(world, x, z, !didUnload));
                        }
                    }
                    catch (Exception e) {
                        ChatLogger.LogPlugin("Exception occoured while trying to " + loadString + " a chunk");
                        e.printStackTrace();
                    }
                }
            }
        }

        logger.LogInfo("Searched " + ChatColor.GREEN + total + ChatColor.GOLD + " chunks.");
        collection.displaySavedAndWrite(logger);
    }

    private static class LoadSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> x = ParsedValue.ParseInteger(args, 0);
            ParsedValue<Integer> z = ParsedValue.ParseInteger(args, 1);
            ParsedValue<Integer> r = ParsedValue.ParseInteger(args, 2);
            ParsedValue<String> w = ParsedValue.ParseString(args, 3);
            forceChunks(sender, x, z, r, w, true, logger);
        }
    }

    private static class UnloadSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> x = ParsedValue.ParseInteger(args, 0);
            ParsedValue<Integer> z = ParsedValue.ParseInteger(args, 1);
            ParsedValue<Integer> r = ParsedValue.ParseInteger(args, 2);
            ParsedValue<String> w = ParsedValue.ParseString(args, 3);
            forceChunks(sender, x, z, r, w, false, logger);
        }
    }

    private static class LoadedSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> parsedX = ParsedValue.ParseInteger(args, 0);
            ParsedValue<Integer> parsedZ = ParsedValue.ParseInteger(args, 1);
            ParsedValue<Integer> parsedRadius = ParsedValue.ParseInteger(args, 2);
            ParsedValue<String> parsedWorld = ParsedValue.ParseString(args, 3);
            World world = null;
            if (parsedRadius.failed() && parsedWorld.failed()) {
                parsedWorld = ParsedValue.ParseString(args, 2);
            }
            if (parsedX.failed()) {
                logger.LogInfo("Error with the X coordinates");
                return;
            }
            if (parsedZ.failed()) {
                logger.LogInfo("Error with the Z coordinates");
                return;
            }
            if (parsedRadius.failed()) {
                parsedRadius.setValue(0);
            }
            else if (parsedRadius.value() > maxRadius) {
                logger.LogInfo("Search radius is too big (max " + maxRadius + "). Defaulting to 0");
                parsedRadius.setValue(0);
            }
            else if (parsedRadius.value() < 0) {
                logger.LogInfo("Negative radius... really? lol (defaulting to 0)");
                parsedRadius.setValue(0);
            }
            if (parsedWorld.failed()) {
                if (sender instanceof Player) {
                    world = ((Player) sender).getWorld();
                }
                else {
                    logger.LogInfo("The world is invalid, and you're not a player");
                    return;
                }
            }
            else {
                world = WorldHelper.GetWorldFromName(parsedWorld.value());
                if (world == null) {
                    logger.LogInfo("The world name doesn't exist");
                    return;
                }
            }

            int centerX = parsedX.value();
            int centerZ = parsedZ.value();
            int radius = parsedRadius.value();
            if (MathsHelper.isOutside(centerX, minChunkX, maxChunkX) || MathsHelper.isOutside(centerZ, minChunkZ, maxChunkZ)) {
                logger.LogInfo("Why are you trying to check past the world border... They're unloaded lol");
                return;
            }

            ResultsCollection collection = PlayerResultsTable.getInstance().getResults(sender.getName());
            collection.clearResults();
            collection.updateHeader("Chunk Locations | Is Loaded");

            int total = 0;
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    total++;
                    try {
                        boolean isLoaded = world.isChunkLoaded(x, z);
                        collection.addRow(new ChunkLoadedResultRow(world, x, z, isLoaded));
                    }
                    catch (Exception e) {
                        ChatLogger.LogPlugin("Exception occoured while trying to check if a chunk is loaded");
                        e.printStackTrace();
                    }
                }
            }

            logger.LogInfo("Searched " + ChatColor.GREEN + total + ChatColor.GOLD + " chunks.");
            collection.displaySavedAndWrite(logger);
        }
    }

    private static class TeleportSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> parsedX = ParsedValue.ParseInteger(args, 0);
            ParsedValue<Integer> parsedZ = ParsedValue.ParseInteger(args, 1);
            ParsedValue<String> parsedPlayer = ParsedValue.ParseString(args, 2);
            Player player = null;
            if (parsedX.failed()) {
                logger.LogInfo("Error with the X coordinates");
                return;
            }
            if (parsedZ.failed()) {
                logger.LogInfo("Error with the Z coordinates");
                return;
            }
            if (parsedPlayer.failed()) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                else {
                    logger.LogInfo("Player name not specified!");
                    return;
                }
            }
            else {
                player = Bukkit.getPlayer(parsedPlayer.value());
                if (player == null) {
                    logger.LogInfo("That player doesn't exist, or isn't online");
                    return;
                }
            }

            World world = player.getWorld();
            if (world == null) {
                logger.LogInfo("Error with world... it somehow doesn't exist lol... how....");
                return;
            }

            try {
                Location location = new Location(world, (parsedX.value() << 4) + 8, 258, (parsedZ.value() << 4) + 8);
                location.setY(AreaSafety.GetSafeYLocation(location, 55).getY());
                CoolEffects.SpawnLightning(player);
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
                CoolEffects.SpawnLightning(player);
            }
            catch (Exception e) {
                logger.LogInfo("Failed to teleport");
            }
        }
    }

    private static class ChunkPositionSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> x = ParsedValue.ParseInteger(args, 0);
            ParsedValue<Integer> z = ParsedValue.ParseInteger(args, 1);
            if (x.failed() && z.failed()) {
                if (!(sender instanceof Player)) {
                    logger.LogInfo("You're not a player... mr console -_-");
                }
                else {
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    int posX = location.getBlockX() >> 4;
                    int posZ = location.getBlockZ() >> 4;
                    logger.LogInfo("Chunk Position: " + ChatFormatting.ColouredXZ(posX, posZ));
                }
            }
            else if (x.failed()) {
                logger.LogInfo("Problem with the X coordinate");
            }
            else if (z.failed()) {
                logger.LogInfo("Problem with the Z coordinate");
            }
            else {
                int posX = x.value() >> 4;
                int posZ = x.value() >> 4;
                logger.LogInfo("Chunk Position: " + ChatFormatting.ColouredXZ(posX, posZ));
            }
        }
    }

    private static class RegionPositionSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<Integer> x = ParsedValue.ParseInteger(args, 0);
            ParsedValue<Integer> z = ParsedValue.ParseInteger(args, 1);
            if (x.failed() && z.failed()) {
                if (!(sender instanceof Player)) {
                    logger.LogInfo("You're not a player... mr console -_-");
                }
                else {
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    int posX = location.getBlockX() >> 5;
                    int posZ = location.getBlockZ() >> 5;
                    logger.LogInfo("Region Position: " + ChatFormatting.ColouredXZ(posX, posZ));
                    logger.LogInfo("File Name: r." + posX + "." + posZ + ".mca");
                }
            }
            else if (x.failed()) {
                logger.LogInfo("Problem with the X coordinate");
            }
            else if (z.failed()) {
                logger.LogInfo("Problem with the Z coordinate");
            }
            else {
                int posX = x.value() >> 5;
                int posZ = x.value() >> 5;
                logger.LogInfo("Region Position: " + ChatFormatting.ColouredXZ(posX, posZ));
                logger.LogInfo("File Name: r." + posX + "." + posZ + ".mca");
            }
        }
    }
}
