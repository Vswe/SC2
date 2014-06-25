package vswe.stevescarts.old.Modules.Realtimers;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import vswe.stevescarts.vehicles.entities.EntityModularCart;
import vswe.stevescarts.old.Helpers.ResourceHelper;
import vswe.stevescarts.old.Interfaces.GuiMinecart;
import vswe.stevescarts.old.Modules.ModuleBase;

public abstract /*remove the abstract*/ class ModuleCommand extends ModuleBase implements ICommandSender {
	public ModuleCommand(EntityModularCart cart) {
		super(cart);
	}

	private String command = "say HI";
	
	@Override
	public void drawForeground(GuiMinecart gui) {
		List lines = gui.getFontRenderer().listFormattedStringToWidth(command,textbox[2] - 4);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i).toString();
			drawString(gui, line, textbox[0]+2, textbox[1] + 2 + i * 8, 0xFFFFFF);
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

	private int[] textbox = new int[] {10,10,145,90};
	
	
	
	@Override
	public void drawBackground(GuiMinecart gui, int x, int y) {
		ResourceHelper.bindResource("/gui/command.png");
		
		drawImage(gui, textbox, 0, 0);
	}	
	
	public void keyPress(char character, int extraInformation) {
		if (extraInformation == 14) {
			if (command.length() > 0) {
				command = command.substring(0, command.length() - 1);
			}
		}else{
			command += Character.toString(character);
		}
	}
	
    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName() {
		return "@";
	}
	
    public void sendChatToPlayer(String var1) {}

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int var1, String var2) {
		return var1 <= 2;
	}

    /**
     * Translates and formats the given string key with the given arguments.
     */
    public String translateString(String var1, Object ... var2) {
		return var1;
	}

    public ChunkCoordinates func_82114_b() {
		return new ChunkCoordinates(getCart().x(), getCart().y(), getCart().z());
	}
	
	private void executeCommand() {
        if (!getCart().worldObj.isRemote)
        {
           /* MinecraftServer server = MinecraftServer.getServer();

            if (server != null && server.func_82356_Z())
            {
                ICommandManager commandManager = server.getCommandManager();
                commandManager.executeCommand(this, command);
            }*/
        }	
	}
	
	@Override
	public void moveMinecartOnRail(int x, int y, int z) {
        if (getCart().worldObj.getBlock(x, y, z) == Blocks.detector_rail)
        {
           executeCommand();
        }
	}	
	
	@Override
	protected void Save(NBTTagCompound tagCompound, int id) {
		tagCompound.setString(generateNBTName("Command",id), command);
	}
	
	@Override
	protected void Load(NBTTagCompound tagCompound, int id) {
		command = tagCompound.getString(generateNBTName("Command",id));	
	}	
	
}