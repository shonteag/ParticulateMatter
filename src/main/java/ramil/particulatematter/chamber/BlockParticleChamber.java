package ramil.particulatematter.chamber;


import com.sun.istack.internal.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.block.BlockTileEntity;
import ramil.particulatematter.item.ItemParticulateWrench;

public class BlockParticleChamber extends BlockTileEntity<TileEntityParticleChamber> {

    public static final int GUI_ID = 1;

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

        if (player.getHeldItem(hand).getItem() instanceof ItemParticulateWrench) {
            return false;
        }

        else {
            player.openGui(ParticulateMatter.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

//    @Override
//    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @javax.annotation.Nullable EnumFacing side) {
//        return true;
//    }

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
