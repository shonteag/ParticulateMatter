package ramil.particulatematter.tile;


import cofh.api.energy.IEnergyReceiver;
import com.sun.istack.internal.Nullable;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;
import ramil.particulatematter.block.ITileEntityLinkable;
import ramil.particulatematter.particlegen.TileEntityParticleChamber;
import ramil.particulatematter.energy.PMEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class TileEntityLaser extends BaseTileEntity implements IEnergyStorage, IEnergyReceiver, ITeslaConsumer, ITileEntityLinkable {

    public PMEnergyStorage storage = new PMEnergyStorage(1000000, 100000, 100000);
    public TileEntityParticleChamber chamber = null;
    public int charge_per_fire = 1000;

    public TileEntityLaser() {

    }


    @Override
    public void update() {
        // if the laser is linked and the laser has a redstone signal
        if (this.chamber != null && getWorld().isBlockPowered(this.getPos())) {
            // fire the laser
            if (this.storage.extractEnergy(charge_per_fire, true) == charge_per_fire) {
                // if we have enough energy to fire, fire!
                this.storage.extractEnergy(charge_per_fire, false);
                this.chamber.addCharge(charge_per_fire);
            }
        }

        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);

        super.update();
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("energy", storage.getEnergyStored());
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        this.storage.setEnergy(tagCompound.getInteger("energy"));
        super.readFromNBT(tagCompound);
    }


    @Override
    public IEnergyStorage getEnergyStorage(EnumFacing facing) {
        return this.storage;
    }

    @Override
    public EnumFacing[] getEnergyConnectionSides() {
        return EnumFacing.values();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public long givePower(long power, boolean simulated) {
        return (long)this.storage.receiveEnergy((int)power, simulated);
    }

    /*
    Linking
    This block can be linked *to*!
     */
    @Override
    public boolean canLinkFrom() {
        return false;
    }
    @Override
    public List<Class<? extends TileEntity>> canLinkTo() {
        List<Class<? extends TileEntity>> classes = new ArrayList<Class<? extends TileEntity>>();
        classes.add(TileEntityParticleChamber.class);
        return classes;
    }

    @Override
    public boolean canLinkTo(TileEntity te_link_to) {
        System.out.println("canLinkTo() called");
        if (te_link_to instanceof TileEntityParticleChamber) {  // lasers can only link to ParticleChambers
            if (te_link_to.getPos().distanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) <= 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean establishLink(TileEntity te_link_to) {
        if (te_link_to instanceof TileEntityParticleChamber) {
            this.chamber = (TileEntityParticleChamber)te_link_to;
            return true;
        }
        return false;
    }
    /*
    end linking
     */


    /*
    Networking - Update the client
     */
    @Override
    public NBTTagCompound getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }

    /*
    end networking
     */

}
