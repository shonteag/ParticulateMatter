package ramil.particulatematter.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ramil.particulatematter.chamber.ContainerParticleChamber;
import ramil.particulatematter.chamber.GuiContainerParticleChamber;
import ramil.particulatematter.chamber.TileEntityParticleChamber;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        // TileEntity instances here
        if (te instanceof TileEntityParticleChamber) {
            return new ContainerParticleChamber(player.inventory, (TileEntityParticleChamber) te);
        }


        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityParticleChamber) {
            TileEntityParticleChamber containerTileEntity = (TileEntityParticleChamber) te;
            return new GuiContainerParticleChamber(containerTileEntity, new ContainerParticleChamber(player.inventory, containerTileEntity));
        }
        // TileEntity instances here

        return null;
    }
}