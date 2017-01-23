package ramil.particulatematter.item;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.particlegen.Particle;


import java.util.List;

public class ItemParticleContainer extends ItemBase {

    private Particle particle;


    public ItemParticleContainer() {
        super("particle_container");
        this.setMaxStackSize(1);

        // for testing only!
        particle = new Particle("Creative Particle", 1000, 2000, 0, 1300, 2000, 1000);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);

        if(particle == null) {
            // no particle present
            tooltip.add(TextFormatting.RED + "No particle present!");
        } else {
            // particle details here
            tooltip.add(TextFormatting.BLUE + particle.name);
            tooltip.add(TextFormatting.WHITE + "Longevity: " + TextFormatting.BLUE + particle.longevity + " ticks");
            tooltip.add(TextFormatting.WHITE + "Max Energy: " + TextFormatting.BLUE + particle.max_energy + " PE");
            tooltip.add(TextFormatting.WHITE + "Optimal Energy: " + TextFormatting.BLUE + particle.optimal_energy + " PE");
            tooltip.add(TextFormatting.WHITE + "Energy Drain: " + TextFormatting.BLUE + particle.energy_drain + " PE/tick");
            tooltip.add(TextFormatting.WHITE + "Deviation: " + TextFormatting.BLUE + particle.deviation);
            tooltip.add(TextFormatting.WHITE + "Max Yield: " + TextFormatting.BLUE + particle.yield + " RF/tick");
        }
    }

}
