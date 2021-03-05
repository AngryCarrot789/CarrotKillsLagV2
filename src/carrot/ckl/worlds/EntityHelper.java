package carrot.ckl.worlds;

import net.minecraft.server.v1_6_R3.Entity;
import org.bukkit.entity.EntityType;

public final class EntityHelper {
    public static Entity GetEntity(org.bukkit.entity.Entity entity){
        return WorldHelper.GetWorldServer(entity.getWorld()).getEntity(entity.getEntityId());
    }

    public static org.bukkit.entity.Entity GetEntity(Entity entity){
        return entity.getBukkitEntity();
    }

    public static boolean IsDroppedItem(org.bukkit.entity.Entity entity){
        return entity.getType() == EntityType.DROPPED_ITEM;
    }
}
