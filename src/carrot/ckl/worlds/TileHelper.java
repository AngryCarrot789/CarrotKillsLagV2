package carrot.ckl.worlds;

import net.minecraft.server.v1_6_R3.TileEntity;
import org.bukkit.World;
import org.bukkit.block.BlockState;

public final class TileHelper {
    public static TileEntity GetTileEntity(BlockState tile){
        return WorldHelper.GetWorldServer(tile.getWorld()).getTileEntity(tile.getX(), tile.getY(), tile.getZ());
    }

    public static BlockState GetTileEntity(World world, TileEntity tile){
        return world.getBlockAt(tile.x, tile.y, tile.z).getState();
    }
}
