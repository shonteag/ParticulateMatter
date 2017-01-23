package ramil.particulatematter.tile;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.energy.PMEnergyStorage;
import ramil.particulatematter.energy.TeslaCompat;
import ramil.particulatematter.energy.TeslaForgeUnitsWrapper;

import java.util.ArrayList;
import java.util.List;

public class BaseTileEntity extends TileEntity implements ITickable {

    private Object teslaWrapper;

    public void update() {

        // energy pushing
        if (!getWorld().isRemote) {

            this.handleEnergyTick();
            // this.handleFluidTick();
            // this.handleItemsTick();

        }
    }

    private void handleEnergyTick() {
        // push energy around
        IEnergyStorage storage = this.getEnergyStorage();
        if (storage != null) {
            if (storage.getEnergyStored() > 0) {

                List<EnumFacing> push_to_sides = new ArrayList<EnumFacing>();

                // find all sides with energy handling tile entities
                for (EnumFacing side : this.getEnergyConnectionSides()) {
                    BlockPos pos = new BlockPos(side.getFrontOffsetX() + this.getPos().getX(), side.getFrontOffsetY() + this.getPos().getY(), side.getFrontOffsetZ() + this.getPos().getZ());
                    TileEntity tile = getWorld().getTileEntity(pos);

                    if (tile instanceof IEnergyStorage) {
                        push_to_sides.add(side);
                    } else if (tile instanceof IEnergyReceiver) {
                        push_to_sides.add(side);
                    }
                }

                // divide the energy evenly across output sides
                int max_extract = ((PMEnergyStorage)this.getEnergyStorage()).getMaxExtract();
                int actual_extract = ((PMEnergyStorage)this.getEnergyStorage()).extractEnergy(max_extract, true);
                if (push_to_sides.size() == 0) {
                    // lets avoid a divide by 0 error, shall we?
                    return;
                }
                int per_side_extract = actual_extract / push_to_sides.size();

                // loop over available sides and push
                for (EnumFacing side : push_to_sides) {
                    BlockPos pos = new BlockPos(side.getFrontOffsetX() + this.getPos().getX(), side.getFrontOffsetY() + this.getPos().getY(), side.getFrontOffsetZ() + this.getPos().getZ());
                    TileEntity tile = getWorld().getTileEntity(pos);

                    // Tesla
                    if (tile instanceof IEnergyStorage) {
                        IEnergyStorage receiver = (IEnergyStorage)tile;
                        int to_extract = receiver.receiveEnergy(storage.extractEnergy(per_side_extract, true), false);
                        storage.extractEnergy(to_extract, false);
                    }

                    // Redstone Flux (being phased out)
                    else if (tile instanceof IEnergyReceiver) {
                        IEnergyReceiver receiver = (IEnergyReceiver)tile;
                        int to_extract = receiver.receiveEnergy(side.getOpposite(), storage.extractEnergy(per_side_extract, true), false);
                        storage.extractEnergy(to_extract, false);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        // items
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            IItemHandler handler = this.getItemHandler(facing);
            if (handler != null) {
                return (T)handler;
            }
        }

        // fluids
        else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            IFluidHandler handler = this.getFluidHandler(facing);
            if (handler != null) {
                return (T)handler;
            }
        }

        // Forge Energy
        else if (capability == CapabilityEnergy.ENERGY) {
            IEnergyStorage storage = this.getEnergyStorage(facing);
            if (storage != null) {
                return (T)storage;
            }
        }

        // Tesla Energy
        else if (ParticulateMatter.tesla_loaded) {
            if (capability == TeslaCompat.teslaConsumer || capability == TeslaCompat.teslaProducer || capability == TeslaCompat.teslaHolder) {
                IEnergyStorage storage = this.getEnergyStorage(facing);
                if (storage != null) {
                    if (this.teslaWrapper == null) {
                        this.teslaWrapper = new TeslaForgeUnitsWrapper(storage);
                    }
                    return (T)this.teslaWrapper;
                }
            }
        }

        return super.getCapability(capability, facing);
    }

    /*
    To give a child TileEntity a capability, @Override one or more of these
    methods!
     */
    public IFluidHandler getFluidHandler(EnumFacing facing) {
        return null;
    }

    public IItemHandler getItemHandler(EnumFacing facing) {
        return null;
    }

    // for energy, override the next 3 methods!
    public IEnergyStorage getEnergyStorage(EnumFacing facing) {
        return null;
    }

    public IEnergyStorage getEnergyStorage() {
        for (EnumFacing face : EnumFacing.VALUES) {
            IEnergyStorage storage = this.getEnergyStorage(face);
            if (storage != null) {
                return storage;
            }
        }
        return null;
    }

    public EnumFacing[] getEnergyConnectionSides() {
        return EnumFacing.values();
    }

}
