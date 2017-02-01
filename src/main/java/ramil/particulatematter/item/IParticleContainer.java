package ramil.particulatematter.item;


import com.sun.istack.internal.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IParticleContainer {

    public int getMaxParticleStackSize(ItemStack stack);

    public boolean insertParticle(ItemStack stack, Particle inParticle, boolean simulate);

    public boolean insertParticle(ItemStack stack, NBTTagCompound particleTagCompound, boolean simulate);

    public Particle extractParticle(ItemStack stack, boolean simulate);

}
