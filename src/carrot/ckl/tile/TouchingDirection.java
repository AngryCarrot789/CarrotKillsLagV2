package carrot.ckl.tile;

import org.bukkit.World;
import org.bukkit.block.BlockState;

public class TouchingDirection {
    private final boolean north;
    private final boolean south;
    private final boolean east;
    private final boolean west;
    private final boolean up;
    private final boolean down;

    public TouchingDirection(
            boolean north,
            boolean east,
            boolean south,
            boolean west,
            boolean up,
            boolean down) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    public static TouchingDirection findTouchingBounds(BlockState origin, ItemDataPair data) {
        World world = origin.getWorld();
        int oX = origin.getX();
        int oY = origin.getY();
        int oZ = origin.getZ();

        boolean n = data.matchBlock(world.getBlockAt(oX - 0, oY - 0, oZ - 1));
        boolean s = data.matchBlock(world.getBlockAt(oX - 0, oY - 0, oZ + 1));
        boolean e = data.matchBlock(world.getBlockAt(oX + 1, oY - 0, oZ - 0));
        boolean w = data.matchBlock(world.getBlockAt(oX - 1, oY - 0, oZ - 0));
        boolean u = data.matchBlock(world.getBlockAt(oX - 0, oY + 1, oZ - 0));
        boolean d = data.matchBlock(world.getBlockAt(oX - 0, oY - 1, oZ - 0));

        return new TouchingDirection(n, e, s, w, u, d);
    }

    public boolean touchingNorth() {
        return north;
    }

    public boolean touchingEast() {
        return east;
    }

    public boolean touchingSouth() {
        return south;
    }

    public boolean touchingWest() {
        return west;
    }

    public boolean touchingUp() {
        return up;
    }

    public boolean touchingDown() {
        return down;
    }

    public boolean touchingAny() {
        return north || south || east || west || up || down;
    }

    @Override
    public String toString() {
        String out = "";
        if (!(north || east || south || west || up || down)) {
            out = "none";
            return out;
        }
        if (north) {
            out += "north";
            if (east || south || west || up || down){
                out += ", ";
            }
        }
        if (east) {
            out += "east";
            if (south || west || up || down) {
                out += ", ";
            }
        }
        if (south) {
            out += "south";
            if (west || up || down) {
                out += ", ";
            }
        }
        if (west) {
            out += "west";
            if (up || down) {
                out += ", ";
            }
        }
        if (up) {
            out += "up";
            if (down) {
                out += ", ";
            }
        }
        if (down) {
            out += "down";
        }
        return out;
    }
}
