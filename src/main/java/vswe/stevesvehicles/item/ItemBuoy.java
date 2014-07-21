package vswe.stevesvehicles.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vswe.stevesvehicles.buoy.EntityBuoy;
import vswe.stevesvehicles.tab.CreativeTabLoader;
import vswe.stevesvehicles.buoy.BuoyType;

import java.util.List;

public class ItemBuoy extends Item {

    public ItemBuoy() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabLoader.blocks);
    }


 	@Override
    public String getUnlocalizedName(ItemStack item) {
		if (item != null && item.getItemDamage() >= 0 && item.getItemDamage() < BuoyType.values().length) {
            BuoyType buoy = BuoyType.getType(item.getItemDamage());
			return buoy.getUnlocalizedName();
		}	
	
        return "item.unknown";
    }	

 	
    @Override
    public int getMetadata(int dmg) {
        return dmg;
    }

    private static final int VIEW_MULTIPLIER = 5;
    private static final double AREA_EXPANSION = 1.0;

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        double x = player.posX;
        double y = player.posY + 1.62D - (double)player.yOffset;
        double z = player.posZ;
        Vec3 camera = Vec3.createVectorHelper(x, y, z);

        Vec3 look = player.getLook(1.0F);
        look.xCoord *= VIEW_MULTIPLIER;
        look.yCoord *= VIEW_MULTIPLIER;
        look.zCoord *= VIEW_MULTIPLIER;

        Vec3 target = camera.addVector(look.xCoord, look.yCoord, look.zCoord);
        MovingObjectPosition object = world.rayTraceBlocks(camera, target, true);

        if (object != null && object.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {



            //noinspection unchecked
            List<Entity> list = world.getEntitiesWithinAABB(Entity.class, player.boundingBox.addCoord(look.xCoord, look.yCoord, look.zCoord).expand(AREA_EXPANSION, AREA_EXPANSION, AREA_EXPANSION));

            boolean valid = true;
            for (Entity entity : list) {
                if (entity.canBeCollidedWith()) {
                    float borderSize = entity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity.boundingBox.expand(borderSize, borderSize, borderSize);

                    if (axisalignedbb.isVecInside(camera)) {
                        valid = false;
                        break;
                    }
                }
            }



            if (valid) {
                Block block = world.getBlock( object.blockX, object.blockY, object.blockZ);

                if (block != null && block.getMaterial() == Material.water) { //TODO allow lava?
                    int targetX = object.blockX;
                    int targetY = object.blockY + 1;
                    int targetZ = object.blockZ;


                    if (world.isAirBlock(targetX, targetY, targetZ)) {
                        if (!world.isRemote) {
                            EntityBuoy buoy = new EntityBuoy(world, targetX, targetY, targetZ);
                            world.spawnEntityInWorld(buoy);
                        }
                        if (!player.capabilities.isCreativeMode) {
                            item.stackSize--;
                        }
                    }

                }

            }
        }

        return item;
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }
}
