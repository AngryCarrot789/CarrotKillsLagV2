package carrot.ckl.player.tables.results.tile;

import carrot.ckl.tile.TouchingDirection;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.base.IBlockData;
import carrot.ckl.player.tables.base.ITeleportable;
import carrot.ckl.player.tables.base.ResultRow;
import carrot.ckl.position.BlockPosition;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TileTouchResultRow extends ResultRow implements ITeleportable, IBlockData {
    private Location location;
    private BlockState blockState;

    public TileTouchResultRow(Location origin, TouchingDirection direction) {
        BlockPosition position = new BlockPosition(origin);
        this.blockState = origin.getBlock().getState();
        this.location = position.getCenterLooking(direction);
        String location = position.getColoured();
        String directions = ChatColor.GREEN + direction.toString();
        setContent(location + ChatColor.LIGHT_PURPLE + "   Directions: " + directions);
    }

    @Override
    public void teleport(Player player) {
        player.sendMessage(
                ChatColor.GOLD + "Teleporting to " +
                BlockPosition.getColouredLocation(location) + ChatColor.GOLD +
                ", Yaw " + ChatColor.YELLOW + location.getYaw() + ChatColor.GOLD +
                ", Pitch " + ChatColor.LIGHT_PURPLE + location.getPitch());
        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
    }

    @Override
    public void displayBlockData(ChatLogger logger) {
        if (blockState != null) {
            logger.LogInfo("ID: " + blockState.getTypeId() + ", Data: " + blockState.getBlock().getData());
        }
        else {
            logger.LogInfo("Block is invalid");
        }
    }
}
