package ramil.particulatematter.item;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;


import java.util.List;


public class ItemParticleContainer extends ItemBase implements IParticleContainer {

    public ItemParticleContainer() {
        super("particle_container");
        this.setMaxStackSize(1);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        // null particle, no particle present
        stack.setTagCompound(tagCompound);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        if (player.isCreative()) {

            // for testing only!
            // creative particle
            if (!player.isSneaking()) {
                this.insertParticle(player.getHeldItem(hand), new Particle("Creative Particle", 1000, 2000, 10, 1300, 2000, 500), false);
                if (world.isRemote)
                    player.sendMessage(new TextComponentString("Creative particle set!"));
            }

        }
        else {
            if (player.isSneaking()) {
                this.extractParticle(player.getHeldItem(hand), false);
                if (world.isRemote)
                    player.sendMessage(new TextComponentString("Particle released!"));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);

        if(stack == null || this.extractParticle(stack, true) == null) {
            // no particle present
            tooltip.add(TextFormatting.RED + "Empty");
        } else {
            Particle particle = this.extractParticle(stack, true);
            // particle details here
            tooltip.add(TextFormatting.BLUE + particle.name);
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                tooltip.add(TextFormatting.WHITE + "Longevity: " + TextFormatting.BLUE + particle.longevity + " ticks");
                tooltip.add(TextFormatting.WHITE + "Max Energy: " + TextFormatting.BLUE + particle.max_energy + " PE");
                tooltip.add(TextFormatting.WHITE + "Optimal Energy: " + TextFormatting.BLUE + particle.optimal_energy + " PE");
                tooltip.add(TextFormatting.WHITE + "Energy Drain: " + TextFormatting.BLUE + particle.energy_drain + " PE/tick");
                tooltip.add(TextFormatting.WHITE + "Deviation: " + TextFormatting.BLUE + particle.deviation);
                tooltip.add(TextFormatting.WHITE + "Max Yield: " + TextFormatting.BLUE + particle.yield + " RF/tick");
            }
            else {
                tooltip.add("<Press SHIFT>");
            }
        }
    }

    @Override
    public int getMaxParticleStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean insertParticle(ItemStack stack, Particle inParticle, boolean simulate) {
        if (stack != null && !stack.hasTagCompound()) {
            System.out.println("setting first tag");
            NBTTagCompound outerCompound = new NBTTagCompound();
            stack.setTagCompound(outerCompound);
        }

        if (stack != null && stack.hasTagCompound() && !stack.getTagCompound().hasKey("particle")) {
            System.out.println("setting tag");
            if (!simulate)
                stack.getTagCompound().setTag("particle", inParticle.getTagCompound());
            return true;
        }

        return false;
    }

    @Override
    public boolean insertParticle(ItemStack stack, NBTTagCompound particleTagCompound, boolean simulate) {
        Particle temp = new Particle(particleTagCompound);
        return insertParticle(stack, temp, simulate);
    }

    @Override
    public Particle extractParticle(ItemStack stack, boolean simulate) {
        if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("particle")) {
            Particle temp = new Particle((NBTTagCompound) stack.getTagCompound().getTag("particle"));

            if (!simulate) {
                stack.getTagCompound().removeTag("particle");
//                stack.setTagCompound(new NBTTagCompound());
            }

            return temp;
        }

        return null;
    }
}
