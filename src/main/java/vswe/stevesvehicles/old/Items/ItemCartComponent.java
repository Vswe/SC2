package vswe.stevesvehicles.old.Items;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.old.StevesVehicles;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.world.World;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vswe.stevesvehicles.old.Helpers.EntityEasterEgg;
import vswe.stevesvehicles.tab.CreativeTabLoader;

public class ItemCartComponent extends Item {

	private IIcon icons[];
	private IIcon unknownIcon;
	public static int size () {
		return ComponentTypes.values().length;
	}

    public ItemCartComponent() {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabLoader.components);
    }

	private String getName(int dmg) {
		return ComponentTypes.values()[dmg].getUnlocalizedName();
	}


	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dmg) {
		if (dmg < 0 || dmg >= icons.length || icons[dmg] == null) {
			return unknownIcon;
		}else{
			return icons[dmg];
		}
    }	

	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
		icons = new IIcon[size()];
		for (int i = 0; i < icons.length; i++) {
			if (getName(i) != null) {
				icons[i] = register.registerIcon(StevesVehicles.instance.textureHeader + ":components/" + getName(i));
			}
		}
		unknownIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":unknown");
    }
	
	@Override
    public String getUnlocalizedName(ItemStack item) {
		if (item == null || item.getItemDamage() < 0 || item.getItemDamage() >= size() || getName(item.getItemDamage()) == null) {
			return getUnlocalizedName();
		}else{
			return "steves_vehicles:item.component:" + getName(item.getItemDamage());
		}
    }
	
	@Override
    public String getUnlocalizedName() {
        return "steves_vehicles:item.component:unknown_component.name";
	}

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List lst, boolean useExtraInfo) {
		if (item == null || item.getItemDamage() < 0 || item.getItemDamage() >= size() || getName(item.getItemDamage()) == null) {
			if (item != null && item.getItem() instanceof ItemCartComponent){
				lst.add("Component id " + item.getItemDamage()); //TODO localize
			}else{
				lst.add("Unknown component id"); //TODO localize
			}
		}
	}

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List lst) {
		for (int i = 0; i < size(); i++) {
			ItemStack iStack = new ItemStack(item, 1, i);
			if (isValid(iStack)) {
				lst.add(iStack);
			}
        }
    }


	public boolean isValid(ItemStack item) {
		if (item == null || !(item.getItem() instanceof ItemCartComponent) || getName(item.getItemDamage()) == null) {
			return false;
		}

        ComponentTypes type = ComponentTypes.values()[item.getItemDamage()];
        return type.getRequiredHoliday() == null || StevesVehicles.holidays.contains(type.getRequiredHoliday());
	}
		
		

		
	//EASTER STUFF
	private boolean isEdibleEgg(ItemStack item) {
		return item != null && (
                item.getItemDamage() == ComponentTypes.EXPLOSIVE_EASTER_EGG.getId() ||
                item.getItemDamage() == ComponentTypes.BURNING_EASTER_EGG.getId() ||
                item.getItemDamage() == ComponentTypes.GLISTERING_EASTER_EGG.getId() ||
                item.getItemDamage() == ComponentTypes.CHOCOLATE_EASTER_EGG.getId()
        );
	}	
	
	private boolean isThrowableEgg(ItemStack item) {
		return item != null && item.getItemDamage() == ComponentTypes.PAINTED_EASTER_EGG.getId();
	}
	
	@Override
    public ItemStack onEaten(ItemStack item, World world, EntityPlayer player) {
		if (isEdibleEgg(item)) {
			if (item.getItemDamage() == ComponentTypes.EXPLOSIVE_EASTER_EGG.getId()) {
				//Explosive Easter Egg
				
				world.createExplosion(null, player.posX, player.posY, player.posZ, 0.1F, false);		
			}else if (item.getItemDamage() == ComponentTypes.BURNING_EASTER_EGG.getId()) {
				//Burning Easter Egg
				
				player.setFire(5);
				
				if (!world.isRemote) {
					player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 600, 0));
				}					
			}else if (item.getItemDamage() == ComponentTypes.GLISTERING_EASTER_EGG.getId()) {
				//Glistering Easter Egg
			
				if (!world.isRemote) {
					player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 50, 2));
				}	
			}else if (item.getItemDamage() == ComponentTypes.CHOCOLATE_EASTER_EGG.getId()) {
				//Chocolate Easter Egg

				if (!world.isRemote) {
					player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 300, 4));
				}								
			}
			

		
			if (!player.capabilities.isCreativeMode) {
				--item.stackSize;
			}
			world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			player.getFoodStats().addStats(2, 0);
			return item;
		}else{
			return super.onEaten(item, world, player);
		}
		
    }	
	
	@Override
    public int getMaxItemUseDuration(ItemStack item)  {
        return isEdibleEgg(item) ? 32 : super.getMaxItemUseDuration(item);
    }

	@Override
    public EnumAction getItemUseAction(ItemStack item) {
		return isEdibleEgg(item) ? EnumAction.eat : super.getItemUseAction(item);
    }		
	
	@Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player){
        if (isEdibleEgg(item)) {
            player.setItemInUse(item, this.getMaxItemUseDuration(item));
			return item;
		}else if(isThrowableEgg(item)) {
			if (!player.capabilities.isCreativeMode) {
				--item.stackSize;
			}
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntityEasterEgg(world, player));
			}
			return item;
        }else{
			return super.onItemRightClick(item, world, player);
		}
       
    }	
		
}
