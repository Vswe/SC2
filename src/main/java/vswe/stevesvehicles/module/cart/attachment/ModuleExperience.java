package vswe.stevesvehicles.module.cart.attachment;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.cart.LocalizationCartCleaning;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleExperience extends ModuleAttachment {

	public ModuleExperience(VehicleBase vehicleBase) {
		super(vehicleBase);
	}
	
	private static final int MAX_EXPERIENCE_AMOUNT = 1500;
	private int experienceAmount;
	
	
	@Override
	public void update() {
		if (!getVehicle().getWorld().isRemote) {
		
			List list = getVehicle().getWorld().getEntitiesWithinAABBExcludingEntity(getVehicle().getEntity(), getVehicle().getEntity().boundingBox.expand(3D, 1D, 3D));

            for (Object obj : list) {
                if (obj instanceof EntityXPOrb) {
                    experienceAmount += ((EntityXPOrb)obj).getXpValue();
                    if (experienceAmount > MAX_EXPERIENCE_AMOUNT) {
                        experienceAmount = MAX_EXPERIENCE_AMOUNT;
                    }else {
                        ((Entity)obj).setDead();
                    }
                }
            }
		
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, LocalizationCartCleaning.LEVEL.translate(String.valueOf(experienceAmount), String.valueOf(MAX_EXPERIENCE_AMOUNT))  + "\n" + LocalizationCartCleaning.EXTRACT.translate() + "\n" + LocalizationCartCleaning.PLAYER_LEVEL.translate(String.valueOf(getClientPlayer().experienceLevel)), x, y, CONTAINER_RECT);
	}
	
	@Override
	public int numberOfGuiData() {
		return 1;
	}
	
	@Override
	protected void checkGuiData(Object[] info) {
		updateGuiData(info, 0, (short)experienceAmount);
	}
	
	@Override
	public void receiveGuiData(int id, short data) {
		if (id == 0) {
			experienceAmount = data;
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawForeground(GuiVehicle gui) {
		drawString(gui, LocalizationCartCleaning.TITLE.translate(), 8, 6, 0x404040);
	}

    private static final int[] CONTAINER_RECT = new int[] {10, 15, 26, 65};

	private int[] getContentRect(float part) {
		int normalHeight = CONTAINER_RECT[3] - 4;
		int currentHeight = (int)(normalHeight * part);
			
		return new int[] {CONTAINER_RECT[0] + 2, CONTAINER_RECT[1] + 2 + normalHeight - currentHeight, CONTAINER_RECT[2] - 4, currentHeight, normalHeight};
	}	
	
	private void drawContent(GuiVehicle gui, int id) {
		int lowerLevel = id * MAX_EXPERIENCE_AMOUNT / 3;
		
		int currentLevel = experienceAmount - lowerLevel;
		float part = 3F * currentLevel / (MAX_EXPERIENCE_AMOUNT);
		if (part > 1) {
			part = 1;
		}
		
		int [] content = getContentRect(part);
		drawImage(gui, content, 4 + content[2] * (id + 1), content[4] - content[3]);
	}
	
		

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/experience.png");
		
		for (int i = 0; i < 3; i++) {
			drawContent(gui, i);
		}
		
		drawImage(gui, CONTAINER_RECT, 0, inRect(x, y, CONTAINER_RECT) ? 65 : 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (inRect(x, y, CONTAINER_RECT)) {
			sendPacket(0);
		}
	}
	
	@Override
	public boolean hasGui() {
		return true;
	}
	
	@Override
	public boolean hasSlots() {
		return false;
	}
	
	@Override
	public int guiWidth() {
		return 70;
	}
	
	@Override
	public int guiHeight() {
		return 84;
	}
	

	@Override
	protected int numberOfPackets() {
		return 1;
	}
	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			player.addExperience(Math.min(experienceAmount, 50));
			experienceAmount -= Math.min(experienceAmount, 50);
		}
	}
	
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		experienceAmount = tagCompound.getShort("Experience");
	}

	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setShort("Experience", (short)experienceAmount);
	}
		
	
}
