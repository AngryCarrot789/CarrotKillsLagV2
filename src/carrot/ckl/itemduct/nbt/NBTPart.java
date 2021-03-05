package carrot.ckl.itemduct.nbt;

import carrot.ckl.itemduct.ItemductFinder;
import net.minecraft.server.v1_6_R3.ItemStack;
import net.minecraft.server.v1_6_R3.NBTTagCompound;

import java.util.ArrayList;

public class NBTPart {
    public NBTTagCompound nbt;

    public NBTPart(NBTTagCompound _nbt) {
        nbt = _nbt;
    }

    public NBTPartStuffed NBTGetStuffedItems() {
        return ItemductFinder.NBTGetStuffedItemStacks(nbt);
    }

    public ArrayList<ItemStack> GetStuffedItemStacks() {
        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();
        NBTPartStuffed stuffedItems = NBTGetStuffedItems();
        for (int i = 0; i < stuffedItems.nbtList.size(); i++) {
            try {
                ItemStack itemstack = ItemStack.createStack(stuffedItems.GetStuffed(i).nbt);
                itemStacks.add(itemstack);
            }
            catch (Exception e) {
            }
        }
        return itemStacks;
    }
}
