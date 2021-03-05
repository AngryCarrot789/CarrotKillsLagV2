package carrot.ckl.player.tables.results.tile;

import carrot.ckl.player.tables.base.ITeleportable;
import carrot.ckl.player.tables.base.ResultRow;
import carrot.ckl.position.BlockPosition;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TileWorldPositionDataResultRow extends ResultRow implements ITeleportable {
    private BlockPosition position;

    public TileWorldPositionDataResultRow(Block block) {
        this.position = new BlockPosition(block.getLocation());
        String world = ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + block.getWorld().getName() + ChatColor.GOLD + "]";
        String location = position.getColoured();
        String data = block.getTypeId() + ":" + ChatColor.LIGHT_PURPLE + block.getData();
        setContent(world + " -- " + location + ChatColor.GREEN + " - ID: " + data);
    }

    @Override
    public void teleport(Player player) {
        player.sendMessage(ChatColor.GOLD + "Teleporting to " + position.getColoured());
        player.teleport(position.getCenter(), PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}