package ramil.particulatematter.laser;


import cofh.api.energy.IEnergyReceiver;
import com.sun.istack.internal.Nullable;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;
import ramil.particulatematter.chamber.TileEntityParticleChamber;
import ramil.particulatematter.energy.PMEnergyStorage;
import ramil.particulatematter.tile.BaseTileEntity;
import ramil.particulatematter.tile.ITileEntityLinkable;
import ramil.particulatematter.util.NbtUtil;

import java.util.ArrayList;
import java.util.List;

public class TileEntityLaser extends BaseTileEntity implements IEnergyStorage, IEnergyReceiver, ITeslaConsumer, ITileEntityLinkable {

    public PMEnergyStorage storage = new PMEnergyStorage(1000000, 100000, 100000);
    public BlockPos link = null;
    public int charge_per_fire = 10;
    public boolean fired_this_tick = false;

    public TileEntityLaser() {

    }

    @Override
    public void update() {
        this.fired_this_tick = false;

        // if the laser is linked and the laser has a redstone signal
        if (this.getLinked() != null && getWorld().isBlockPowered(this.getPos())) {
            // fire the laser
            int fire_cost = SettingsLaser.FIRE_COST + charge_per_fire;

            if (this.storage.extractEnergy(fire_cost, true) == fire_cost) {
                // check to see if it's still valid
                if (this.getLinked() instanceof TileEntityParticleChamber) {
                    // if we have enough energy to fire, fire!
                    this.storage.extractEnergy(fire_cost, false);
                    // add charge to target
                    this.getLinked().addCharge(this.charge_per_fire);
                    // update fired tracker
                    this.fired_this_tick = true;
                }
                else {
                    // no longer valid link
                    this.link = null;
                }
            }
        }

        this.markDirty();

        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);

        super.update();
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("energy", storage.getEnergyStored());

        // link
        NbtUtil.writePos(tagCompound, "chamber", this.link);

        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.storage.setEnergy(tagCompound.getInteger("energy"));

        // link
        this.link = NbtUtil.readPos(tagCompound, "chamber");
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
            this.link = te_link_to.getPos();
            this.markDirty();
            return true;
        }
        return false;
    }

    public TileEntityParticleChamber getLinked() {
        if (link != null && getWorld().getTileEntity(link) instanceof TileEntityParticleChamber) {
            return (TileEntityParticleChamber) getWorld().getTileEntity(link);
        }
        return null;
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
