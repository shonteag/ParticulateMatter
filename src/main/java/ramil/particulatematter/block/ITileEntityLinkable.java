package ramil.particulatematter.block;


import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ITileEntityLinkable {

    // if the block can link, return true
    public boolean canLinkFrom();

    // get a list of all TileEntity classes that this TileEntity can link to.
    public List<Class<? extends TileEntity>> canLinkTo();

    // can this instance of TileEntity link?
    public boolean canLinkTo(TileEntity te_link_to);

    // establish the link
    public boolean establishLink(TileEntity te_link_to);

}
