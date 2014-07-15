package vswe.stevesvehicles.tileentity.manager.cargo;

import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.module.data.ModuleData;


public class CargoItemSelectionModule extends CargoItemSelection {
    private ModuleData module;
    public CargoItemSelectionModule(ILocalizedText name, Class validSlot, ModuleData module) {
        super(name, validSlot, null);
        this.module = module;
    }

    @Override
    public ItemStack getIcon() {
        return module == null ? null : module.getItemStack();
    }
}
