package vswe.stevesvehicles.block;

import net.minecraft.block.BlockRailBase;


public class BlockSpecialRailBase extends BlockRailBase implements IBlockBase {
    private String unlocalizedName;
    protected BlockSpecialRailBase(boolean p_i45389_1_) {
        super(p_i45389_1_);
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
