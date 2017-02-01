package ramil.particulatematter.laser;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.block.BlockTileEntity;
import ramil.particulatematter.energy.PMEnergyStorage;

import javax.annotation.Nullable;

public class BlockLaser extends BlockTileEntity<TileEntityLaser> {


    public BlockLaser() {
        super(Material.IRON, "laser");
    }

    @Override
    public Class<TileEntityLaser> getTileEntityClass() {
        return TileEntityLaser.class;
    }

    @Override
    public TileEntityLaser createTileEntity(World world, IBlockState state) {
        return new TileEntityLaser();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {

        // empty hand to show details
        if (player.getHeldItem(hand).isEmpty()) {
            TileEntityLaser te = (TileEntityLaser) world.getTileEntity(pos);
            if (te.storage != null) {
                if (world.isRemote) player.sendMessage(new TextComponentString("Energy stored: " + ((PMEnergyStorage) te.storage).getEnergyStored()));
            }
            if (te.getLinked() != null) {
                if (world.isRemote) player.sendMessage(new TextComponentString("Linked to chamber at: " + te.getLinked().getPos().toString()));
            }
            return true;
        }

        return false;
    }

//    @Override
//    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
//        return true;
//    }


    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
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

}
