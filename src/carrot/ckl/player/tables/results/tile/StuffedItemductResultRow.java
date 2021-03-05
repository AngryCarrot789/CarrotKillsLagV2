package carrot.ckl.player.tables.results.tile;

import carrot.ckl.itemduct.StuffedItemduct;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.player.tables.base.ITeleportable;
import carrot.ckl.player.tables.base.ResultRow;
import carrot.ckl.position.BlockPosition;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class StuffedItemductResultRow extends ResultRow implements ITeleportable {
    private StuffedItemduct itemduct;

    public StuffedItemductResultRow(StuffedItemduct itemduct) {
        this.itemduct = itemduct;
        String location = ChatFormatting.ColouredXYZ(itemduct.duct.block.getLocation());
        String stuffedItems = "Stuffed: " + itemduct.stuffed.size();
        setContent(location + ChatColor.GREEN + " - " + stuffedItems);
    }

    @Override
    public void teleport(Player player) {
        Location pos = itemduct.duct.block.getLocation();
        player.sendMessage(ChatColor.GOLD + "Teleporting to " + BlockPosition.getColouredLocation(pos));
        player.teleport(pos, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}