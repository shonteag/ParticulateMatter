package ramil.particulatematter.chamber;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.util.widget.EnergyBar;
import ramil.particulatematter.util.widget.ParticleBar;

public class GuiContainerParticleChamber extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(ParticulateMatter.mod_id, "textures/gui/particle_chamber.png");

    private TileEntityParticleChamber te;
    private EnergyBar energyBar;
    private ParticleBar particleBar;

    public GuiContainerParticleChamber(TileEntityParticleChamber tileEntity, ContainerParticleChamber container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;

        this.te = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.particleBar = new ParticleBar(this.guiLeft + 135, this.guiTop + 10, te);
        this.energyBar = new EnergyBar(this.guiLeft + 160, this.guiTop + 10, te.storage);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        this.energyBar.draw();
        this.particleBar.draw();
    }
}