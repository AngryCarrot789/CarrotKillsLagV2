package carrot.ckl.command.commands;

import carrot.ckl.command.IExecutableCommand;
import carrot.ckl.command.IExecutableSubCommands;
import carrot.ckl.command.helpers.ArgumentParser;
import carrot.ckl.command.helpers.ParsedValue;
import carrot.ckl.tile.ItemDataPair;
import carrot.ckl.tile.TouchingDirection;
import carrot.ckl.itemduct.Itemduct;
import carrot.ckl.itemduct.ItemductFinder;
import carrot.ckl.itemduct.StuffedItemduct;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.PlayerResultsTable;
import carrot.ckl.player.tables.ResultsCollection;
import carrot.ckl.player.tables.results.tile.StuffedItemductResultRow;
import carrot.ckl.player.tables.results.tile.TileTouchResultRow;
import carrot.ckl.player.tables.results.tile.TileWorldPositionDataResultRow;
import carrot.ckl.worlds.WorldHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class TileCommands extends IExecutableSubCommands {
    public TileCommands() {
        subCommandMap = new TreeMap<String, IExecutableCommand>();
        subCommandMap.put("find", new FindTileEntitySubCommand());
        subCommandMap.put("findtouch", new FindTileEntityTouchSubCommand());
        subCommandMap.put("findtouchitemduct", new FindItemductTouchingSubCommand());
        subCommandMap.put("stuffed", new FindItemductStuffedSubCommand());
    }
    @Override
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
                    "Do " + ChatColor.GREEN + "/ckl tile" + ChatColor.GOLD +
                    " for a list of sub-commands");
        }
        else {
            executableCommand.Execute(sender, logger, subArgs);
        }
    }

    @Override
    public void displayHelp(ChatLogger logger) {
        logger.LogInfo("All of these sub-commands have the optional" +
                       "[(ws:w:(c:radius))] parameter. ws = every world, " +
                       "w = world you're in or a world name, " +
                       "c:radius = chunk you're in, or a radius for a circle search of chunks");
        logger.LogAny(ChatFormatting.FormatCommand("find",
                                                   "<id:data1>",
                                                   "Finds every TileEntity with the given id and data"));
        logger.LogAny(ChatFormatting.FormatCommand("findtouch",
                                                   "<id:data1> <id:data2>",
                                                   "Finds every TileEntity with the given id/data <1>, " +
                                                   "and searches 6 blocks around if their id/data is <2>"));
        logger.LogAny(ChatFormatting.FormatCommand("findtouchitemduct",
                                                   "<id:data1>",
                                                   "Finds every itemduct and searches for blocks touching it with id/data <1>"));
        logger.LogAny(ChatFormatting.FormatCommand("stuffed",
                                                   "[(ws:w:(c:radius))]",
                                                   "Finds stuffed itemducts"));
    }

    public static class FindTileEntitySubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<ItemDataPair> blockData = ArgumentParser.ParseItemData(args, 0);
            ParsedValue<String> searchType = ArgumentParser.ParseString(args, 1);
            ArrayList<Chunk> searchChunks = new ArrayList<Chunk>();

            if (blockData.failed()) {
                logger.LogInfo("Error with the block data. Example: 1404:21. If you dont care about block data, put 1404:-1");
                return;
            }
            if (searchType.failed()) {
                if (searchType instanceof Player) {
                    logger.LogInfo("Searching the chunk you're in");
                    searchChunks.add(((Player) sender).getLocation().getChunk());
                }
                else {
                    logger.LogInfo("Cannot search an area around a non player. CONSOLE! Use ws");
                    return;
                }
            }
            else if (searchType.value().equalsIgnoreCase("ws")) {
                logger.LogInfo("Searching every world's loaded chunks");
                for (World world : Bukkit.getWorlds()) {
                    searchChunks.addAll(Arrays.asList(world.getLoadedChunks()));
                }
            }
            else if (searchType.value().equalsIgnoreCase("c")) {
                if (sender instanceof Player) {
                    logger.LogInfo("Searching the chunk you're in");
                    searchChunks.add(((Player) sender).getLocation().getChunk());
                }
                else {
                    logger.LogInfo("You're not a player...");
                    return;
                }
            }
            else {
                ParsedValue<Integer> searchRadius = ParsedValue.ParseInteger(args, 1);
                if (!searchRadius.failed()) {
                    if (sender instanceof Player) {
                        searchRadius = ArgumentParser.ParseInteger(args, 1);
                        if (searchRadius.failed()) {
                            logger.LogInfo("Error with the radius to search");
                            return;
                        }
                        else {
                            if (searchRadius.value() > ChunkCommands.maxRadius) {
                                logger.LogInfo("Radius is too big. Maximum is " + ChunkCommands.maxRadius + ". Defaulting to 0");
                                searchRadius.setValue(0);
                                return;
                            }
                            Location center = ((Player) sender).getLocation();
                            World world = center.getWorld();
                            int centerX = center.getBlockX() >> 4;
                            int centerZ = center.getBlockZ() >> 4;
                            int radius = searchRadius.value();
                            for (int x = centerX - radius; x <= centerX + radius; x++) {
                                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                                    searchChunks.add(world.getChunkAt(x, z));
                                }
                            }
                        }
                    }
                    else {
                        logger.LogInfo("Console...");
                        return;
                    }
                }
                else {
                    World searchWorld = WorldHelper.GetWorldFromName(searchType.value());
                    if (searchWorld != null) {
                        logger.LogInfo("Searching in " + ChatColor.GREEN + ChatFormatting.Apostrophise(searchWorld.getName()));
                        searchChunks.addAll(Arrays.asList(searchWorld.getLoadedChunks()));
                    }
                    else {
                        if (searchType.value().equalsIgnoreCase("w")) {
                            if (sender instanceof Player) {
                                logger.LogInfo("Searching all the loaded chunks in the world you're in");
                                searchChunks.addAll(Arrays.asList(((Player) sender).getWorld().getLoadedChunks()));
                            }
                            else {
                                logger.LogInfo("Console...");
                                return;
                            }
                        }

                        else {
                            logger.LogInfo("That search area is unknown");
                            return;
                        }
                    }
                }
            }

            ResultsCollection collection = PlayerResultsTable.getInstance().getResults(sender.getName());
            collection.clearResults();
            collection.updateHeader("World | TileEntity Position | ID:Data");

            ItemDataPair data = blockData.value();

            for (Chunk chunk : searchChunks) {
                for (BlockState tileEntity : chunk.getTileEntities()) {
                    Block block = tileEntity.getBlock();
                    if (data.matchBlock(block)) {
                        collection.addRow(new TileWorldPositionDataResultRow(block));
                    }
                }
            }

            logger.LogInfo("Found " + collection.resultsCount());
            collection.displaySavedAndWrite(logger);
        }
    }

    public static class FindTileEntityTouchSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<ItemDataPair> itemData1 = ArgumentParser.ParseItemData(args, 0);
            ParsedValue<ItemDataPair> itemData2 = ArgumentParser.ParseItemData(args, 1);
            ParsedValue<String> worldName = ArgumentParser.ParseString(args, 2);
            World world = null;

            if (itemData1.failed()) {
                logger.LogInfo("Error with the 1st parameter");
                return;
            }
            if (itemData2.failed()) {
                logger.LogInfo("Error with the 2nd parameter");
                return;
            }
            if (worldName.failed()) {
                if (sender instanceof Player) {
                    world = ((Player) sender).getWorld();
                }
                else {
                    logger.LogInfo("Error with the world name name (parameter 4)");
                    return;
                }
            }

            if (world == null) {
                world = WorldHelper.GetWorldFromName(worldName.value());
                if (world == null) {
                    logger.LogInfo("Error getting the world. It doesn't exist or isn't loaded");
                    return;
                }
            }

            ResultsCollection results = PlayerResultsTable.getInstance().getResults(sender.getName());
            results.clearResults();
            results.updateHeader("Position (X/Y/Z) | Direction (N/E/S/W/UP/DOWN)");

            Chunk[] chunks = world.getLoadedChunks();
            int totalTileEntities = 0;
            for (Chunk chunk : chunks) {
                BlockState[] tileEntities = chunk.getTileEntities();
                totalTileEntities += tileEntities.length;
                for (BlockState state : tileEntities) {
                    if (itemData1.value().matchBlock(state.getBlock())) {
                        TouchingDirection direction = TouchingDirection.findTouchingBounds(state, itemData2.value());
                        if (direction.touchingAny()) {
                            results.addRow(new TileTouchResultRow(state.getLocation(), direction));
                        }
                    }
                }
            }
            logger.LogInfo("Scanned " + ChatColor.GREEN + chunks.length +
                           ChatColor.GOLD + " chunks, found " +
                           ChatColor.GREEN + totalTileEntities +
                           ChatColor.GOLD + " TEs, " +
                           ChatColor.GREEN + results.resultsCount() + ChatColor.GOLD + " results");
            results.displaySavedAndWrite(logger);

        }
    }

    public static class FindItemductTouchingSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<ItemDataPair> touchingBlockId = ArgumentParser.ParseItemData(args, 0);
            ParsedValue<String> worldName = ArgumentParser.ParseString(args, 1);

            World world = null;

            if (touchingBlockId.failed()) {
                logger.LogInfo("Error with the touching block");
                return;
            }
            if (worldName.failed()) {
                if (sender instanceof Player) {
                    world = ((Player) sender).getWorld();
                }
                else {
                    logger.LogInfo("Error with the world name name (parameter 4)");
                    return;
                }
            }

            if (world == null) {
                world = WorldHelper.GetWorldFromName(worldName.value());
                if (world == null) {
                    logger.LogInfo("Error getting the world. It doesn't exist or isn't loaded");
                    return;
                }
            }

            ResultsCollection results = PlayerResultsTable.getInstance().getResults(sender.getName());
            results.clearResults();
            results.updateHeader("Position (X/Y/Z) | Direction (N/E/S/W/UP/DOWN)");

            ArrayList<Itemduct> itemducts = ItemductFinder.GetConduitsWithinWorld(world, true);
            for (Itemduct itemduct : itemducts) {
                TouchingDirection direction = TouchingDirection.findTouchingBounds(itemduct.block, touchingBlockId.value());
                if (direction.touchingAny()) {
                    results.addRow(new TileTouchResultRow(itemduct.block.getLocation(), direction));
                }
            }
            logger.LogInfo("Found " +
                           ChatColor.GREEN + itemducts.size() +
                           ChatColor.GOLD + " itemducts, " +
                           ChatColor.GREEN + results.resultsCount() + ChatColor.GOLD + " results");
            results.displaySavedAndWrite(logger);
        }
    }

    public static class FindItemductStuffedSubCommand implements IExecutableCommand {
        @Override
        public void Execute(CommandSender sender, ChatLogger logger, String[] args) {
            ParsedValue<String> searchArea = ArgumentParser.ParseString(args, 1);

            ResultsCollection collection = PlayerResultsTable.getInstance().getResults(sender.getName());

            if (searchArea.failed()) {
                collection.clearResults();
                collection.updateHeader("Itemduct Location | Stuffed Items");
                if (sender instanceof Player) {
                    logger.LogInfo("Searching all the loaded chunks in the world you're in for stuffed itemducts");
                    searchWorld(((Player) sender).getWorld(), logger, collection);
                }
                else {
                    logger.LogInfo("Searching loaded chunks in every world for stuffed itemducts");
                    searchEveryWorld(logger, collection);
                }
            }
            else if (searchArea.value().equalsIgnoreCase("ws")) {
                collection.clearResults();
                collection.updateHeader("Itemduct Location | Stuffed Items");
                logger.LogInfo("Searching loaded chunks in every world for stuffed itemducts");
                searchEveryWorld(logger, collection);
            }
            else {
                if (searchArea.value().equalsIgnoreCase("w")) {
                    if (sender instanceof Player) {
                        collection.clearResults();
                        collection.updateHeader("Itemduct Location | Stuffed Items");
                        searchWorld(((Player) sender).getWorld(), logger, collection);
                    }
                    else {
                        logger.LogInfo("Console... use ws :)");
                    }
                }
                else if (searchArea.value().equalsIgnoreCase("c")) {
                    collection.clearResults();
                    collection.updateHeader("Itemduct Location | Stuffed Items");
                    searchChunk(((Player) sender).getLocation().getChunk(), logger, collection);
                }
                else {
                    logger.LogInfo("That search area is unknown. Use ws, w or c for all worlds, your world or chunk youre in");
                }
            }
        }


        private void searchEveryWorld(ChatLogger logger, ResultsCollection collection) {
            for (World world : Bukkit.getWorlds()) {
                searchWorld(world, logger, collection);
            }
        }

        private void searchWorld(World world, ChatLogger logger, ResultsCollection collection) {
            ArrayList<StuffedItemduct> stuffedItemducts = ItemductFinder.GetStuffedItemductsInWorld(WorldHelper.GetWorldServer(world));
            if (stuffedItemducts.size() > 0) {
                try {
                    for (StuffedItemduct stuffed : stuffedItemducts) {
                        if (stuffed.duct != null) {
                            collection.addRow(new StuffedItemductResultRow(stuffed));
                        }
                    }
                }
                catch (Exception e) {
                    logger.LogInfo("Exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            logger.LogInfo("Found " + ChatColor.GREEN + collection.resultsCount() + ChatColor.GOLD + " results");
            collection.displaySavedAndWrite(logger);
        }

        private void searchChunk(Chunk chunk, ChatLogger logger, ResultsCollection collection) {
            ArrayList<StuffedItemduct> stuffedItemducts = ItemductFinder.GetStuffedItemductsInChunk(chunk);
            if (stuffedItemducts.size() > 0) {
                try {
                    for (StuffedItemduct stuffed : stuffedItemducts) {
                        if (stuffed.duct != null) {
                            collection.addRow(new StuffedItemductResultRow(stuffed));
                        }
                    }
                }
                catch (Exception e) {
                    logger.LogInfo("Exception: " + e.getMessage());
                }
            }

            logger.LogInfo("Found " + ChatColor.GREEN + collection.resultsCount() + ChatColor.GOLD + " results");
            collection.displaySavedAndWrite(logger);
        }
    }
}
