package ramil.particulatematter.block;


import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ramil.particulatematter.laser.BlockLaser;
import ramil.particulatematter.laser.RendererLaser;
import ramil.particulatematter.chamber.BlockParticleChamber;
import ramil.particulatematter.chamber.RendererParticleChamber;
import ramil.particulatematter.chamber.TileEntityParticleChamber;
import ramil.particulatematter.laser.TileEntityLaser;

public class ModBlocks {

    public static BlockCentrifuge blockCentrifuge;

    public static BlockParticleChamber blockParticleChamber;
    public static BlockLaser blockLaser;

    public static void init() {
        blockCentrifuge = register(new BlockCentrifuge());
        blockParticleChamber = register(new BlockParticleChamber());
        blockLaser = register(new BlockLaser());
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityParticleChamber.class, new RendererParticleChamber());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaser.class, new RendererLaser());
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
