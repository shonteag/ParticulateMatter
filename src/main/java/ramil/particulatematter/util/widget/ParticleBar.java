package ramil.particulatematter.util.widget;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.chamber.TileEntityParticleChamber;
import ramil.particulatematter.energy.PMEnergyStorage;
import ramil.particulatematter.item.Particle;

public class ParticleBar extends Gui {

    private int x;
    private int y;
    private TileEntityParticleChamber te;

    ResourceLocation components = new ResourceLocation(ParticulateMatter.mod_id, "textures/gui/gui_components.png");

    public ParticleBar(int x, int y, TileEntityParticleChamber te){
        this.x = x;
        this.y = y;
        this.te = te;
    }

    public void draw(){
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(components);

        int barX = this.x;
        int barY = this.y;

        // particle energy
        this.drawTexturedModalRect(barX, barY, 0, 0, 8, 55);
        // ticks left
        this.drawTexturedModalRect(barX+8, barY, 0, 0, 8, 55);




        // particle energy
        if(this.te.particle != null && this.te.particle.current_energy > 0) {
            int i = this.te.particle.current_energy*53/this.te.particle.max_energy;

            GlStateManager.color(0, 1, 1);
            this.drawTexturedModalRect(barX+1, barY+54-i, 1, 1, 6, i);
            GlStateManager.color(1F, 1F, 1F);
        }

        // optimal energy
        if (this.te.particle != null) {
            int arrowY = this.te.particle.optimal_energy*53/this.te.particle.max_energy;
            this.drawTexturedModalRect(barX+1, barY+54-arrowY, 8, 0, 6, 1);
        }

        // longevity
        if (this.te.particle != null && this.te.particle.ticks_left > 0) {
            int i = this.te.particle.ticks_left*53/this.te.particle.longevity;

            GlStateManager.color(0, 1, 0);
            this.drawTexturedModalRect(barX+8+1, barY+54-i, 1, 1, 6, i);
            GlStateManager.color(1F, 1F, 1F);
        }
    }

}
