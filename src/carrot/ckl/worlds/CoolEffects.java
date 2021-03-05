package carrot.ckl.worlds;

import carrot.ckl.CarrotKillsLag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public final class CoolEffects {
    public static void SpawnLightning(Player player){
        try{
            if (player != null && player.getWorld() != null) {
                World w = player.getWorld();
                w.strikeLightningEffect(player.getLocation());
            }
        }
        catch (Exception ignored){

        }
    }
    public static void SpawnDelayedLightningBats(Player player){
        try{
            World world = player.getWorld();
            final ArrayList<Entity> entities = new ArrayList<Entity>();
            for(int i = 0; i < 10; i++){
                entities.add(world.spawnEntity(player.getLocation(), EntityType.BAT));
            }
            world.strikeLightningEffect(player.getLocation());
            Bukkit.getScheduler().scheduleSyncDelayedTask(CarrotKillsLag.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for(Entity entity : entities){
                        entity.getWorld().strikeLightningEffect(entity.getLocation());
                        //entity.getWorld().createExplosion(entity.getLocation(), 0, false);
                        entity.remove();
                    }
                }
            }, 80);
        }
        catch (Exception ignored){

        }
    }

    public static String RainbowText(String text){
        Random random = new Random();
        String buffer = new String();
        for(int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            switch (random.nextInt(6)) {
                case 0: buffer += ChatColor.RED + String.valueOf(character); break;
                case 1: buffer += ChatColor.GOLD + String.valueOf(character); break;
                case 2: buffer += ChatColor.YELLOW + String.valueOf(character); break;
                case 3: buffer += ChatColor.GREEN + String.valueOf(character); break;
                case 4: buffer += ChatColor.AQUA + String.valueOf(character); break;
                case 5: buffer += ChatColor.BLUE + String.valueOf(character); break;
                case 6: buffer += ChatColor.DARK_PURPLE + String.valueOf(character); break;
                default: buffer += ChatColor.WHITE + String.valueOf(character); break;
            }
        }
        return buffer;
    }
}
