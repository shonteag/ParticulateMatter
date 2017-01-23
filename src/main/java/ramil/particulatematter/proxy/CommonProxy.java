package ramil.particulatematter.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.network.PacketHandler;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {

    }

    public void preInit(FMLPreInitializationEvent e) {
        PacketHandler.registerMessages(ParticulateMatter.mod_id);
    }

}
