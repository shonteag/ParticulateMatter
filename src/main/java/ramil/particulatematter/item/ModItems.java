package ramil.particulatematter.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    // items
    public static ItemParticulateWrench particulateWrench;
    public static ItemParticleContainer particleContainer;

    public static void init() {
        particulateWrench = register(new ItemParticulateWrench());
        particleContainer = register(new ItemParticleContainer());

        particulateWrench.setCreativeTab(CreativeTabs.TOOLS);
        particleContainer.setCreativeTab(CreativeTabs.TOOLS);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {

    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemBase) {
            ((ItemBase)item).registerItemModel();
        }

        return item;
    }

}
