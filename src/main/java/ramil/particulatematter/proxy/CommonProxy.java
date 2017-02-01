package ramil.particulatematter.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ramil.particulatematter.ParticulateMatter;
import ramil.particulatematter.network.PacketHandler;

import static ramil.particulatematter.ParticulateMatter.instance;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {

    }

    public void preInit(FMLPreInitializationEvent e) {
        PacketHandler.registerMessages(ParticulateMatter.mod_id);
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {

    }

}
