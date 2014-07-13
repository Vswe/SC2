import vswe.stevesvehicles.module.data.ModuleData;
import vswe.stevesvehicles.module.data.registry.ModuleRegistry;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.upgrade.Upgrade;
import vswe.stevesvehicles.upgrade.registry.UpgradeRegistry;
import vswe.stevesvehicles.vehicle.VehicleRegistry;
import vswe.stevesvehicles.vehicle.VehicleType;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Temporary class for some file util
public class Util {
    public static void main(String[] args) throws IOException{
        //this code is just for moving stuff around during development, doesn't reall matter that it's using absolute paths
        File itemDir = new File("C:\\Users\\Vswe\\Dropbox\\Minecraft Modding\\SC2\\src\\main\\resources\\assets\\stevescarts\\textures\\items");
        File blockDir = new File("C:\\Users\\Vswe\\Dropbox\\Minecraft Modding\\SC2\\src\\main\\resources\\assets\\stevescarts\\textures\\blocks");

        ModuleRegistry.init();
        for (ModuleData moduleData : ModuleRegistry.getAllModules()) {
            String name = "modules/" + moduleData.getFullRawUnlocalizedName().replace(".", "/").replace(":", "/") + ".png";
            File target = new File(itemDir, name);
            moveFile(itemDir, target, moduleData.getRawUnlocalizedName());
        }

        for (ComponentTypes componentTypes : ComponentTypes.values()) {
            File target = new File(itemDir, "components/" + componentTypes.getUnlocalizedName() + ".png");
            moveFile(itemDir, target, componentTypes.getUnlocalizedName());
        }

        moveFile(itemDir, new File(itemDir, "unknown.png"), "unknown_icon");

        VehicleRegistry.init();
        for (VehicleType vehicleType : VehicleRegistry.getInstance().getAllVehicles()) {
            File target = new File(itemDir, "vehicles/" + vehicleType.getRawUnlocalizedName() + ".png");
            moveFile(itemDir, target, "modular_" + vehicleType.getRawUnlocalizedName());
        }


        final List<String> blackList = new ArrayList<String>();
        blackList.add("unknown.png");
        for (File child : itemDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png") && !blackList.contains(name);
            }
        })) {

            moveFile(child, new File(itemDir, "deleted/" + child.getName()));
        }

        Upgrade.disableRecipes = true;
        UpgradeRegistry.init();
        for (Upgrade upgrade : UpgradeRegistry.getAllUpgrades()) {
            File target = new File(blockDir, "upgrades/" + upgrade.getFullRawUnlocalizedName().replace(".", "/").replace(":", "/") + ".png");
            moveFile(blockDir, target, upgrade.getRawUnlocalizedName().replace("_upgrade", ""));
        }

        Map<String, String> blocks = new HashMap<String, String>();
        blocks.put("cargo_distributor_blue", "distributor/blue");
        blocks.put("cargo_distributor_green", "distributor/green");
        blocks.put("cargo_distributor_red", "distributor/red");
        blocks.put("cargo_distributor_yellow", "distributor/yellow");
        blocks.put("cargo_distributor_orange", "distributor/orange");
        blocks.put("cargo_distributor_purple", "distributor/purple");

        blocks.put("cargo_manager_blue", "managers/cargo/blue");
        blocks.put("cargo_manager_red", "managers/cargo/red");
        blocks.put("cargo_manager_green", "managers/cargo/green");
        blocks.put("cargo_manager_yellow", "managers/cargo/yellow");
        blocks.put("cargo_manager_top", "managers/cargo/top");
        blocks.put("cargo_manager_bot", "managers/cargo/bot");

        blocks.put("liquid_manager_blue", "managers/liquid/blue");
        blocks.put("liquid_manager_red", "managers/liquid/red");
        blocks.put("liquid_manager_green", "managers/liquid/green");
        blocks.put("liquid_manager_yellow", "managers/liquid/yellow");
        blocks.put("liquid_manager_top", "managers/liquid/top");
        blocks.put("liquid_manager_bot", "managers/liquid/bot");

        blocks.put("cart_assembler_bot", "assembler/bot");
        blocks.put("cart_assembler_side_1", "assembler/side_1");
        blocks.put("cart_assembler_side_2", "assembler/side_2");
        blocks.put("cart_assembler_side_3", "assembler/side_3");
        blocks.put("cart_assembler_side_4", "assembler/side_4");
        blocks.put("cart_assembler_top", "assembler/top");

        blocks.put("reinforced_metal", "storage/reinforced_metal");
        blocks.put("galgadorian", "storage/galgadorian");
        blocks.put("enhanced_galgadorian", "storage/enhanced_galgadorian");

        blocks.put("advanced_detector_rail", "rails/detector");
        blocks.put("advanced_detector_rail_corner", "rails/detector_corner");
        blocks.put("junction_rail", "rails/junction");
        blocks.put("junction_rail_corner", "rails/junction_corner");

        blocks.put("module_toggler_bot", "toggler/bot");
        blocks.put("module_toggler_side", "toggler/side");
        blocks.put("module_toggler_top", "toggler/top");

        blocks.put("detector_junction_blue", "detectors/junction/blue");
        blocks.put("detector_junction_green", "detectors/junction/green");
        blocks.put("detector_junction_red", "detectors/junction/red");
        blocks.put("detector_junction_yellow", "detectors/junction/yellow");
        blocks.put("detector_junction_top", "detectors/junction/top");
        blocks.put("detector_junction_bot", "detectors/junction/bot");

        blocks.put("detector_manager_blue", "detectors/manager/blue");
        blocks.put("detector_manager_green", "detectors/manager/green");
        blocks.put("detector_manager_red", "detectors/manager/red");
        blocks.put("detector_manager_yellow", "detectors/manager/yellow");
        blocks.put("detector_manager_top", "detectors/manager/top");
        blocks.put("detector_manager_bot", "detectors/manager/bot");

        blocks.put("detector_station_blue", "detectors/station/blue");
        blocks.put("detector_station_green", "detectors/station/green");
        blocks.put("detector_station_red", "detectors/station/red");
        blocks.put("detector_station_yellow", "detectors/station/yellow");
        blocks.put("detector_station_top", "detectors/station/top");
        blocks.put("detector_station_bot", "detectors/station/bot");

        blocks.put("detector_redstone_blue", "detectors/redstone/blue");
        blocks.put("detector_redstone_green", "detectors/redstone/green");
        blocks.put("detector_redstone_red", "detectors/redstone/red");
        blocks.put("detector_redstone_yellow", "detectors/redstone/yellow");
        blocks.put("detector_redstone_top", "detectors/redstone/top");
        blocks.put("detector_redstone_bot", "detectors/redstone/bot");

        blocks.put("detector_unit_blue", "detectors/unit/blue");
        blocks.put("detector_unit_green", "detectors/unit/green");
        blocks.put("detector_unit_red", "detectors/unit/red");
        blocks.put("detector_unit_yellow", "detectors/unit/yellow");
        blocks.put("detector_unit_top", "detectors/unit/top");
        blocks.put("detector_unit_bot", "detectors/unit/bot");

        blocks.put("upgrade_side_0", "upgrades/sides/default");
        blocks.put("upgrade_side_1", "upgrades/sides/chest");
        
        for (Map.Entry<String, String> stringStringEntry : blocks.entrySet()) {
            moveFile(blockDir, new File(blockDir, stringStringEntry.getValue() + ".png"), stringStringEntry.getKey());
        }

        for (File child : blockDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        })) {

            moveFile(child, new File(blockDir, "deleted/" + child.getName()));
        }
    }

    private static void moveFile(File sourceDir, File target, String name) throws IOException {
        File source = new File(sourceDir, name + ".png");
        if (!source.exists()) {
            source = new File(sourceDir, name + "_icon.png");
        }

        if (source.exists()) {
            moveFile(source, target);
        }
    }

    private static void moveFile(File source, File target) throws IOException {
        if (target.equals(source)) {
            return;
        }

        createFolder(target.getParentFile());
        if (!source.renameTo(target)) {
            System.err.println("Failed to move " + target.getName());
        }
    }

    private static void createFolder(File dir) throws IOException {
        if (dir == null) {
            return;
        }

        File parent = dir.getParentFile();
        createFolder(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
