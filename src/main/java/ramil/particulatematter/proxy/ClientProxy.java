package ramil.particulatematter.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.item.ModItems;

public class ClientProxy extends CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ParticulateMatter.mod_id + ":" + id, "inventory"));
    }

}
