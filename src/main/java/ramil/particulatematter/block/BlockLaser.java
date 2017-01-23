package ramil.particulatematter.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.energy.PMEnergyStorage;
import ramil.particulatematter.tile.TileEntityLaser;

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
            if (te.chamber != null) {
                if (world.isRemote) player.sendMessage(new TextComponentString("Linked to chamber at: " + te.chamber.getPos().toString()));
            }
            return true;
        }

        return false;
    }

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
