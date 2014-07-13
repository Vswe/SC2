package vswe.stevesvehicles.upgrade.registry;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.effect.assembly.Blueprint;
import vswe.stevesvehicles.upgrade.effect.assembly.Disassemble;
import vswe.stevesvehicles.upgrade.effect.assembly.InputChest;
import vswe.stevesvehicles.upgrade.effect.external.Deployer;
import vswe.stevesvehicles.upgrade.effect.external.Manager;
import vswe.stevesvehicles.upgrade.effect.external.Redstone;
import vswe.stevesvehicles.upgrade.effect.external.Transposer;
import vswe.stevesvehicles.upgrade.effect.time.TimeFlatCart;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class UpgradeRegistryControl extends UpgradeRegistry {
    public UpgradeRegistryControl() {
        super("control");

        Upgrade input = new Upgrade("module_input") {
            @SideOnly(Side.CLIENT)
            private IIcon side;

            @SideOnly(Side.CLIENT)
            @Override
            protected void createIcon(IIconRegister register) {
                super.createIcon(register);
                side = register.registerIcon(StevesVehicles.instance.textureHeader + ":upgrades/sides/chest");
            }

            @SideOnly(Side.CLIENT)
            @Override
            public IIcon getSideTexture() {
                return side;
            }
        };
        input.addEffect(InputChest.class, 7, 3);

        input.addShapedRecipe(  null,               SIMPLE_PCB,     null,
                                Items.redstone,     Blocks.chest,   Items.redstone,
                                Items.iron_ingot,   BLANK_UPGRADE,  Items.iron_ingot);

        register(input);



        Upgrade production = new Upgrade("production_line");
        production.addEffect(Blueprint.class);

        production.addShapedRecipe( null,               SIMPLE_PCB,         null,
                                    Items.redstone,     Blocks.piston,      Items.redstone,
                                    Items.iron_ingot,   BLANK_UPGRADE,      Items.iron_ingot);

        register(production);



        Upgrade deployer = new Upgrade("cart_deployer");
        deployer.addEffect(Deployer.class);

        deployer.addShapedRecipe(   Items.iron_ingot,   Blocks.rail,    Items.iron_ingot,
                                    Items.redstone,     Blocks.piston,  Items.redstone,
                                    Items.iron_ingot,   BLANK_UPGRADE,  Items.iron_ingot);

        register(deployer);



        Upgrade modifier = new Upgrade("cart_modifier");
        modifier.addEffect(Disassemble.class);

        modifier.addShapedRecipe(   Items.iron_ingot,   null,           Items.iron_ingot,
                                    Items.redstone,     Blocks.anvil,   Items.redstone,
                                    Items.iron_ingot,     BLANK_UPGRADE,  Items.iron_ingot);

        register(modifier);



        Upgrade crane = new Upgrade("cart_crane");
        crane.addEffect(Transposer.class);

        crane.addShapedRecipe(  Items.iron_ingot,   Blocks.piston,      Items.iron_ingot,
                                Items.redstone,     Blocks.rail,        Items.redstone,
                                Items.iron_ingot,   BLANK_UPGRADE,      Items.iron_ingot);

        register(crane);



        Upgrade redstone = new Upgrade("redstone_control") {
            @Override
            public boolean connectToRedstone() {
                return true;
            }
        };
        redstone.addEffect(Redstone.class);

        redstone.addShapedRecipe(   Blocks.redstone_torch,  null,           Blocks.redstone_torch,
                                    Items.redstone,         SIMPLE_PCB,     Items.redstone,
                                    Items.iron_ingot,       BLANK_UPGRADE,  Items.iron_ingot);

        register(redstone);



        Upgrade manager = new Upgrade("manager_bridge");
        manager.addEffect(Manager.class);
        manager.addEffect(TimeFlatCart.class, 100);

        manager.addShapedRecipe(    Items.iron_ingot,   Blocks.piston,      Items.iron_ingot,
                                    Items.redstone,     Blocks.rail,        Items.redstone,
                                    Items.iron_ingot,   BLANK_UPGRADE,      Items.iron_ingot);

        register(manager);
    }
}
