package carrot.ckl.itemduct;

import carrot.ckl.itemduct.nbt.NBTPart;
import carrot.ckl.itemduct.nbt.NBTPartStuffed;
import net.minecraft.server.v1_6_R3.*;
import org.bukkit.block.BlockState;

import java.util.ArrayList;

public class Itemduct {
    public NBTPart nbt;
    public TileEntity tile;
    public BlockState block;

    public Itemduct() {

    }

    public Itemduct(TileEntity _tile, BlockState _block) {
        tile = _tile;
        block = _block;
        GetNBT();
    }

    public void GetNBT() {
        if (tile != null) {
            NBTTagCompound _nbt = new NBTTagCompound();
            tile.b(_nbt);
            nbt = new NBTPart(_nbt);
        }
    }

    public ArrayList<ItemStack> GetStuffedItems() {
        return nbt.GetStuffedItemStacks();
    }

    public void ClearStuffedItems() {
        NBTPartStuffed stuffed = new NBTPartStuffed(new NBTTagList());
        nbt.nbt.set("StuffedItems", (NBTBase) stuffed.nbtList);
    }
}
