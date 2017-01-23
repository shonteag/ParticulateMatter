package ramil.particulatematter.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.block.ITileEntityLinkable;
import ramil.particulatematter.tile.BaseTileEntity;

import javax.xml.soap.Text;
import java.util.List;

public class ItemParticulateWrench extends ItemBase {

    private BlockPos link = null;

    public ItemParticulateWrench() {
        super("particulate_wrench");
        setMaxStackSize(1);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {

        if (this.link != null) {
            tooltip.add(TextFormatting.GREEN + "Link from " + this.link);
            tooltip.add(TextFormatting.WHITE + "  Shift-click to clear.");
        }

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.isSneaking()) {
                // clear the linking mode
                this.link = null;
                player.sendMessage(new TextComponentString("Linking mode cleared."));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {

            if(player.isSneaking()) {
                // sneak-use

            } else {
                if (this.link == null) {
                    // Init link!
                    TileEntity te_linking = world.getTileEntity(pos);
                    if (te_linking instanceof ITileEntityLinkable && ((ITileEntityLinkable) te_linking).canLinkFrom()) {
                        this.link = pos;
                        player.sendMessage(new TextComponentString("Linking mode set to: " + pos.toString()));
                    }

                } else {
                    // Establish link!
                    TileEntity te_link_to = world.getTileEntity(pos);
                    if (te_link_to instanceof ITileEntityLinkable &&
                            ((ITileEntityLinkable) te_link_to).canLinkTo(world.getTileEntity(this.link))) {
                        // these blocks can link!
                        ((ITileEntityLinkable) te_link_to).establishLink(world.getTileEntity(this.link));
                        this.link = null;
                        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Link established!"));
                    } else {
                        // cannot link :(
                        player.sendMessage(new TextComponentString(TextFormatting.RED + "Link cannot be established."));
                    }
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

}
