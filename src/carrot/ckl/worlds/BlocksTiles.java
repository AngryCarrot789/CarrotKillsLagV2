package carrot.ckl.worlds;

import net.minecraft.server.v1_6_R3.TileEntity;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class BlocksTiles {
    public static boolean IsBlockPowered(Location blockLocation){
        return blockLocation.getBlock().isBlockIndirectlyPowered();
    }

    public static boolean SetBlockAs(Location blockLocation, int id, boolean onlyIfRedstonePowered) {
        if (onlyIfRedstonePowered) {
            if (!IsBlockPowered(blockLocation)) {
                return false;
            }
        }

        blockLocation.getBlock().setType(Material.getMaterial(id));
        return true;
    }

    public static ArrayList<BlockState> GetTiles(int id){
        ArrayList<BlockState> totalTiles = new ArrayList<BlockState>();
        try {
            for(Chunk chunk : WorldHelper.GetEveryLoadedChunkOnServer()){
                for(BlockState tile : chunk.getTileEntities()) {
                    if (tile.getTypeId() == id){
                        totalTiles.add(tile);
                    }
                }
            }
        }
        catch (Exception e){
        }
        return totalTiles;
    }

    public static boolean IsAir(Location pos) {
        if (pos.getBlock() == null) {
            return true;
        }
        if (pos.getBlock().getType() == Material.AIR) {
            return true;
        }
        return false;
    }

    public static TileEntity GetTileEntity(BlockState tile){
        return WorldHelper.GetWorldServer(tile.getWorld()).getTileEntity(tile.getX(), tile.getY(), tile.getZ());
    }

    public static BlockState GetTileEntity(World world, TileEntity tile){
        return world.getBlockAt(tile.x, tile.y, tile.z).getState();
    }

    public static List<Block> BlocksBetweenPoints(Location loc1, Location loc2)
    {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    blocks.add(loc1.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }
}
