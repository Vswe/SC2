package vswe.stevesvehicles.old.Modules.Realtimers;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.ModuleBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleExperience extends ModuleBase {

	public ModuleExperience(EntityModularCart cart) {
		super(cart);
		
		
	}
	
	private static final int MAX_EXPERIENCE_AMOUNT = 1500;
	private int experienceAmount;
	
	
	@Override
	public void update() {
		if (!getCart().worldObj.isRemote) {
		
			List list = getCart().worldObj.getEntitiesWithinAABBExcludingEntity(getCart(), getCart().boundingBox.expand(3D, 1D, 3D));
	
			for (int e = 0; e < list.size(); e++)
	        {
	            if (list.get(e) instanceof EntityXPOrb)
	            {
	            	experienceAmount += ((EntityXPOrb)list.get(e)).getXpValue();
	            	if (experienceAmount > MAX_EXPERIENCE_AMOUNT) {
	            		experienceAmount = MAX_EXPERIENCE_AMOUNT;
	            	}else{
	            		((Entity)list.get(e)).setDead();
	            	}
				}
			}
		
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE_LEVEL.translate(String.valueOf(experienceAmount), String.valueOf(MAX_EXPERIENCE_AMOUNT))  + "\n" + Localization.MODULES.ATTACHMENTS.EXPERIENCE_EXTRACT.translate() + "\n" + Localization.MODULES.ATTACHMENTS.EXPERIENCE_PLAYER_LEVEL.translate(String.valueOf(getClientPlayer().experienceLevel)), x, y, getContainerRect());
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
		drawString(gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE.translate(), 8, 6, 0x404040);
	}
	
	private int[] getContainerRect() {
		return new int[] {10, 15, 26, 65};
	}
	
	private int[] getContentRect(float part) {
		int[] cont = getContainerRect();
		
		int normalHeight = cont[3] - 4;
		int currentHeight = (int)(normalHeight * part);
			
		return new int[] {cont[0] + 2, cont[1] + 2 + normalHeight - currentHeight, cont[2] - 4, currentHeight, normalHeight};
	}	
	
	private void drawContent(GuiVehicle gui, int x, int y, int id) {
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
			drawContent(gui, x, y, i);
		}
		
		drawImage(gui, getContainerRect(), 0, inRect(x, y, getContainerRect()) ? 65 : 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (inRect(x, y, getContainerRect())) {
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
	protected void Load(NBTTagCompound tagCompound, int id) {
		experienceAmount = tagCompound.getShort(generateNBTName("Experience",id));
	}

	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setShort(generateNBTName("Experience",id), (short)experienceAmount);
	}
		
	
}
