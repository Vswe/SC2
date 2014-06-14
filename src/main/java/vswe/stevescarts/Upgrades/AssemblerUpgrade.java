package vswe.stevescarts.Upgrades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.RecipeHelper;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
public class AssemblerUpgrade {


	private static HashMap<Byte, AssemblerUpgrade> upgrades;
	private static HashMap<Byte, IIcon> sides;
	
	public static HashMap<Byte, AssemblerUpgrade> getUpgrades() {
		return upgrades;
	}
	
	public static Collection<AssemblerUpgrade> getUpgradesList() {
		return upgrades.values();
	}
	
	public static AssemblerUpgrade getUpgrade(int id) {
		return upgrades.get((byte)id);
	}
	
	static {
		upgrades = new HashMap<Byte, AssemblerUpgrade>();
		sides = new HashMap<Byte, IIcon>();
	}
	
	public static void init() {

		AssemblerUpgrade batteries = 
		new AssemblerUpgrade(0, "Batteries")
			.addEffect(new FuelCapacity(5000))
			.addEffect(new Recharger(40))
			.addRecipe(new Object[][] {
				{Items.redstone,Items.redstone,Items.redstone},
				{Items.redstone,Items.diamond,Items.redstone},
				{Items.redstone, ComponentTypes.BLANK_UPGRADE.getItemStack(),Items.redstone}
			});	
			
		new AssemblerUpgrade(1, "Power Crystal")
			.addEffect(new FuelCapacity(15000))
			.addEffect(new Recharger(150))
			.addRecipe(new Object[][] {
				{Items.diamond,Items.glowstone_dust,Items.diamond},
				{Items.glowstone_dust, Blocks.emerald_block,Items.glowstone_dust},
				{Items.diamond,batteries.getItemStack(),Items.diamond}
			});		
			
		AssemblerUpgrade knowledge = 
		new AssemblerUpgrade(2, "Module knowledge")
			.addEffect(new TimeFlat(-750))
			.addEffect(new TimeFlatCart(-5000))
			.addEffect(new WorkEfficiency(-0.01F))
			.addRecipe(new Object[][] {
				{Items.book,Blocks.bookshelf,Items.book},
				{Blocks.bookshelf,Blocks.enchanting_table,Blocks.bookshelf},
				{ComponentTypes.REINFORCED_METAL.getItemStack() ,ComponentTypes.BLANK_UPGRADE.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()}
			});
			
		new AssemblerUpgrade(3, "Industrial espionage")
			.addEffect(new TimeFlat(-2500))
			.addEffect(new TimeFlatCart(-14000))
			.addEffect(new WorkEfficiency(-0.01F))
			.addRecipe(new Object[][] {
				{Blocks.bookshelf,  ComponentTypes.REINFORCED_METAL.getItemStack(), Blocks.bookshelf},
				{ComponentTypes.EYE_OF_GALGADOR.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.EYE_OF_GALGADOR.getItemStack()},
				{ ComponentTypes.REINFORCED_METAL.getItemStack() ,knowledge.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()}
			});		
		
		ItemStack books[] = new ItemStack[5];
		for (int i = 0; i < 5; i++) {
			books[i] = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.efficiency,i+1));
		}
		
		AssemblerUpgrade experienced = 
		new AssemblerUpgrade(4, "Experienced assembler")
			.addEffect(new WorkEfficiency(0.10F))
			.addEffect(new FuelCost(0.3F))
			.addRecipe(new Object[][] {
					{ComponentTypes.SIMPLE_PCB, books[0], ComponentTypes.SIMPLE_PCB},
					{ ComponentTypes.REINFORCED_METAL.getItemStack() , ComponentTypes.ADVANCED_PCB.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()},
					{ ComponentTypes.REINFORCED_METAL.getItemStack() , ComponentTypes.BLANK_UPGRADE.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()}
				})
			.addRecipe(new Object[][] {
					{Items.redstone,books[1],Items.redstone},
					{ ComponentTypes.REINFORCED_METAL.getItemStack() , ComponentTypes.ADVANCED_PCB.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()},
					{ ComponentTypes.REINFORCED_METAL.getItemStack() ,ComponentTypes.BLANK_UPGRADE.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()}
				})
			.addRecipe(new Object[][] {
					{Items.redstone,books[2],Items.redstone},
					{Items.iron_ingot, ComponentTypes.ADVANCED_PCB.getItemStack(),Items.iron_ingot},
					{ ComponentTypes.REINFORCED_METAL.getItemStack() ,ComponentTypes.BLANK_UPGRADE.getItemStack(),  ComponentTypes.REINFORCED_METAL.getItemStack()}
				})
			.addRecipe(new Object[][] {
					{null,books[3], null},
					{Items.iron_ingot, ComponentTypes.SIMPLE_PCB,Items.iron_ingot},
					{ ComponentTypes.REINFORCED_METAL.getItemStack() ,ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}
				})
			.addRecipe(new Object[][] {
					{null,books[4], null},
					{null,Items.redstone,null},
					{Items.iron_ingot,ComponentTypes.BLANK_UPGRADE.getItemStack(),Items.iron_ingot}
				});

				
		new AssemblerUpgrade(5, "New Era")
			.addEffect(new WorkEfficiency(1.00F))	
			.addEffect(new FuelCost(30.00F))
			.addRecipe(new Object[][] {
					{ComponentTypes.GALGADORIAN_METAL.getItemStack(),books[4], ComponentTypes.GALGADORIAN_METAL.getItemStack()},
					{ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.ADVANCED_PCB, ComponentTypes.GALGADORIAN_METAL.getItemStack()},
					{ComponentTypes.GALGADORIAN_METAL.getItemStack(),experienced.getItemStack(),ComponentTypes.GALGADORIAN_METAL.getItemStack()}
				});
				
		new AssemblerUpgrade(6, "CO2 friendly")
			.addEffect(new FuelCost(-0.15F))
			.addRecipe(new Object[][] {
					{null,Blocks.piston, null},
					{ComponentTypes.SIMPLE_PCB.getItemStack(),Blocks.fence, ComponentTypes.SIMPLE_PCB.getItemStack()},
					{ComponentTypes.CLEANING_FAN, ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.CLEANING_FAN}
				});
				
		new AssemblerUpgrade(7, "Generic engine")
			.addEffect(new CombustionFuel())	
			.addEffect(new FuelCost(0.05F))
			.addRecipe(new Object[][] {
					{null, ComponentTypes.SIMPLE_PCB.getItemStack(), null},
					{Blocks.piston,Blocks.furnace, Blocks.piston},
					{Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}
				});			
	
		new AssemblerUpgrade(8, "Module input", 1)
			.addEffect(new InputChest(7, 3))	
			.addRecipe(new Object[][] {
					{null, ComponentTypes.ADVANCED_PCB.getItemStack(), null},
					{Blocks.piston,Blocks.chest, Blocks.piston},
					{Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}
				});		
			
		new AssemblerUpgrade(9, "Production line")
			.addEffect(new Blueprint())				
			.addRecipe(new Object[][] {
					{null, ComponentTypes.SIMPLE_PCB.getItemStack(), null},
					{ComponentTypes.ADVANCED_PCB.getItemStack(),Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack()},
					{ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}
				});
				
		new AssemblerUpgrade(10, "Cart Deployer")
			.addEffect(new Deployer())
			.addRecipe(new Object[][] {
					{Items.iron_ingot, Blocks.rail, Items.iron_ingot},
					{ComponentTypes.SIMPLE_PCB.getItemStack() ,Blocks.piston, ComponentTypes.SIMPLE_PCB.getItemStack()},
					{Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}
				});			

		new AssemblerUpgrade(11, "Cart Modifier")
			.addEffect(new Disassemble())
			.addRecipe(new Object[][] {
					{Items.iron_ingot, null, Items.iron_ingot},
					{ComponentTypes.SIMPLE_PCB.getItemStack() ,Blocks.anvil, ComponentTypes.SIMPLE_PCB.getItemStack()},
					{Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}
				});		
				
		new AssemblerUpgrade(12, "Cart Crane")
			.addEffect(new Transposer())
			.addRecipe(new Object[][] {
					{Blocks.piston, Blocks.rail, Blocks.piston},
					{ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack()},
					{null, ComponentTypes.BLANK_UPGRADE.getItemStack(), null}
				});				

		new AssemblerUpgrade(13, "Redstone Control")
			.addEffect(new Redstone())
			.addRecipe(new Object[][] {
					{ComponentTypes.SIMPLE_PCB.getItemStack(), Items.repeater, ComponentTypes.SIMPLE_PCB.getItemStack()},
					{Blocks.redstone_torch, ComponentTypes.ADVANCED_PCB.getItemStack(), Blocks.redstone_torch},
					{Items.redstone, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.redstone}
				});	
				
		new AssemblerUpgrade(14, "Creative Mode")
			.addEffect(new WorkEfficiency(10000.00F))
			.addEffect(new FuelCost(-1.00F));


		AssemblerUpgrade demolisher = 
		new AssemblerUpgrade(15, "Quick Demolisher")
			.addEffect(new TimeFlatRemoved(-8000))
			.addRecipe(new Object[][] {
				{Blocks.obsidian, ComponentTypes.REINFORCED_METAL.getItemStack(), Blocks.obsidian},
				{ComponentTypes.REINFORCED_METAL.getItemStack() ,Blocks.iron_block, ComponentTypes.REINFORCED_METAL.getItemStack()},
				{Blocks.obsidian, ComponentTypes.BLANK_UPGRADE.getItemStack(), Blocks.obsidian}
			});	

		new AssemblerUpgrade(16, "Entropy")
			.addEffect(new TimeFlatRemoved(-32000))
			.addEffect(new TimeFlat(3000))			
			.addRecipe(new Object[][] {
				{ComponentTypes.EYE_OF_GALGADOR.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(),null},
				{Items.diamond,Blocks.lapis_block,Items.diamond},
				{null,demolisher.getItemStack(), ComponentTypes.EYE_OF_GALGADOR.getItemStack()}
			});		
		
		new AssemblerUpgrade(17, "Manager Bridge")
		.addEffect(new Manager())
		.addEffect(new TimeFlatCart(200))
		.addRecipe(new Object[][] {
				{Items.iron_ingot, Items.ender_pearl, Items.iron_ingot},
				{ComponentTypes.SIMPLE_PCB.getItemStack(), ModBlocks.EXTERNAL_DISTRIBUTOR.getBlock(), ComponentTypes.SIMPLE_PCB.getItemStack()},
				{Items.iron_ingot,  ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}
			});			
			

		new AssemblerUpgrade(18, "Thermal Engine Upgrade")
		.addEffect(new ThermalFuel())
		.addEffect(new FuelCost(0.05F))
		.addRecipe(new Object[][] {
				{Blocks.nether_brick, ComponentTypes.ADVANCED_PCB.getItemStack(), Blocks.nether_brick},
				{Blocks.piston,Blocks.furnace, Blocks.piston},
				{Blocks.obsidian, ComponentTypes.BLANK_UPGRADE.getItemStack(), Blocks.obsidian}
			});			


		
		new AssemblerUpgrade(19, "Solar Panel")
			.addEffect(new Solar())
			.addRecipe(new Object[][] {
					{ComponentTypes.SOLAR_PANEL.getItemStack(), ComponentTypes.SOLAR_PANEL.getItemStack(), ComponentTypes.SOLAR_PANEL.getItemStack()},
					{Items.diamond, Items.redstone,Items.diamond},
					{Items.redstone, ComponentTypes.BLANK_UPGRADE.getItemStack(),Items.redstone}
				});	
		

		
		
	}

	//used to fix the destroy animation
	public static IIcon getStandardIcon() {
		return sides.get((byte)0);
	}
	
	@SideOnly(Side.CLIENT)
	public static void initSides(IIconRegister register) {
		ArrayList<Integer> used = new ArrayList<Integer>();
	
		for (AssemblerUpgrade upgrade : getUpgradesList()) {
			if (!used.contains(upgrade.sideTexture)) {
				sides.put((byte)upgrade.sideTexture, register.registerIcon(StevesCarts.instance.textureHeader + ":upgrade_side_" + upgrade.sideTexture + "_icon"));
				used.add(upgrade.sideTexture);
			}	
		}
	}
	
	
	private byte id;
	private int sideTexture;
	private String name;
	private ArrayList<BaseEffect> effects;
	public AssemblerUpgrade(int id, String name) {
		this(id, name, 0);
	}
	
	public AssemblerUpgrade(int id, String name, int sideTexture) {
		this.id = (byte)id;
		this.sideTexture = sideTexture;
		this.name = name;
		effects = new ArrayList<BaseEffect>();
		
		upgrades.put(this.id, this);
	}	
	
	public byte getId() {
		return id;
	}
	
	public String getName() {
		return StatCollector.translateToLocal("item." + StevesCarts.localStart + getRawName() + ".name");
	}
	
	public AssemblerUpgrade addEffect(BaseEffect effect) {
		effects.add(effect);
		
		return this;
	}

	public AssemblerUpgrade addRecipe(int resultCount, Object[][] recipe) {
		RecipeHelper.addRecipe(getItemStack(resultCount), recipe);
		return this;
	}
	
	public AssemblerUpgrade addRecipe(Object[][] recipe) {
		return addRecipe(1, recipe);
	}
	
	protected ItemStack getItemStack() {
		return getItemStack(1);
	}
	
	protected ItemStack getItemStack(int count) {
		return new ItemStack(ModItems.upgrades, count, id);
	}	
	
	public ArrayList<BaseEffect> getEffects() {
		return effects;
	}
	
	public boolean useStandardInterface() {
		return getInterfaceEffect() == null;
	}
	
	
	
	public int getInventorySize() {
		InventoryEffect inv = getInventoryEffect();
		if (inv != null) {
			return inv.getInventorySize();
		}else{
			return 0;		
		}	
	}
	
	public InterfaceEffect getInterfaceEffect() {
		for (BaseEffect effect : effects) {
			if (effect instanceof InterfaceEffect) {
				return (InterfaceEffect)effect;
			}
		}
		return null;
	}	
	
	public InventoryEffect getInventoryEffect() {
		for (BaseEffect effect : effects) {
			if (effect instanceof InventoryEffect) {
				return ((InventoryEffect)effect);
			}
		}	
		return null;
	}
	
	public TankEffect getTankEffect() {
		for (BaseEffect effect : effects) {
			if (effect instanceof TankEffect) {
				return ((TankEffect)effect);
			}
		}	
		return null;
	}	

	public void init(TileEntityUpgrade upgrade) {
		for (BaseEffect effect : effects) {
			effect.init(upgrade);
		}		
	}
	
	public void load(TileEntityUpgrade upgrade, NBTTagCompound compound) {
		for (BaseEffect effect : effects) {
			effect.load(upgrade, compound);
		}		
	}
	
	public void save(TileEntityUpgrade upgrade, NBTTagCompound compound) {
		for (BaseEffect effect : effects) {
			effect.save(upgrade, compound);
		}		
	}		
	
	public void update(TileEntityUpgrade upgrade) {
		for (BaseEffect effect : effects) {
			effect.update(upgrade);
		}		
	}
	
	public void removed(TileEntityUpgrade upgrade) {
		for (BaseEffect effect : effects) {
			effect.removed(upgrade);
		}		
	}	

	public String getRawName() {
		return name.replace(":","").replace(" ","_").toLowerCase();
	}
	
	private IIcon icon;
	
	@SideOnly(Side.CLIENT)
	public void createIcon(IIconRegister register) {
		icon = register.registerIcon(StevesCarts.instance.textureHeader + ":" + getRawName().replace("_upgrade", "") + "_icon");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return icon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getMainTexture() {
		return icon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getSideTexture() {
		return sides.get((byte)sideTexture);
	}


	
}