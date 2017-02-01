package ramil.particulatematter.util.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.energy.PMEnergyStorage;

/**
 * Created by shonteag on 1/30/2017.
 */

@SideOnly(Side.CLIENT)
public class EnergyBar extends Gui {

    private PMEnergyStorage rfReference;
    private int x;
    private int y;

    ResourceLocation components = new ResourceLocation(ParticulateMatter.mod_id, "textures/gui/gui_components.png");

    public EnergyBar(int x, int y, PMEnergyStorage rfReference){
        this.x = x;
        this.y = y;
        this.rfReference = rfReference;
    }

    public void setData(int x, int y, PMEnergyStorage rfReference){
        this.x = x;
        this.y = y;
        this.rfReference = rfReference;
    }

    public void draw(){
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(components);

        int barX = this.x;
        int barY = this.y;

        // draw
        this.drawTexturedModalRect(barX, barY, 0, 0, 8, 55);

        if(this.rfReference.getEnergyStored() > 0){
            int i = this.rfReference.getEnergyStored()*53/this.rfReference.getMaxEnergyStored();

            GlStateManager.color(1, 0, 0);
            this.drawTexturedModalRect(barX+1, barY+54-i, 1, 1, 6, i);
            GlStateManager.color(1F, 1F, 1F);
        }
    }

}
