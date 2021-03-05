package carrot.ckl.position;

import carrot.ckl.tile.TouchingDirection;
import carrot.ckl.logs.ChatFormatting;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class BlockPosition {
    private World world;
    private final Double x;
    private final Double y;
    private final Double z;

    public BlockPosition(int x, int y, int z) {
        this.x = x + 0.5D;
        this.y = y + 0.5D;
        this.z = z + 0.5D;
    }

    public BlockPosition(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        this.world = location.getWorld();
    }

    public Location getCenter() {
        return new Location(this.world, this.x, this.y, this.z);
    }

    public Location getCenterLooking(TouchingDirection direction) {
        // pitch 0 = center, -90 = up, 90 = down
        // yaw 180 = north, 90 = west, 0 = south, -90 = east
        float yaw = 0.0f;
        float pitch = 0.0f;
        if (direction.touchingUp()) {
            pitch = -75.0f;
        }
        if (direction.touchingDown()) {
            pitch = 75.0f;
        }
        if (direction.touchingNorth() || direction.touchingSouth()) {
            if (direction.touchingNorth()) {
                if (direction.touchingEast()) {
                    yaw = -135.0f;
                }
                else if (direction.touchingWest()) {
                    yaw = 135.0f;
                }
                else {
                    yaw = 180.0f;
                }
            }
            if (direction.touchingSouth()) {
                if (direction.touchingEast()) {
                    yaw = -45.0f;
                }
                else if (direction.touchingWest()) {
                    yaw = 45.0f;
                }
                else {
                    yaw = 0.0f;
                }
            }
        }
        else {
            if (direction.touchingEast()) {
                yaw = -90.0f;
            }
            else if (direction.touchingWest()) {
                yaw = 90.0f;
            }
            else {
                // default to north
                yaw = 180.0f;
            }
        }
        return new Location(this.world, this.x, this.y, this.z, yaw, pitch);
    }

    public String getColoured() {
        return "" + ChatColor.RED + x + ", " + ChatColor.GREEN + y + ", " + ChatColor.BLUE + z + "";
    }

    public static String getColouredLocation(Location location) {
        return ChatFormatting.ColouredXYZ(location);
    }

    public String getColouredLocation() {
        return ChatFormatting.ColouredXYZ(this.x, this.y, this.z);
    }
}
