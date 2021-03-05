package carrot.ckl.tile;

import org.bukkit.block.Block;

public class ItemDataPair {
    private int id;
    private byte data;
    public ItemDataPair(int id, byte data) {
        this.id = id;
        this.data = data;
    }

    public boolean matchBlock(Block block) {
        return block.getType().getId() == this.id && (this.data == -1 || block.getData() == this.data);
    }

    public boolean matchData(int id, byte data) {
        return id == this.id && (this.data == -1 || data == this.data);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return id + ":" + data;
    }
}
