package carrot.ckl.itemduct.nbt;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;

public class NBTPartStuffed {
    public NBTTagList nbtList;

    public NBTPartStuffed(NBTTagList _nbtList) {
        nbtList = _nbtList;
    }

    public NBTPart GetStuffed(int index) {
        return new NBTPart((NBTTagCompound) nbtList.get(index));
    }
}
