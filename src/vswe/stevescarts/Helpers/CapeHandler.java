package vswe.stevescarts.Helpers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CapeHandler implements Runnable, ITickHandler {


	public CapeHandler() {
		TickRegistry.registerTickHandler(this, Side.CLIENT);
		capes = new HashMap<String,String>();	

		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			HttpURLConnection connection = (HttpURLConnection)new URL("https://dl.dropbox.com/u/46486053/Capes.txt").openConnection();
			HttpURLConnection.setFollowRedirects(true);
			connection.setConnectTimeout(2147483647);
			connection.setDoInput(true);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
		
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(":");
				
				if (split.length == 2) {
					String image = "https://dl.dropbox.com/u/46486053/" + split[0];
					String usersString = split[1];
					String[] users = usersString.split(",");
					for (String user : users) {
						capes.put(user, image);
					}
				}
			}
		}catch(IOException ex) {
	
		}		
	}
	
	HashMap<String,String> capes;
	
	@Override	
	public void tickStart(EnumSet type, Object... tickData) {
		if (type.contains(TickType.PLAYER)) {

			EntityPlayer player = (EntityPlayer)tickData[0];
			

			if (player instanceof AbstractClientPlayer) {
				loadNewCape((AbstractClientPlayer)player);
			}
		}
	}
	
	private void loadNewCape(AbstractClientPlayer player) {
        if (player != null) {
			String username = StringUtils.stripControlCodes(player.username);
			String cape = capes.get(username);
			if (cape != null) {

				ResourceLocation loc = ResourceHelper.getResourceFromPath(cape);
				if (!loc.equals(player.getLocationCape())) {
					ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, loc, 4);
					ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, tryToDownloadCape(loc, cape), 2);
                }
			}
		}		
	}
	
    private ThreadDownloadImageData tryToDownloadCape(ResourceLocation cape, String capeUrl) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        TextureObject object = texturemanager.getTexture(cape);

        //no need to download the cape if we have it already
        if (object == null) {
            object = new ThreadDownloadImageData(capeUrl, null, null);
            texturemanager.loadTexture(cape, object);
        }

        return (ThreadDownloadImageData)object;
    }
	

	
	@Override
	public void tickEnd(EnumSet type, Object... tickData) {
	
	}
	
	@Override
	public EnumSet ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}
	
	@Override
	public String getLabel() {
		return "SC2Capes";
	}		
	
}