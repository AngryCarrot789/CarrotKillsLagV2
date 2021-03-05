package carrot.ckl.itemduct;

import net.minecraft.server.v1_6_R3.ItemStack;

import java.util.ArrayList;

public class StuffedItemduct {
    public Itemduct duct;
    public ArrayList<ItemStack> stuffed;

    public StuffedItemduct(Itemduct _duct, ArrayList<ItemStack> stuffedItems) {
        duct = _duct;
        stuffed = stuffedItems;
    }
}