package vswe.stevescarts.Blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;


public abstract class BlockContainerBase extends BlockContainer implements IBlockBase {
    private String unlocalizedName;
    protected BlockContainerBase(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public void setUnlocalizedName(String name) {
        this.unlocalizedName = name;
    }
}
