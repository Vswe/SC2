package vswe.stevesvehicles.upgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevesvehicles.item.ModItems;
import vswe.stevesvehicles.StevesVehicles;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.recipe.IRecipeOutput;
import vswe.stevesvehicles.recipe.ModuleRecipeShaped;
import vswe.stevesvehicles.upgrade.effect.BaseEffect;
import vswe.stevesvehicles.upgrade.registry.UpgradeRegistry;

public class Upgrade implements IRecipeOutput {


	private static HashMap<Byte, IIcon> sides;
	private static HashMap<Class<? extends BaseEffect>, IEffectInfo> effectInfo;


    public static void registerInfo(Class<? extends BaseEffect> clazz, IEffectInfo info) {
        if (effectInfo == null) {
            effectInfo = new HashMap<Class<? extends BaseEffect>, IEffectInfo>();
        }

        effectInfo.put(clazz, info);
    }

    public void addInfo(List<String> info) {
        for (EffectType effect : effects) {
            IEffectInfo entry = effectInfo.get(effect.getClazz());
            if (entry != null) {
                String name = entry.getName(effect.getParams());
                if (name != null) {
                    info.add(name);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static IIcon standardSideIcon;

	//used to fix the destroy animation
	public static IIcon getStandardIcon() {
		return standardSideIcon;
	}


    private String fullUnlocalizedName;
    public final String getFullRawUnlocalizedName() {
        return fullUnlocalizedName;
    }

    public final void setFullRawUnlocalizedName(String val) {fullUnlocalizedName = val;}

	private final String unlocalizedName;
	private ArrayList<EffectType> effects;

	public Upgrade(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		effects = new ArrayList<EffectType>();
	}	

	public String getUnlocalizedName() {
        return "steves_vehicles:tile." + getFullRawUnlocalizedName() + ".name";
    }

	public String getName() {
		return StatCollector.translateToLocal(getUnlocalizedName());
	}
	
	public Upgrade addEffect(Class <? extends BaseEffect> effect, Object ... params) {
		effects.add(new EffectType(effect, params));
		
		return this;
	}

    public static boolean disableRecipes;
    public Upgrade addRecipe(IRecipe recipe) {
        if (!disableRecipes) {
            GameRegistry.addRecipe(recipe);
        }

        return this;
    }

    public Upgrade addShapedRecipeWithSize(int width, int height, Object ... recipe) {
        addRecipe(new ModuleRecipeShaped(this, width, height, recipe));

        return this;
    }

    public Upgrade addShapedRecipe(Object ... recipe) {
        if (recipe.length == 9) {
            addShapedRecipeWithSize(3, 3, recipe);
        }else if(recipe.length == 4) {
            addShapedRecipeWithSize(2, 2, recipe);
        }

        return this;
    }


	public ItemStack getItemStack() {
		return getItemStack(1);
	}
	
	protected ItemStack getItemStack(int count) {
		return new ItemStack(ModItems.upgrades, count, UpgradeRegistry.getIdFromUpgrade(this));
	}


    public ArrayList<EffectType> getEffectTypes() {
        return effects;
    }

    public final String getRawUnlocalizedName() {
		return unlocalizedName;
	}
	
	private IIcon icon;
	
	@SideOnly(Side.CLIENT)
	protected void createIcon(IIconRegister register) {
		icon = register.registerIcon(StevesVehicles.instance.textureHeader + ":upgrades/" + getFullRawUnlocalizedName().replace(".", "/").replace(":", "/"));
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
		return standardSideIcon;
	}


    public boolean connectToRedstone() {
        return false;
    }

    public static void registerIcons(IIconRegister register) {
        standardSideIcon = register.registerIcon(StevesVehicles.instance.textureHeader + ":upgrades/sides/default");

        for (Upgrade upgrade : UpgradeRegistry.getAllUpgrades()) {
            upgrade.createIcon(register);
        }
    }
}