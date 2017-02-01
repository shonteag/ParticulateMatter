package ramil.particulatematter;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import ramil.particulatematter.block.ModBlocks;
import ramil.particulatematter.item.ModItems;
import ramil.particulatematter.proxy.CommonProxy;

@Mod(modid = ParticulateMatter.mod_id, name = ParticulateMatter.name, version = ParticulateMatter.version)
public class ParticulateMatter {

    public static final String mod_id = "particulatematter";
    public static final String name = "Particulate Matter";
    public static final String version = "0.0.1";

    public static boolean tesla_loaded = false;

    @Mod.Instance(mod_id)
    public static ParticulateMatter instance;

    @SidedProxy(serverSide = "ramil.particulatematter.proxy.CommonProxy", clientSide = "ramil.particulatematter.proxy.ClientProxy")
    public static ramil.particulatematter.proxy.CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(name + " loading...");

        System.out.println(name + ", detecting integrated mods...");
        if (Loader.isModLoaded("tesla")) {
            System.out.println(name + ", detected 'tesla'");
            tesla_loaded = true;
        }

        ModItems.init();
        ModBlocks.init();

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }
}