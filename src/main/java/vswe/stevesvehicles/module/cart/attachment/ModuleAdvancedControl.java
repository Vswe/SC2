package vswe.stevesvehicles.module.cart.attachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import vswe.stevesvehicles.client.interfaces.GuiVehicle;
import vswe.stevesvehicles.old.Helpers.Localization;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.module.cart.ILeverModule;
import vswe.stevesvehicles.module.ModuleBase;
import vswe.stevesvehicles.module.common.engine.ModuleEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;


public class ModuleAdvancedControl extends ModuleBase implements ILeverModule {
	public ModuleAdvancedControl(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	@Override
	public boolean hasSlots() {
		return false;
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return 90;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	private byte [] engineInformation;


	
	//to allow modules to render graphical overlays on the screen
	@SideOnly(Side.CLIENT)
	public void renderOverlay( net.minecraft.client.Minecraft minecraft) {						
		ResourceHelper.bindResource("/gui/drive.png");
		
		if (engineInformation != null) {
			for (int i = 0; i < getVehicle().getEngines().size(); i++) {
				drawImage(5, i * 15, 0, 0, 66, 15); 
				
				int upperBarLength = (engineInformation[i*2] & 63);
				int lowerBarLength = (engineInformation[i*2 + 1] & 63);
				
				ModuleEngine engine = getVehicle().getEngines().get(i);
				float[] rgb = engine.getGuiBarColor();
				GL11.glColor4f(rgb[0], rgb[1], rgb[2], 1.0F);
				drawImage(5+2, i * 15 + 2, 66, 0, upperBarLength, 5);
				drawImage(5+2, i * 15 + 2 + 6, 66, 6, lowerBarLength, 5);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				
				drawImage(5, i * 15,66 + engine.getPriority() * 7,11,7,15);
			}
		}
		
		int enginesEndAt = getVehicle().getEngines().size() * 15;
		
        drawImage(5,enginesEndAt,0,15,32,32);
		if (minecraft.gameSettings.keyBindForward.getIsKeyPressed()) {
			drawImage(5+10,enginesEndAt + 5,32 + 10,15 + 5,12,6);
		}else if (minecraft.gameSettings.keyBindLeft.getIsKeyPressed()) {
			drawImage(5+2,enginesEndAt + 13,32 + 2,15 + 13,6,12);
		}else if (minecraft.gameSettings.keyBindRight.getIsKeyPressed()) {
			drawImage(5+24,enginesEndAt + 13,32 + 24,15 + 13,6,12);
		}    

		int speedGraphicHeight = getSpeedSetting() * 2;
		drawImage(5+9,enginesEndAt + 13 + 12 - speedGraphicHeight,32 + 9,15 + 13 + 12 - speedGraphicHeight, 14, speedGraphicHeight);
		
		drawImage(0,0,0,67,5,130);
		drawImage(1,1 + (256 - getVehicle().y()) / 2 ,5,67,5,1);

		drawImage(5,enginesEndAt + 32,0,47,32,20);
			
		drawImage(5,enginesEndAt + 52,0,47,32,20);
		drawImage(5,enginesEndAt + 72,0,47,32,20);
		
		
		
	    minecraft.fontRenderer.drawString(Localization.MODULES.ATTACHMENTS.ODO.translate(), 5 + 2, enginesEndAt +52 + 2, 0x404040);
        minecraft.fontRenderer.drawString(distToString(odo), 5 + 2, enginesEndAt +52 + 11, 0x404040);		
        minecraft.fontRenderer.drawString(Localization.MODULES.ATTACHMENTS.TRIP.translate(), 5 + 2, enginesEndAt +52 + 22, 0x404040);
        minecraft.fontRenderer.drawString(distToString(trip), 5 + 2, enginesEndAt +52 + 31, 0x404040);
		
		RenderItem itemRenderer = new RenderItem();
        itemRenderer.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, new ItemStack(Items.clock, 1), 5, enginesEndAt +32 + 3);
        itemRenderer.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, new ItemStack(Items.compass, 1), 5 + 16, enginesEndAt+32 + 3);
	
	}
	
    private String distToString(double dist) {
        int i = 0;

        for (; dist >= 1000; i++){
            dist /= 1000.0;
        }

        int val;

        if (dist >= 100){
            val = 1;
        }else if (dist >= 10){
            val = 10;
        }else{
            val = 100;
        }

        double d = Math.round(dist * val) / (double)val;
        String s;

        if (d == (int)d){
            s = String.valueOf((int)d);
        }else{
            s = String.valueOf(d);
        }

        while (s.length() < (s.indexOf('.') != -1 ? 4 : 3)) {
            if (s.indexOf('.') != -1) {
                s += "0";
            }else{
                s += ".0";
            }
        }

        s += Localization.MODULES.ATTACHMENTS.DISTANCES.translate(String.valueOf(i));

        return s;
    }	


	
	
    @Override
    public RAIL_DIRECTION getSpecialRailDirection(int x, int y, int z) {
		if (isForwardKeyDown()) {
			return RAIL_DIRECTION.FORWARD;
		}else if(isLeftKeyDown()) {
			return RAIL_DIRECTION.LEFT;
		}else if(isRightKeyDown()) {
			return RAIL_DIRECTION.RIGHT;
		}else {
			return RAIL_DIRECTION.DEFAULT;
		}	
	}


	
	@Override
	protected void receivePacket(int id, byte[] data, EntityPlayer player) {
		if (id == 0) {
			engineInformation = data;
		}else if(id == 1) {
			if (getVehicle().getEntity().riddenByEntity != null && getVehicle().getEntity().riddenByEntity instanceof EntityPlayer && getVehicle().getEntity().riddenByEntity == player) {
				keyInformation = data[0];
                ((EntityModularCart)getVehicle().getEntity()).resetRailDirection();
			}
		}else if(id == 2) {
			int intOdo = 0;
			int intTrip = 0;
			for (int i = 0; i < 4; i++) {
				int temp = data[i];
				if (temp < 0) {
					temp+=256;
				}
				intOdo |= temp << (i*8);
				temp = data[i+4];
				if (temp < 0) {
					temp+=256;
				}
				intTrip |= temp << (i*8);
			}
			odo = intOdo;
			trip = intTrip;
		}else if (id == 3) {
			trip = 0;
			tripPacketTimer = 0;
		}
	}

	private int tripPacketTimer;
	private int enginePacketTimer;
	private byte keyInformation;

    private static final int SPEED_CHANGE_COOLDOWN = 8;

	@Override
	public void update() {
		super.update();
		
		if (!getVehicle().getWorld().isRemote && getVehicle().getEntity().riddenByEntity != null && getVehicle().getEntity().riddenByEntity instanceof EntityPlayer) {
			if (enginePacketTimer == 0) {
				sendEnginePacket((EntityPlayer)getVehicle().getEntity().riddenByEntity);
				enginePacketTimer = 15;
			}else{
				enginePacketTimer--;
			}
			if (tripPacketTimer == 0) {
				sendTripPacket((EntityPlayer)getVehicle().getEntity().riddenByEntity);
				tripPacketTimer = 500;
			}else{
				tripPacketTimer--;
			}			
		}else{
			enginePacketTimer = 0;
			tripPacketTimer = 0;
		}
		
		
		if (getVehicle().getWorld().isRemote) {
			encodeKeys();
		}
		
		if (!lastBackKey && isBackKeyDown()) {
			turnback();
		}
		lastBackKey = isBackKeyDown();
		
		if (!getVehicle().getWorld().isRemote) {
			if (speedChangeCooldown == 0) {
                if(isJumpKeyDown() && !isSneakKeyDown()) {
					setSpeedSetting(getSpeedSetting() +1 );
					speedChangeCooldown = SPEED_CHANGE_COOLDOWN;
				}else if(isSneakKeyDown() && !isJumpKeyDown()) {
					setSpeedSetting(getSpeedSetting() - 1);
					speedChangeCooldown = SPEED_CHANGE_COOLDOWN;
				}
			}else{
				speedChangeCooldown--;
			}
			
			if (isForwardKeyDown() && isLeftKeyDown() && isRightKeyDown()) {
				if (getVehicle().getEntity().riddenByEntity != null && getVehicle().getEntity().riddenByEntity instanceof EntityPlayer) {
					getVehicle().getEntity().riddenByEntity.mountEntity(getVehicle().getEntity());
					keyInformation = (byte)0;
				}			
			}
		}
		
		
		

        double x = getVehicle().getEntity().posX - lastPosX;
        double y = getVehicle().getEntity().posY - lastPosY;
        double z = getVehicle().getEntity().posZ - lastPosZ;
        lastPosX = getVehicle().getEntity().posX;
        lastPosY = getVehicle().getEntity().posY;
        lastPosZ = getVehicle().getEntity().posZ;
        double dist = Math.sqrt(x * x + y * y + z * z);

        if (!first) {
            odo += dist;
            trip += dist;
        }else {
            first = false;
        }		
		
	}
	
    //the reason prePosX etc. isn't used is to make sure that the calculation takes place exactly the same number of times the last values are saved.
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean first = true;	
	
	private int speedChangeCooldown;
	private boolean lastBackKey;
	
	@Override
    public double getPushFactor() {
        switch (getSpeedSetting()) {
            case 1:
                return 0.01D;

            case 2:
                return 0.03D;

            case 3:
                return 0.05D;

            case 4:
                return 0.07D;

            case 5:
                return 0.09D;

            case 6:
                return 0.11D;

            default:
                return super.getPushFactor();
        }
    }
	
	
	private void encodeKeys() {
		if (getVehicle().getEntity().riddenByEntity != null && getVehicle().getEntity().riddenByEntity instanceof EntityPlayer && getVehicle().getEntity().riddenByEntity == getClientPlayer()) {
			net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getMinecraft();	
				
			byte oldVal = keyInformation;
		
			keyInformation = 0;
			

			keyInformation |= (byte)((minecraft.gameSettings.keyBindForward.getIsKeyPressed() ? 1 : 0));
			keyInformation |= (byte)((minecraft.gameSettings.keyBindLeft.getIsKeyPressed() ? 1 : 0) << 1);
			keyInformation |= (byte)((minecraft.gameSettings.keyBindRight.getIsKeyPressed() ? 1 : 0) << 2);
			keyInformation |= (byte)((minecraft.gameSettings.keyBindBack.getIsKeyPressed() ? 1 : 0) << 3);
			keyInformation |= (byte)((minecraft.gameSettings.keyBindJump.getIsKeyPressed() ? 1 : 0) << 4);
			keyInformation |= (byte)((minecraft.gameSettings.keyBindSneak.getIsKeyPressed() ? 1 : 0) << 5);

			
			if (oldVal != keyInformation) {
				PacketHandler.sendPacket(getVehicle(), 1 + getPacketStart(), new byte[] {keyInformation});
			}
		}
	}
	
	private boolean isForwardKeyDown() {
		return (keyInformation & 1) != 0;
	}	
	private boolean isLeftKeyDown() {
		return (keyInformation & (1 << 1)) != 0;
	}
	private boolean isRightKeyDown() {
		return (keyInformation & (1 << 2)) != 0;
	}	
	private boolean isBackKeyDown() {
		return (keyInformation & (1 << 3)) != 0;
	}	
	private boolean isJumpKeyDown() {
		return (keyInformation & (1 << 4)) != 0;
	}	
	private boolean isSneakKeyDown() {
		return (keyInformation & (1 << 5)) != 0;
	}		

	private double odo;
	private double trip;	
	private void sendTripPacket(EntityPlayer player) {
		byte[] data = new byte[8];
		int intOdo = (int)odo;
		int intTrip = (int)trip;

		for (int i = 0; i < 4; i++) {
			data[i] = (byte)((intOdo & (255 << (i * 8))) >> (i * 8));
			data[i+4] = (byte)((intTrip & (255 << (i * 8))) >> (i * 8));
		}
		sendPacket(2, data, player);
	}
	
	private void sendEnginePacket(EntityPlayer player) {
		int engineCount = getVehicle().getEngines().size();
		//there's room for two engines in every single byte
		byte[] data = new byte[engineCount * 2];
		
		for (int i = 0; i < getVehicle().getEngines().size(); i++) {
			ModuleEngine engine  = getVehicle().getEngines().get(i);
		
			int totalFuel = engine.getTotalFuel();
			
			int fuelInTopBar = 20000;
			int maxBarLength = 62;
			float percentage = (totalFuel % fuelInTopBar) / (float)fuelInTopBar;
			int upperBarLength = (int)(maxBarLength * percentage);
			
			int lowerBarLength = totalFuel / fuelInTopBar;
			if (lowerBarLength > maxBarLength) {
				lowerBarLength = maxBarLength;
			}
			
			data[i*2] = (byte)(upperBarLength & 63);
			data[i*2 + 1] = (byte)(lowerBarLength & 63);
		}
		
		sendPacket(0, data, player);
	}
	

	
	@Override
	public int numberOfPackets() {
		return 4;
	}


	
	
	private void setSpeedSetting(int val) {
		if (val < 0 || val > 6) {
			return;
		}
		updateDw(0, val);
	}
	
	private int getSpeedSetting() {
		if (isPlaceholder()) {
			return 1;
		}else{
			return getDw(0);
		}
	}
	
	
	@Override
	public int numberOfDataWatchers() {
		return 1;
	}

	@Override
	public void initDw() {
		addDw(0,0);
	}


	@Override
	public boolean stopEngines() {
		return getSpeedSetting() == 0;
	}
	

	@Override
    public int getConsumption(boolean isMoving)
    {
		if (!isMoving) {
			return super.getConsumption(false);
		}
	
        switch (getSpeedSetting()) {
            case 4:
                return 1;

            case 5:
                return 3;

            case 6:
                return 5;

            default:
                return super.getConsumption(true);
        }
    }	
	
	

	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource("/gui/advlever.png");

		if (inRect(x,y, BUTTON_RECT)) {
			drawImage(gui, BUTTON_RECT, 0, BUTTON_RECT[3]);
		}else{
			drawImage(gui, BUTTON_RECT, 0, 0);
		}
	}

	private static final int[] BUTTON_RECT = new int[] {15,20, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, Localization.MODULES.ATTACHMENTS.CONTROL_RESET.translate(), x,y, BUTTON_RECT);
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, BUTTON_RECT)) {
				sendPacket(3);
			}
		}
	}
	
	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui, Localization.MODULES.ATTACHMENTS.CONTROL_SYSTEM.translate(), 8, 6, 0x404040);
	}
	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setByte("Speed", (byte)getSpeedSetting());
		tagCompound.setDouble("ODO", odo);
		tagCompound.setDouble("TRIP", trip);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		setSpeedSetting(tagCompound.getByte("Speed"));
		odo = tagCompound.getDouble("ODO");
		trip = tagCompound.getDouble("TRIP");
	}	

	public float getWheelAngle() {
        if (isLeftKeyDown()) {
            return (float)Math.PI / 8;
        }else if (isRightKeyDown()){
            return (float)-Math.PI / 8;
        }else{
            return 0;
        }
	}

	@Override
	public float getLeverState() {
		if (isPlaceholder())  {
			return 0F;
		}else{
			return getSpeedSetting() / 6F;	
		}
	}	
	
	//would be better to have in the seat but the onInteractFirst thingy doesn't really work due to vanilla stuff
	@Override
	public void postUpdate() {
		if (getVehicle().getWorld().isRemote && getVehicle().getEntity().riddenByEntity != null && getVehicle().getEntity().riddenByEntity instanceof EntityPlayer && getVehicle().getEntity().riddenByEntity == getClientPlayer()) {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), false);
		}
	}
}