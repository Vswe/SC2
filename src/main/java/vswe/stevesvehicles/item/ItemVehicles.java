package vswe.stevesvehicles.item;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vswe.stevesvehicles.localization.entry.info.LocalizationLabel;
import vswe.stevesvehicles.module.data.ModuleDataItemHandler;
import vswe.stevesvehicles.module.data.ModuleDataPair;
import vswe.stevesvehicles.StevesVehicles;
import vswe.stevesvehicles.util.Tuple;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.VehicleRegistry;
import vswe.stevesvehicles.vehicle.VehicleType;
import vswe.stevesvehicles.vehicle.entity.IVehicleEntity;
import vswe.stevesvehicles.vehicle.version.VehicleVersion;
import vswe.stevesvehicles.client.gui.ColorHelper;
import vswe.stevesvehicles.GeneratedInfo;
import vswe.stevesvehicles.module.data.ModuleData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemVehicles extends Item {

	
	
	
    public ItemVehicles() {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(null);	
    }

	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        for (VehicleType vehicleType : VehicleRegistry.getInstance().getAllVehicles()) {
            vehicleType.registerIcons(register);
        }
        fallbackFallbackIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":unknown");
    }

    @SideOnly(Side.CLIENT)
    private IIcon fallbackFallbackIcon; //if it fails to use a fallback icon :P

    //this will only be used if the 3d rendering fails for some reason
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg) {
        VehicleType type = VehicleRegistry.getInstance().getTypeFromId(dmg);
        if (type != null) {
            return type.getFallbackIcon();
        }else{
            return fallbackFallbackIcon;
        }
    }

    private VehicleType getVehicleType(ItemStack item) {
        VehicleVersion.updateItemStack(item);
        return VehicleRegistry.getInstance().getTypeFromId(item.getItemDamage());
    }

    //TODO let the registered elements decide when they should be placed
	@Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        VehicleType vehicle = getVehicleType(item);
        if (vehicle != null && vehicle == VehicleRegistry.CART) {
            if (BlockRailBase.func_150049_b_(world, x, y, z)) {
                return placeVehicle(vehicle, player, item, world, x + 0.5, y + 0.5, z + 0.5) != null || world.isRemote;
            }
        }
        return false;
    }

    //TODO let the registered elements decide when they should be placed
    //TODO clean this up
    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        VehicleType vehicle = getVehicleType(item);
        if (vehicle != null && vehicle == VehicleRegistry.BOAT) {
            float f = 1.0F;
            float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
            float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)f + 1.62D - (double)player.yOffset;
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
            Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
            float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = 5.0D;
            Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
            MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3, vec31, true);

            if (movingobjectposition != null) {

                Vec3 vec32 = player.getLook(f);
                boolean flag = false;
                float f9 = 1.0F;
                List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double)f9, (double)f9, (double)f9));
                int i;

                for (i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity)list.get(i);

                    if (entity.canBeCollidedWith()) {
                        float f10 = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f10, (double)f10, (double)f10);

                        if (axisalignedbb.isVecInside(vec3)) {
                            flag = true;
                        }
                    }
                }

                if (!flag) {

                    if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        i = movingobjectposition.blockX;
                        int j = movingobjectposition.blockY;
                        int k = movingobjectposition.blockZ;

                        if (world.getBlock(i, j, k) == Blocks.snow_layer) {
                            --j;
                        }

                        Entity boat = placeVehicle(vehicle, player, item, world, i + 0.5F, j + 1.0F, k + 0.5F);
                        if (boat != null) {
                            boat.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                            if (!world.getCollidingBoundingBoxes(boat, boat.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                                boat.setDead();
                            }
                        }
                    }

                }
            }
        }

        return item;
    }

    private Entity placeVehicle(VehicleType vehicle, EntityPlayer player, ItemStack item, World world, double x, double y, double z) {
        if (!world.isRemote) {
            try {
                NBTTagCompound info = item.getTagCompound();
                if (info != null) {
                    if (!info.hasKey(VehicleBase.NBT_INTERRUPT_MAX_TIME)) {
                        Class<? extends IVehicleEntity> clazz = vehicle.getClazz();
                        Constructor<? extends IVehicleEntity> constructor = clazz.getConstructor(World.class, double.class, double.class, double.class, NBTTagCompound.class, String.class);
                        Object obj = constructor.newInstance(world, x, y, z, info, item.hasDisplayName() ? item.getDisplayName() : null);
                        world.spawnEntityInWorld((Entity)obj);
                        if (!player.capabilities.isCreativeMode) {
                            --item.stackSize;
                        }
                        return (Entity)obj;
                    }
                }

                return null;
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            if (!player.capabilities.isCreativeMode) {
                --item.stackSize;
            }
            return null;
        }
    }


    @SideOnly(Side.CLIENT)
	@Override
    /**
     * allows items to add custom lines of information to the mouse over description
     */
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean useExtraInfo) {
		VehicleVersion.updateItemStack(item);

        NBTTagCompound info = item.getTagCompound();

		if (info != null) {
            addInfo(ModuleDataItemHandler.getModulesAndCompoundsFromItem(item), list, null);
            addInfo(ModuleDataItemHandler.getSpareModulesAndCompoundsFromItem(item), list, ColorHelper.ORANGE);

			if (info.hasKey(VehicleBase.NBT_INTERRUPT_MAX_TIME)) {
				list.add(ColorHelper.RED + LocalizationLabel.INCOMPLETE.translate());
                list.add(ColorHelper.RED + LocalizationLabel.INTERRUPT_INSTRUCTION.translate());
				int maxTime = info.getInteger(VehicleBase.NBT_INTERRUPT_MAX_TIME);
				int currentTime = info.getInteger(VehicleBase.NBT_INTERRUPT_TIME);
				int timeLeft = maxTime - currentTime;
				list.add(ColorHelper.RED + LocalizationLabel.TIME_LEFT.translate() + ": " + formatTime(timeLeft));

			}

			if (GeneratedInfo.inDev) {
                //dev version only, no localization required
				list.add(ColorHelper.WHITE + "Version: " + (info.hasKey("CartVersion") ? info.getByte("CartVersion") : 0));
			}
		}else{
			list.add(LocalizationLabel.NO_MODULES.translate());
		}
	}

    private void addInfo(List<Tuple<ModuleData, NBTTagCompound>> modules, List list, ColorHelper color) {
        if (modules != null) {

            ArrayList<ModuleDataPair> counts = new ArrayList<ModuleDataPair>();

            for (Tuple<ModuleData, NBTTagCompound> moduleTuple : modules) {
                ModuleData module = moduleTuple.getFirstObject();
                NBTTagCompound moduleCompound = moduleTuple.getSecondObject();
                boolean found = false;
                if (module.hasExtraData()) {
                    for (ModuleDataPair count : counts) {
                        if (count.isContainingData(module)) {
                            count.increase();
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    ModuleDataPair count = new ModuleDataPair(module);
                    if (module.hasExtraData()) {
                        count.setExtraData(moduleCompound);
                    }
                    counts.add(count);
                }
            }

            for (ModuleDataPair count : counts) {
                if (color != null) {
                    list.add(color + count.toString());
                }else{
                    list.add(count.toString());
                }

            }
        }
    }
	
	private String formatTime(int ticks) {
		int seconds = ticks / 20;
		ticks -= seconds * 20;
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		int hours = minutes / 60;
		minutes -= hours * 60;
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}	
	

    @Override
    public String getUnlocalizedName(ItemStack item) {
        int dmg = item.getItemDamage();
        VehicleType type = VehicleRegistry.getInstance().getTypeFromId(dmg);
        if (type != null) {
            return type.getUnlocalizedNameForItem();
        }else{
            return "item.unknown";
        }
    }
}
