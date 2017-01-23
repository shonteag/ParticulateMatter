package ramil.particulatematter.block;


import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ramil.particulatematter.particlegen.BlockParticleChamber;
import ramil.particulatematter.particlegen.RendererParticleChamber;
import ramil.particulatematter.particlegen.TileEntityParticleChamber;

public class ModBlocks {

    public static BlockCentrifuge blockCentrifuge;

    public static BlockParticleChamber blockParticleChamber;
    public static BlockLaser blockLaser;

    public static void init() {
        blockCentrifuge = register(new BlockCentrifuge());

        blockParticleChamber = register(new BlockParticleChamber());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityParticleChamber.class, new RendererParticleChamber());

        blockLaser = register(new BlockLaser());
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BlockBase) {
            ((BlockBase)block).registerItemModel(itemBlock);
        }

        if (block instanceof BlockTileEntity) {
            GameRegistry.registerTileEntity(((BlockTileEntity<?>)block).getTileEntityClass(), block.getRegistryName().toString());
        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }
}
