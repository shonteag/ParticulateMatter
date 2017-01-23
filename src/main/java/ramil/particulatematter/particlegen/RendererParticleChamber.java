package ramil.particulatematter.particlegen;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import ramil.particulatematter.ParticulateMatter;

public class RendererParticleChamber extends TileEntitySpecialRenderer<TileEntityParticleChamber> {

    private ResourceLocation particle_active = new ResourceLocation(ParticulateMatter.mod_id, "textures/entities/particle_active.png");

    @Override
    public void renderTileEntityAt(TileEntityParticleChamber te, double x, double y, double z, float partialTicks, int destroyStage) {
        Tessellator tessy = Tessellator.getInstance();
        VertexBuffer buffer = tessy.getBuffer();

        GlStateManager.depthMask(false);
        GlStateManager.disableAlpha();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        this.bindTexture(particle_active);

        if (te.particle != null) {



        }

        GlStateManager.popMatrix();
    }


}
