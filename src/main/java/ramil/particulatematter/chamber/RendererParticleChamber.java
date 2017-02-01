package ramil.particulatematter.chamber;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ramil.particulatematter.ParticulateMatter;

import java.util.Random;

public class RendererParticleChamber extends TileEntitySpecialRenderer<TileEntityParticleChamber> {

    private ResourceLocation particle_active = new ResourceLocation(ParticulateMatter.mod_id, "textures/entities/particle_active.png");

    @Override
    public void renderTileEntityAt(TileEntityParticleChamber te, double x, double y, double z, float partialTicks, int destroyStage) {
        // THIS IS BOTANIA CODE FOR LEARNING PURPOSES!!!!

//        Tessellator tessellator = Tessellator.getInstance();
//
//        int ticks = ClientTickHandler.ticksInGame % 200;
//        if (ticks >= 100)
//            ticks = 200 - ticks - 1;
//
//        float f1 = ticks / 200F;
//        float f2 = 0F;
//        if (f1 > 0.7F)
//            f2 = (f1 - 0.7F) / 0.2F;
//        Random random = new Random(seed);
//
//        GlStateManager.pushMatrix();
//        GlStateManager.disableTexture2D();
//        GlStateManager.shadeModel(GL11.GL_SMOOTH);
//        GlStateManager.enableBlend();
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//        GlStateManager.disableAlpha();
//        GlStateManager.enableCull();
//        GlStateManager.depthMask(false);
//        GlStateManager.scale(xScale, yScale, zScale);
//
//        for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
//            GlStateManager.rotate(random.nextFloat() * 360F, 1F, 0F, 0F);
//            GlStateManager.rotate(random.nextFloat() * 360F, 0F, 1F, 0F);
//            GlStateManager.rotate(random.nextFloat() * 360F, 0F, 0F, 1F);
//            GlStateManager.rotate(random.nextFloat() * 360F, 1F, 0F, 0F);
//            GlStateManager.rotate(random.nextFloat() * 360F, 0F, 1F, 0F);
//            GlStateManager.rotate(random.nextFloat() * 360F + f1 * 90F, 0F, 0F, 1F);
//            tessellator.getBuffer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
//            float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
//            float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
//            float r = ((color & 0xFF0000) >> 16) / 255F;
//            float g = ((color & 0xFF00) >> 8) / 255F;
//            float b = (color & 0xFF) / 255F;
//            tessellator.getBuffer().pos(0, 0, 0).color(r, g, b, 1F - f2).endVertex();
//            tessellator.getBuffer().pos(-0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
//            tessellator.getBuffer().pos(0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
//            tessellator.getBuffer().pos(0, f3, 1F * f4).color(0, 0, 0, 0).endVertex();
//            tessellator.getBuffer().pos(-0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
//            tessellator.draw();
//        }
//
//        GlStateManager.depthMask(true);
//        GlStateManager.disableCull();
//        GlStateManager.disableBlend();
//        GlStateManager.shadeModel(GL11.GL_FLAT);
//        GlStateManager.color(1F, 1F, 1F, 1F);
//        GlStateManager.enableTexture2D();
//        GlStateManager.enableAlpha();
//        GlStateManager.popMatrix();
    }





}
