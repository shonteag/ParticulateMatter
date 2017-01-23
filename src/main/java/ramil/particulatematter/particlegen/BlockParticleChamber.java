package ramil.particulatematter.particlegen;


import com.sun.istack.internal.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.block.BlockTileEntity;
import ramil.particulatematter.energy.PMEnergyStorage;

public class BlockParticleChamber extends BlockTileEntity<TileEntityParticleChamber> {


    public BlockParticleChamber() {
        super(Material.IRON, "particle_chamber");
    }

    @Override
    public Class<TileEntityParticleChamber> getTileEntityClass() {
        return TileEntityParticleChamber.class;
    }

    @Nullable
    @Override
    public TileEntityParticleChamber createTileEntity(World world, IBlockState state) {
        return new TileEntityParticleChamber();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {

        // empty hand to show details
        if (player.getHeldItem(hand).isEmpty()) {
            TileEntityParticleChamber te = (TileEntityParticleChamber) world.getTileEntity(pos);
            if (te.storage != null) {
                if (world.isRemote) player.sendMessage(new TextComponentString("Energy stored: " + ((PMEnergyStorage) te.storage).getEnergyStored()));
            }

            if (te.particle != null) {
                if (world.isRemote) {
                    player.sendMessage(new TextComponentString(TextFormatting.BLUE + "Particle: " + TextFormatting.WHITE + te.particle.name));
                    player.sendMessage(new TextComponentString(TextFormatting.BLUE + "  Ticks Remaining: " + TextFormatting.WHITE + te.particle.ticks_left));
                    player.sendMessage(new TextComponentString(TextFormatting.BLUE + "  Laser charge: " + TextFormatting.WHITE + te.getCharge()));
                }
            } else {
                if (world.isRemote) player.sendMessage(new TextComponentString("Particle: null"));
            }

            return true;
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean isTranslucent(IBlockState state)
    {
        return true;
    }

}
