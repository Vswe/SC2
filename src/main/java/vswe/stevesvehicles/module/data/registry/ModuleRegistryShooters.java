package vswe.stevesvehicles.module.data.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelGun;
import vswe.stevesvehicles.client.rendering.models.ModelMobDetector;
import vswe.stevesvehicles.client.rendering.models.ModelShootingRig;
import vswe.stevesvehicles.client.rendering.models.ModelSniperRifle;
import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.ModuleDataGroup;
import vswe.stevesvehicles.module.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.module.common.addon.mobdetector.ModuleAnimal;
import vswe.stevesvehicles.module.common.addon.mobdetector.ModuleBat;
import vswe.stevesvehicles.module.common.addon.mobdetector.ModuleMonster;
import vswe.stevesvehicles.module.common.addon.mobdetector.ModulePlayer;
import vswe.stevesvehicles.module.common.addon.mobdetector.ModuleVillager;
import vswe.stevesvehicles.module.common.addon.projectile.ModuleCake;
import vswe.stevesvehicles.module.common.addon.projectile.ModuleEgg;
import vswe.stevesvehicles.module.common.addon.projectile.ModuleFireball;
import vswe.stevesvehicles.module.common.addon.projectile.ModulePotion;
import vswe.stevesvehicles.module.common.addon.projectile.ModuleSnowball;
import vswe.stevesvehicles.module.common.attachment.ModuleShooter;
import vswe.stevesvehicles.module.common.attachment.ModuleShooterAdvanced;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicle.VehicleRegistry;

import java.util.ArrayList;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryShooters extends ModuleRegistry {
    public static final String SHOOTER_KEY = "Shooters";
    public static final String DETECTOR_KEY = "EntityDetectors";
    private ModuleData advanced;
    public ModuleRegistryShooters() {
        super("steves_vehicles_shooters");

        loadShooters();
        loadDetectors();
        loadProjectiles();
    }


    private void loadShooters() {
        ModuleDataGroup shooters = ModuleDataGroup.createGroup(SHOOTER_KEY, Localization.MODULE_INFO.SHOOTER_GROUP);
        ModuleDataGroup detectors = ModuleDataGroup.getGroup(DETECTOR_KEY);

        ModuleData shooter = new ModuleData("shooter", ModuleShooter.class, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                ArrayList<Integer> pipes = new ArrayList<Integer>();
                for (int i = 0; i < 9; i++) {
                    if (i == 4) continue;
                    pipes.add(i);
                }


                addModel("Rig",  new ModelShootingRig());
                addModel("Pipes", new ModelGun(pipes));
            }
        };

        shooter.addShapedRecipe(PIPE,           PIPE,                   PIPE,
                                PIPE,           SHOOTING_STATION,       PIPE,
                                PIPE,           PIPE,                   PIPE);


        shooter.addSides(ModuleSide.TOP);
        shooter.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        shooters.add(shooter);
        register(shooter);



        advanced = new ModuleData("advanced_shooter", ModuleShooterAdvanced.class, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Rig",  new ModelShootingRig());
                addModel("MobDetector", new ModelMobDetector());
                addModel("Pipes", new ModelSniperRifle());
            }
        };

        advanced.addShapedRecipe(   null,                   ENTITY_SCANNER,         null,
                                    null,                   SHOOTING_STATION,       PIPE,
                                    Items.iron_ingot,       ENTITY_ANALYZER,        Items.iron_ingot);


        advanced.addSides(ModuleSide.TOP);
        advanced.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        advanced.addRequirement(detectors);
        shooters.add(advanced);
        register(advanced);


    }

    private void loadDetectors() {
        ModuleDataGroup detectors = ModuleDataGroup.createGroup(DETECTOR_KEY, Localization.MODULE_INFO.ENTITY_GROUP);

        ModuleData animal = new ModuleData("entity_detector_animal", ModuleAnimal.class, 1);
        animal.addShapedRecipeWithSize(1, 2,
                Items.porkchop,
                EMPTY_DISK);


        animal.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        animal.addParent(advanced);
        detectors.add(animal);
        register(animal);


        ModuleData player = new ModuleData("entity_detector_player", ModulePlayer.class, 7);
        player.addShapedRecipeWithSize(1, 2,
                Items.diamond,
                EMPTY_DISK);


        player.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        player.addParent(advanced);
        detectors.add(player);
        register(player);


        ModuleData villager = new ModuleData("entity_detector_villager", ModuleVillager.class, 1);
        villager.addShapedRecipeWithSize(1, 2,
                Items.emerald,
                EMPTY_DISK);


        villager.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        villager.addParent(advanced);
        detectors.add(villager);
        register(villager);


        ModuleData monster = new ModuleData("entity_detector_monster", ModuleMonster.class, 1);
        monster.addShapedRecipeWithSize(1, 2,
                Items.rotten_flesh,
                EMPTY_DISK);


        monster.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        monster.addParent(advanced);
        detectors.add(monster);
        register(monster);


        ModuleData bat = new ModuleData("entity_detector_bat", ModuleBat.class, 1);
        bat.addShapedRecipeWithSize(1, 2,
                Blocks.pumpkin,
                EMPTY_DISK);


        bat.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        bat.addParent(advanced);
        detectors.add(bat);
        register(bat);
        if (!StevesVehicles.isHalloween) {
            bat.lock();
        }
    }

    private void loadProjectiles() {
        ModuleDataGroup shooters = ModuleDataGroup.getGroup(SHOOTER_KEY);

        ModuleData potion = new ModuleData("projectile_potion", ModulePotion.class, 10);
        potion.addShapedRecipeWithSize(1, 2,
                Items.glass_bottle,
                EMPTY_DISK);


        potion.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        potion.addRequirement(shooters);
        register(potion);


        ModuleData fire = new ModuleData("projectile_fire_charge", ModuleFireball.class, 10);
        fire.addShapedRecipeWithSize(1, 2,
                Items.fire_charge,
                EMPTY_DISK);


        fire.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        fire.addRequirement(shooters);
        fire.lockByDefault();
        register(fire);


        ModuleData egg = new ModuleData("projectile_egg", ModuleEgg.class, 10);
        egg.addShapedRecipeWithSize(1, 2,
                Items.egg,
                EMPTY_DISK);


        egg.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        egg.addRequirement(shooters);
        register(egg);


        ModuleData snowball = new ModuleData("projectile_snowball", ModuleSnowball.class, 10);
        snowball.addShapedRecipeWithSize(1, 2,
                Items.snowball,
                EMPTY_DISK);


        snowball.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        snowball.addRequirement(shooters);
        register(snowball);
        if (!StevesVehicles.isChristmas) {
            snowball.lock();
        }


        ModuleData cake = new ModuleData("projectile_cake", ModuleCake.class, 10);
        cake.addShapedRecipeWithSize(1, 2,
                Items.cake,
                EMPTY_DISK);


        cake.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        cake.addRequirement(shooters);
        cake.lock();
        register(cake);
    }
}
