package ramil.particulatematter.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class PMEnergyStorage extends EnergyStorage implements IEnergyStorage {

    public PMEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("energy", this.getEnergyStored());
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        this.energy = tagCompound.getInteger("energy");
    }
}
