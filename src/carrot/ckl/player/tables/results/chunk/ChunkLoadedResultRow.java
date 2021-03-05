package carrot.ckl.player.tables.results.chunk;

import carrot.ckl.helpers.AreaSafety;
import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.player.tables.base.ITeleportable;
import carrot.ckl.player.tables.base.ResultRow;
import carrot.ckl.position.BlockPosition;
import carrot.ckl.worlds.CoolEffects;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChunkLoadedResultRow extends ResultRow implements ITeleportable {
    private Location location;

    public ChunkLoadedResultRow(World world, int x, int z, boolean didLoad) {
        this.location = new Location(world, (x << 4) + 8, 258, (z << 4) + 8);
        this.location.setY(AreaSafety.GetSafeYLocation(this.location, 55).getY());
        String location = ChatFormatting.ColouredXZ(x, z);
        String loaded = didLoad ? (ChatColor.GREEN + "Loaded") : (ChatColor.RED + "Unloaded");
        setContent(location + ChatColor.GOLD + " - " + loaded);
    }

    @Override
    public void teleport(Player player) {
        player.sendMessage(ChatColor.GOLD + "Teleporting to " + BlockPosition.getColouredLocation(location));
        CoolEffects.SpawnLightning(player);
        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
        CoolEffects.SpawnLightning(player);
    }
}