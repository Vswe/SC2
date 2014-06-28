package vswe.stevesvehicles.modules.data.registries;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleAnimal;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleBat;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleMonster;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModulePlayer;
import vswe.stevesvehicles.old.Modules.Addons.Mobdetectors.ModuleVillager;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooter;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevesvehicles.old.StevesVehicles;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;


public class ModuleRegistryShooters extends ModuleRegistry {
    public static final String SHOOTER_KEY = "Shooters";
    public static final String DETECTOR_KEY = "EntityDetectors";
    public ModuleRegistryShooters() {
        super("steves_vehicles_shooters");

        ModuleDataGroup shooters = ModuleDataGroup.createGroup(SHOOTER_KEY, Localization.MODULE_INFO.SHOOTER_GROUP);
        ModuleDataGroup detectors = ModuleDataGroup.createGroup(DETECTOR_KEY, Localization.MODULE_INFO.ENTITY_GROUP);

        ModuleData shooter = new ModuleData("shooter", ModuleShooter.class, 15) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {

            }
        };

        shooter.addShapedRecipe(PIPE,           PIPE,                   PIPE,
                                PIPE,           SHOOTING_STATION,       PIPE,
                                PIPE,           PIPE,                   PIPE);


        shooter.addSides(ModuleSide.TOP);
        shooter.addVehicles(VehicleRegistry.CART, VehicleRegistry.BOAT);
        shooters.add(shooter);
        register(shooter);



        ModuleData advanced = new ModuleData("advanced_shooter", ModuleShooterAdv.class, 50) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {

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
}
