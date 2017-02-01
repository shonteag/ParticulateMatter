package ramil.particulatematter.laser;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import ramil.particulatematter.util.RenderUtil;


public class RendererLaser extends TileEntitySpecialRenderer<TileEntityLaser> {

    @Override
    public void renderTileEntityAt(TileEntityLaser te, double x, double y, double z, float partialTicks, int destroyStage) {

        if (te.getLinked() != null && te.fired_this_tick) {

            float tx = te.getPos().getX() + .5F;
            float ty = te.getPos().getY() + .5F;
            float tz = te.getPos().getZ() + .5F;

            float cx = te.link.getX() + .5F;
            float cy = te.link.getY() + .5F;
            float cz = te.link.getZ() + .5F;

            RenderUtil.renderLaser(tx, ty, tz, cx, cy, cz, .9F, .04F, SettingsLaser.COLOR_RED);
        }

    }
}
