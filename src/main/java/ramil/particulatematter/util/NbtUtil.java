package ramil.particulatematter.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NbtUtil {

    public static void writePos(NBTTagCompound tagCompound, String key, BlockPos pos) {
        if (pos == null)
            return;

        tagCompound.setIntArray(key, new int[]{pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos readPos(NBTTagCompound tagCompound, String key) {
        if (!tagCompound.hasKey(key))
            return null;

        int[] pos = tagCompound.getIntArray(key);
        return new BlockPos(pos[0], pos[1], pos[2]);
    }


}
