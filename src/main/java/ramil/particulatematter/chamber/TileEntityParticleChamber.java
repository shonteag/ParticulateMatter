package ramil.particulatematter.chamber;


import com.sun.istack.internal.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ramil.particulatematter.tile.ITileEntityLinkable;
import ramil.particulatematter.energy.PMEnergyStorage;
import ramil.particulatematter.item.ItemParticleContainer;
import ramil.particulatematter.item.Particle;
import ramil.particulatematter.tile.BaseTileEntity;

import java.util.ArrayList;
import java.util.List;

public class TileEntityParticleChamber extends BaseTileEntity implements ITileEntityLinkable {

    public PMEnergyStorage storage = new PMEnergyStorage(1000000, 100000, 100000);
    public Particle particle;
    private int charge_on_tick = 0;

    public static final int SIZE = 2;


    public TileEntityParticleChamber() {
        this.particle = null;
    }

    // This item handler will hold our nine inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TileEntityParticleChamber.this.markDirty();
        }
    };

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

        // check for redstone signal
        if (this.particle != null) {
            if (getWorld().isBlockPowered(this.getPos())) {
                // tick the particle, if it exists
                if (this.particle.isActive()) {
                    // fire the particle, if it is active
                    int rfReturn = this.particle.tick(this.charge_on_tick);
                    this.storage.receiveEnergy(rfReturn, false);

                    // this little f*cker cost me 3 hours of time. RESET THE DAMN CHARGE PER TICK COUNT!
                    this.charge_on_tick = 0;
                } else {
                    // if inactive, remove particle, waste laser charge
                    // potential negative effects?
                    if (this.particle.death_type == EnumParticleDeath.OVERCHARGE) {
                        // world.createExplosion((Entity) null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 3.0F, false);
                    }
                    else if (this.particle.death_type == EnumParticleDeath.UNDERCHARGE) {
                        // black hole maybe?
                    }
                    System.out.println("particulatematter:particle_death:" + this.particle.death_type + ":" + this.getPos().toString());

                    this.particle = null;
                    this.charge_on_tick = 0;
                }
            }
        }
        else {
            // waste the charge
            this.charge_on_tick = 0;

            // there is no particle, check the stacks
            ItemStack stack = this.itemStackHandler.getStackInSlot(0);
            if (stack.getItem() instanceof ItemParticleContainer && ((ItemParticleContainer)stack.getItem()).extractParticle(stack, true) != null) {
                stack = this.itemStackHandler.extractItem(0, 1, true);
                this.itemStackHandler.setStackInSlot(0, new ItemStack(new ItemParticleContainer(), 0));
                this.particle = ((ItemParticleContainer) stack.getItem()).extractParticle(stack, false);
                this.itemStackHandler.insertItem(1, stack, false);
            }
        }

        this.markDirty();

        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);

        super.update();
    }

    public boolean isItemValidForSlot(ItemStack stack, int index) {
        if (index == 1)
            return false;

        // if slot is insert (0), if the stack is not null, if the stack is an ItemParticleContainer, and if the ItemParticleContainer has a particle
        else if (index == 0 && stack != null && stack.getItem() instanceof ItemParticleContainer && ((ItemParticleContainer) stack.getItem()).extractParticle(stack, true) != null)
            return true;

        else
            return false;
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

    /*
    Item Slots
     */
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }


}