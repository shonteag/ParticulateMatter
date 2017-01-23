package ramil.particulatematter.particlegen;


import com.sun.istack.internal.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;
import ramil.particulatematter.block.ITileEntityLinkable;
import ramil.particulatematter.energy.PMEnergyStorage;
import ramil.particulatematter.tile.BaseTileEntity;

import java.util.ArrayList;
import java.util.List;


public class TileEntityParticleChamber extends BaseTileEntity implements ITileEntityLinkable {

    public PMEnergyStorage storage = new PMEnergyStorage(1000000, 100000, 100000);
    public Particle particle;
    private int charge_on_tick = 0;


    public TileEntityParticleChamber() {
        this.particle = new Particle("creative_particle", 1000, 10000, 0, 5000, 2000, 1000);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        if (particle != null) {
            NBTTagCompound particleTagCompound = this.particle.getTagCompound();
            tagCompound.setTag("particle", particleTagCompound);
        }
        tagCompound.setInteger("energy", this.storage.getEnergyStored());
        tagCompound.setInteger("charge", this.charge_on_tick);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        if (tagCompound.hasKey("particle")) {
            if (this.particle == null) {
                Particle temp = new Particle(tagCompound.getCompoundTag("particle"));
                if (temp.isActive())
                    this.particle = temp;
            } else {
                this.particle.updateParticle(tagCompound.getCompoundTag("particle"));
            }
        } else {
            this.particle = null;
        }
        this.storage.setEnergy(tagCompound.getInteger("energy"));
        this.charge_on_tick = tagCompound.getInteger("charge");
        super.readFromNBT(tagCompound);
    }

    public void addCharge(int charge) {
        // used by lasers to charge the particle
        this.charge_on_tick += charge;
    }

    public int getCharge() {
        return this.charge_on_tick;
    }

    @Override
    public void update() {

        if (!getWorld().isRemote) { // server

            // check for redstone signal
            if (getWorld().isBlockPowered(this.getPos())) {
                // tick the particle, if it exists
                if (this.particle != null) {
                    if (this.particle.isActive()) {
                        // fire the particle, if it is active
                        this.storage.receiveEnergy(this.particle.tick(this.charge_on_tick), false);
                    } else {
                        // if inactive, remove particle, waste laser charge
                        // potential negative effects?
                        if (this.particle.death_type == EnumParticleDeath.OVERCHARGE) {
                            world.createExplosion((Entity) null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 3.0F, false);
                        }
                        else if (this.particle.death_type == EnumParticleDeath.UNDERCHARGE) {
                            // black hole maybe?
                        }

                        this.particle = null;
                        this.charge_on_tick = 0;
                    }
                }
            }

        } else { // client

        }

        this.markDirty();
        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);

        super.update();
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
    public boolean canLinkFrom() {
        return true;
    }

    @Override
    public List<Class<? extends TileEntity>> canLinkTo() {
        return new ArrayList<Class<? extends TileEntity>>(){};
    }

    @Override
    public boolean canLinkTo(TileEntity te_link_to) {
        return false;
    }

    @Override
    public boolean establishLink(TileEntity te_link_to) {
        return false;
    }


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

}