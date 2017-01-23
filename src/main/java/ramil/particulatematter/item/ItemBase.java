package ramil.particulatematter.item;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import ramil.particulatematter.ParticulateMatter;

public class ItemBase extends Item {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel() {
        System.out.println("Model registered " + name);
        ParticulateMatter.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}
