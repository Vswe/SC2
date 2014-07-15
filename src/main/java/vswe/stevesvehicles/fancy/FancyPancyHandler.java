package vswe.stevesvehicles.fancy;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevesvehicles.client.ResourceHelper;



@SideOnly(Side.CLIENT)
public abstract class FancyPancyHandler {

    public HashMap<String, ServerFancy> getServerFancies() {
        return serverFancies;
    }

    public HashMap<String, UserFancy> getFancies() {
        return fancies;
    }

    public final String getCode() {
        return code;
    }

    private final String code;
    public FancyPancyHandler(String code) {
        this.code = code;
        FMLCommonHandler.instance().bus().register(this);
		fancies = new HashMap<String,UserFancy>();
        serverFancies = new HashMap<String, ServerFancy>();
	}


    private static final int PROTOCOL_VERSION = 0;



    private HashMap<String,UserFancy> fancies;
    private HashMap<String,ServerFancy> serverFancies;
    private boolean ready = false;
    private String serverHash;
    private int serverReHash;

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private void generateServerHash() {
        ServerData data = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 6);
        String ip;
        if (data != null) {
            if (data.serverIP.equals("127.0.0.1")) {
                ip = "localhost";
            }else{
                ip = data.serverIP;
            }
        }else if (Minecraft.getMinecraft().getIntegratedServer() != null && Minecraft.getMinecraft().getIntegratedServer().getPublic()){
            ip = "localhost";
        }else{
            ip = "single player";
        }

        serverHash = md5(ip.toLowerCase());
        serverReHash = 100;
    }

    public String getServerHash() {
        if (serverReHash == 0) {
            generateServerHash();
        }

        return serverHash;
    }

    private String md5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            md5.update(str.getBytes());
            byte[] bytes = md5.digest();
            String result = "";

            for (byte b : bytes) {
                result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        }catch (NoSuchAlgorithmException ignored) {}

        return null;
    }

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (!ready || event.phase != TickEvent.Phase.START) {
            return;
        }
        if (serverReHash > 0) {
            serverReHash--;
        }


        EntityPlayer player = event.player;


        if (player instanceof AbstractClientPlayer) {
            loadNewFancy((AbstractClientPlayer) player);
        }

    }


	
	private void loadNewFancy(AbstractClientPlayer player) {
        if (player != null) {

            String username = StringUtils.stripControlCodes(player.getDisplayName());
            UserFancy fancyObj = fancies.get(username);

            if (fancyObj == null && serverFancies.size() > 0 && serverFancies.containsKey(getServerHash())) {
                fancyObj = new UserFancy(this);
                fancies.put(username, fancyObj);
            }

            if (fancyObj != null) {
                fancyObj.update(player);

                String fancy = fancyObj.getImage(player);
                if (fancy != null) {
                    ResourceLocation loc = ResourceHelper.getResourceFromPath(fancy);
                    if (!loc.equals(getCurrentResource(player))) {
                        setCurrentResource(player, loc, fancy);
                    }
                }
            }
		}		
	}

    public ThreadDownloadImageData tryToDownloadFancy(ResourceLocation fancy, String fancyUrl) {
        return tryToDownloadFancy(fancy, fancyUrl, null, null);
    }

    public ThreadDownloadImageData tryToDownloadFancy(ResourceLocation fancy, String fancyUrl, ResourceLocation fallbackResource, IImageBuffer optionalBuffer) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject object = texturemanager.getTexture(fancy);

        //no need to download the fancy if we have it already
        if (object == null) {
            object = new ThreadDownloadImageData(fancyUrl, fallbackResource, optionalBuffer);
            texturemanager.loadTexture(fancy, object);
        }

        return (ThreadDownloadImageData)object;
    }

    public abstract String getDefaultUrl(AbstractClientPlayer player);
    public abstract ResourceLocation getDefaultResource(AbstractClientPlayer player);
    public abstract ThreadDownloadImageData getCurrentTexture(AbstractClientPlayer player);
    public abstract ResourceLocation getCurrentResource(AbstractClientPlayer player);
    public abstract void setCurrentResource(AbstractClientPlayer player, ResourceLocation resource, String url);
    public abstract LoadType getDefaultLoadType();
    public abstract String getDefaultUrl();
}