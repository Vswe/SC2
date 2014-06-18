package vswe.stevescarts.Renders;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import vswe.stevescarts.Blocks.BlockCartAssembler;
import vswe.stevescarts.Blocks.BlockUpgrade;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import vswe.stevescarts.TileEntities.TileEntityBase;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.Tessellator;

public class RendererUpgrade implements ISimpleBlockRenderingHandler
{
	private int id;
    public RendererUpgrade()
    {

		id = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		//renderer.renderStandardBlock(block, x, y, z);
	}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
	
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityCartAssembler) {
			TileEntityCartAssembler assembler = (TileEntityCartAssembler)te;
			BlockCartAssembler b = (BlockCartAssembler)block;
			

			renderer.renderStandardBlock(block, x, y, z);
			
	
			return true;
		}else if(te instanceof TileEntityUpgrade){
			TileEntityUpgrade upgrade = (TileEntityUpgrade)te;
			BlockUpgrade b = (BlockUpgrade)block;
			


			int side = b.setUpgradeBounds(world, x, y, z);
			Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			if (side != -1) {
				renderer.renderFaceYPos(block, x, y, z, upgrade.getTexture(side == 0));
				renderer.renderFaceYNeg(block, x, y, z, upgrade.getTexture(side == 1));
				renderer.renderFaceZNeg(block, x, y, z, upgrade.getTexture(side == 2));
				renderer.renderFaceXPos(block, x, y, z, upgrade.getTexture(side == 3));
				renderer.renderFaceZPos(block, x, y, z, upgrade.getTexture(side == 4));
				renderer.renderFaceXNeg(block, x, y, z, upgrade.getTexture(side == 5));
			}
			
			return true;
		}else{
			return false;
		}
	}

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }


    @Override
    public int getRenderId() {
		return id;
	}

}
