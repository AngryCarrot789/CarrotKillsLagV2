package carrot.ckl.worlds;

import net.minecraft.server.v1_6_R3.TileEntity;
import net.minecraft.server.v1_6_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

import java.util.ArrayList;

public final class WorldHelper {
    public static ArrayList<Chunk> GetEveryLoadedChunkOnServer(){
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        try {
            for (World w : Bukkit.getWorlds()) {
                for (Chunk c : w.getLoadedChunks()) {
                    chunks.add(c);
                }
            }
        }
        catch(Exception e){
        }
        return chunks;
    }

    public static ArrayList<net.minecraft.server.v1_6_R3.Chunk> GetLoadedChunks(World world) {
        ArrayList<net.minecraft.server.v1_6_R3.Chunk> chunks = new ArrayList();
        try {
            net.minecraft.server.v1_6_R3.World minecraftWorld = WorldHelper.GetWorldServer(world);
            for(Chunk bukkitChunk : world.getLoadedChunks()){
                chunks.add(minecraftWorld.getChunkAt(bukkitChunk.getX(), bukkitChunk.getZ()));
            }
        }
        catch (Exception e){

        }
        return chunks;
    }

    public static CraftWorld GetCraftWorld(Chunk c){
        if (c == null) {
            return null;
        }
        return (CraftWorld) c.getWorld();
    }
    public static WorldServer GetWorldServer(Chunk c) {
        if (c == null) {
            return null;
        }
        return GetCraftWorld(c).getHandle();
    }
    public static CraftWorld GetCraftWorld(World w){
        if (w == null) {
            return null;
        }
        return (CraftWorld)w;
    }
    public static WorldServer GetWorldServer(World w) {
        if (w == null) {
            return null;
        }
        return GetCraftWorld(w).getHandle();
    }

    public static Chunk GetChunk(net.minecraft.server.v1_6_R3.Chunk chunk) {
        return chunk.world.getChunkAt(chunk.x, chunk.z).bukkitChunk;
    }

    public static ArrayList<TileEntity> GetTileEntitiesInWorld(World world, WorldServer ws) {
        ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
        for (Chunk chunk : world.getLoadedChunks()){
            for(BlockState bs : chunk.getTileEntities()){
                TileEntity te = ws.getTileEntity(bs.getX(), bs.getY(), bs.getZ());
                tiles.add(te);
            }
        }
        return tiles;
    }

    public static boolean DoesWorldExist(String worldName){
        for(World world : Bukkit.getWorlds()){
            try{
                String worldName1 = world.getName();
                if (worldName1.equalsIgnoreCase(worldName)){
                    return true;
                }
            }
            catch (Exception e){

            }
        }
        return false;
    }

    public static World GetWorldFromName(String worldName){
        for(World world : Bukkit.getWorlds()){
            try{
                String worldName1 = world.getName();
                if (worldName1.equalsIgnoreCase(worldName)){
                    return world;
                }
            }
            catch (Exception e){

            }
        }
        return null;
    }
}
