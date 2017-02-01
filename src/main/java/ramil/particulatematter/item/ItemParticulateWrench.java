package ramil.particulatematter.item;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import org.lwjgl.input.Keyboard;
import ramil.particulatematter.tile.ITileEntityLinkable;

import java.util.List;

public class ItemParticulateWrench extends ItemBase {

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

        if (this.hasStoredLink(stack)) {
            tooltip.add(TextFormatting.GREEN + "Link from " + this.getLink(stack).toString());
            tooltip.add(TextFormatting.WHITE + "  Shift-click to clear.");
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            tooltip.add("Right-click block to initialize linking.");
        }
        else {
            tooltip.add("<Press SHIFT>");
        }

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        stack.setTagCompound(tagCompound);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.isSneaking()) {
                // clear the linking mode
                this.clearLink(player.getHeldItem(hand));
                player.sendMessage(new TextComponentString("Linking mode cleared."));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    public boolean setLink(ItemStack stack, BlockPos linkFrom) {
        // check for NBT
        if (stack != null && stack.hasTagCompound()) {
            stack.getTagCompound().setInteger("link_x", linkFrom.getX());
            stack.getTagCompound().setInteger("link_y", linkFrom.getY());
            stack.getTagCompound().setInteger("link_z", linkFrom.getZ());
            return true;
        }
        return false;
    }

    public boolean hasStoredLink(ItemStack stack) {
        if (stack != null &&
                stack.hasTagCompound() &&
                stack.getTagCompound().hasKey("link_x") &&
                stack.getTagCompound().hasKey("link_y") &&
                stack.getTagCompound().hasKey("link_z")) {
            return true;
        }
        return false;
    }

    public BlockPos getLink(ItemStack stack) {
        // check for NBT
        if (this.hasStoredLink(stack)) {
            return new BlockPos(
                    stack.getTagCompound().getInteger("link_x"),
                    stack.getTagCompound().getInteger("link_y"),
                    stack.getTagCompound().getInteger("link_z")
            );
        }
        return null;
    }

    public void clearLink(ItemStack stack) {
        if (this.hasStoredLink(stack)) {
            stack.getTagCompound().removeTag("link_x");
            stack.getTagCompound().removeTag("link_y");
            stack.getTagCompound().removeTag("link_z");
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!stack.hasTagCompound()) {
            this.onCreated(stack, world, player);
        }


        if(player.isSneaking()) {
            // sneak-use
        }

        else {
            if (!this.hasStoredLink(stack)) {
                // Init link!
                TileEntity te_linking = world.getTileEntity(pos);
                if (te_linking instanceof ITileEntityLinkable && ((ITileEntityLinkable) te_linking).canLinkFrom()) {
                    this.setLink(stack, pos);
                    if (world.isRemote)
                        player.sendMessage(new TextComponentString("Linking mode set to: " + pos.toString()));
                }
            }

            else {
                // Establish link!
                TileEntity te_link_to = world.getTileEntity(pos);
                BlockPos link = this.getLink(stack);
                if (link == null) {
                    return EnumActionResult.FAIL;
                }
                if (te_link_to instanceof ITileEntityLinkable &&
                        ((ITileEntityLinkable) te_link_to).canLinkTo(world.getTileEntity(link))) {
                    // these blocks can link!
                    ((ITileEntityLinkable) te_link_to).establishLink(world.getTileEntity(link));
                    this.clearLink(player.getHeldItem(hand));
                    if (world.isRemote)
                        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Link established!"));
                } else {
                    // cannot link :(
                    if (world.isRemote)
                        player.sendMessage(new TextComponentString(TextFormatting.RED + "Link cannot be established."));
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

}
