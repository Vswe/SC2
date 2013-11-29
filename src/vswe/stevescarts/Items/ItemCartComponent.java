package vswe.stevescarts.Items;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.StevesCarts;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Icon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.world.World;
import net.minecraft.util.FoodStats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vswe.stevescarts.Helpers.EntityEasterEgg;
public class ItemCartComponent extends Item
{
    private static String components[] = new String[]
    {
        "Wooden Wheels",
        "Iron Wheels",
		"Red Pigment",
		"Green Pigment",
		"Blue Pigment",
		"Glass o'Magic",
		"Dynamite",
		null,
		null,
		"Simple PCB",
		"Graphical Interface",
		"Raw Handle",
		"Refined Handle",
		"Speed Handle",
		"Wheel",
		"Saw Blade",
		"Advanced PCB",
		"Wood Cutting Core",
		"Raw Hardener",
		"Refined Hardener",
		"Hardened Mesh",
		"Stabilized Metal",
		"Reinforced Metal",
		"Reinforced Wheels",
		"Pipe",
		"Shooting Station",
		"Entity Scanner",
		"Entity Analyzer",
		"Empty Disk",
		"Tri-torch",
		"Chest Pane",
		"Large Chest Pane",
		"Huge Chest Pane",
		"Chest Lock",
		"Iron Pane",
		"Large Iron Pane",
		"Huge Iron Pane",
		"Dynamic Pane",
		"Large Dynamic Pane",
		"Huge Dynamic Pane",
		"Cleaning Fan",
		"Cleaning Core",
		"Cleaning Tube",
		"Fuse",
		"Solar Panel",
		"Eye of Galgador",
		"Lump of Galgador",
		"Galgadorian Metal",
		"Large Lump of Galgador",
		"Enhanced Galgadorian Metal", 
		"Stolen Present",
		"Green Wrapping Paper", 
		"Red Wrapping Paper",
		"Warm hat",
		"Red Gift Ribbon",
		"Yellow Gift Ribbon",
		"Sock",
		"Stuffed Sock",
		"Advanced Solar Panel",
		"Blank Upgrade",
		"Tank Valve",
		"Tank Pane",
		"Large Tank Pane",
		"Huge Tank Pane",
		"Liquid Cleaning Core",
		"Liquid Cleaning Tube",
		"Explosive Easter Egg",
		"Burning Easter Egg",
		"Glistering Easter Egg",
		"Chocolate Easter Egg",
		"Painted Easter Egg",
		"Basket",
		"Oak Log",
		"Oak Twig",
		"Spruce Log",
		"Spruce Twig",
		"Birch Log",
		"Birch Twig",
		"Jungle Log",
		"Jungle Twig",
		"Hardened Saw Blade",
		"Galgadorian Saw Blade",
		"Galgadorian Wheels",
		"Iron Blade",
		"Blade Arm"		
    };

	private Icon icons[];
	private Icon unknownIcon;
	public static int size () {
		return components.length;
	}

    public ItemCartComponent(int i)
    {
        super(i);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(StevesCarts.tabsSC2Components);	
    }

	private String getName(int dmg) {
		return components[dmg];
	}
	
	public String getName(ItemStack par1ItemStack)
    {
		if (par1ItemStack == null || par1ItemStack.getItemDamage() < 0 || par1ItemStack.getItemDamage() >= size() || getName(par1ItemStack.getItemDamage()) == null) {
			return "Unknown SC2 Component";
		}else{
			return getName(par1ItemStack.getItemDamage());
		}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int dmg)
    {
		if (dmg < 0 || dmg >= icons.length || icons[dmg] == null) {
			return unknownIcon;
		}else{
			return icons[dmg];
		}
    }	
	
	private String getRawName(int i) {
		return components[i].replace(":","").replace(" ","_").toLowerCase();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
		icons = new Icon[size()];
		for (int i = 0; i < icons.length; i++) {
			if (getName(i) != null) {
				icons[i] = register.registerIcon(StevesCarts.instance.textureHeader + ":" + getRawName(i) + "_icon");
			}
		}
		unknownIcon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + "unknown_icon");	
    }
	
	@Override
    public String getUnlocalizedName(ItemStack item)
    {
		if (item == null || item.getItemDamage() < 0 || item.getItemDamage() >= size() || getName(item.getItemDamage()) == null) {
			return getUnlocalizedName();
		}else{
			return "item." + StevesCarts.instance.localStart + getRawName(item.getItemDamage());
		}
    }
	
	@Override
    public String getUnlocalizedName() {
	
		return "item." + StevesCarts.instance.localStart + "unknowncomponent";	
	}	
	
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
	
		if (par1ItemStack == null || par1ItemStack.getItemDamage() < 0 || par1ItemStack.getItemDamage() >= size() || getName(par1ItemStack.getItemDamage()) == null) {
			if (par1ItemStack != null && par1ItemStack.getItem() instanceof ItemCartComponent){
				par3List.add("Component id " + par1ItemStack.getItemDamage());
			}else{
				par3List.add("Unknown component id");
			}
		}
	}		
	
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
		for (int i = 0; i < components.length; i++) {
			ItemStack iStack = new ItemStack(par1, 1, i);
			if (isValid(iStack)) {
				par3List.add(iStack);
			}
        }
    }


	public boolean isValid(ItemStack item) {
		if (item == null || !(item.getItem() instanceof ItemCartComponent) || getName(item.getItemDamage()) == null) {
			return false;
		}
	
		if (item.getItemDamage() >= 50 && item.getItemDamage() < 58) {
			return StevesCarts.isChristmas;
		}
		if (item.getItemDamage() >= 66 && item.getItemDamage() < 72) {
			return StevesCarts.isEaster;
		}		
		
		if (item.getItemDamage() >= 72 && item.getItemDamage() < 80) {
			return false;
		}
		
		return true;
	}
		
		
	//WOOD STUFF
	public static ItemStack getWood(int type, boolean isLog) {
		return getWood(type, isLog, 1);
	}
	
	public static ItemStack getWood(int type, boolean isLog, int count) {
		return new ItemStack(StevesCarts.component, count, 72 + type * 2 + (isLog ? 0 : 1));
	}
	
	public static boolean isWoodLog(ItemStack item) {
		if (item != null && item.getItemDamage() >= 72 && item.getItemDamage() < 80) {
			return (item.getItemDamage() - 72) % 2 == 0;
		}else{
			return false;
		}		
	}
	
	public static boolean isWoodTwig(ItemStack item) {
		if (item != null && item.getItemDamage() >= 72 && item.getItemDamage() < 80) {
			return (item.getItemDamage() - 72) % 2 == 1;
		}else{
			return false;
		}		
	}		
		
	//EASTER STUFF
	private boolean isEdibleEgg(ItemStack item) {
		return item != null && item.getItemDamage() >= 66 && item.getItemDamage() < 70;
	}	
	
	private boolean isThrowableEgg(ItemStack item) {
		return item != null && item.getItemDamage() == 70;
	}
	
	@Override
    public ItemStack onEaten(ItemStack item, World world, EntityPlayer player)
    {
		if (isEdibleEgg(item)) {
			if (item.getItemDamage() == 66) {
				//Explosive Easter Egg
				
				world.createExplosion(null, player.posX, player.posY, player.posZ, 0.1F, false);		
			}else if (item.getItemDamage() == 67) {
				//Burning Easter Egg
				
				player.setFire(5);
				
				if (!world.isRemote)
				{
					player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 600, 0));
				}					
			}else if (item.getItemDamage() == 68) {
				//Glistering Easter Egg
			
				if (!world.isRemote)
				{
					player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 50, 2));
				}	
			}else if (item.getItemDamage() == 69) {
				//Chocolate Easter Egg

				if (!world.isRemote)
				{
					player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 300, 4));
				}								
			}else if (item.getItemDamage() == 70) {
				//Colorful Easter Egg
				
			
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
    public int getMaxItemUseDuration(ItemStack item)
    {
        return isEdibleEgg(item) ? 32 : super.getMaxItemUseDuration(item);
    }

	@Override
    public EnumAction getItemUseAction(ItemStack item)
    {
		return isEdibleEgg(item) ? EnumAction.eat : super.getItemUseAction(item);
    }		
	
	@Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
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
