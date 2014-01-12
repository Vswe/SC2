package vswe.stevescarts.Helpers;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
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

    private HashMap<String,UserCape> capes;
    private HashMap<String,ServerCape> serverCapes;
    private boolean ready = false;
    private String serverHash;
    private int serverReHash;

	public CapeHandler() {
		TickRegistry.registerTickHandler(this, Side.CLIENT);
		capes = new HashMap<String,UserCape>();
        serverCapes = new HashMap<String, ServerCape>();
        new Thread(this).start();
	}


    private static final int PROTOCOL_VERSION = 0;

    @Override
    public void run() {
        try {
			HttpURLConnection connection = (HttpURLConnection)new URL("https://dl.dropbox.com/u/46486053/RemoteInfo.txt").openConnection();
			HttpURLConnection.setFollowRedirects(true);
			connection.setConnectTimeout(2147483647);
			connection.setDoInput(true);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Vswe\\Dropbox\\Public\\RemoteInfo.txt")/*connection.getInputStream()*/));
			String line;


            while ((line = reader.readLine()) != null) {

                int commentStart = line.indexOf("#");
                if (commentStart != -1) {
                    line = line.substring(0, commentStart);
                }

                line = line.trim();

				String[] split = line.split(":");

                if (split.length == 2) {
                    String command = split[0];
                    String content = split[1];

                    HashMap<String, String[]> entries = new HashMap<String, String[]>();
                    int start = 0;
                    while (true) {


                        int startIndex = content.indexOf("[", start);
                        if (startIndex == -1) {
                            break;
                        }
                        String entry = content.substring(start, startIndex).trim();
                        int endIndex = content.indexOf("]", startIndex);
                        if (endIndex == -1) {
                            break;
                        }
                        start = endIndex + 1;
                        String values = content.substring(startIndex + 1, endIndex).trim();
                        String[] valueSplit = values.split(",");
                        for (int i = 0; i < valueSplit.length; i++) {
                            valueSplit[i] = valueSplit[i].trim();
                        }
                        entries.put(entry, valueSplit);
                    }


                    if (command.equals("C")) {
                        String[] users = entries.get("User");
                        if (users != null) {

                            for (String user : users) {
                                UserCape userCape = capes.get(user);
                                if (userCape == null) {
                                    userCape = new UserCape();
                                    capes.put(user, userCape);
                                }

                                userCape.add(new Cape(entries));
                            }
                        }else{
                            String[] servers = entries.get("Server");
                            if (servers != null) {
                                for (String server : servers) {
                                    ServerCape serverCape = serverCapes.get(server);
                                    if (serverCape == null) {
                                        serverCape = new ServerCape();
                                        serverCapes.put(server, serverCape);
                                    }

                                    serverCape.add(new Cape(entries));
                                }
                            }
                        }
                    }
                }

			}
		}catch(Throwable ex) {
	        ex.printStackTrace();
		}

        ready = true;
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

    private String getServerHash() {
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
	
	@Override	
	public void tickStart(EnumSet type, Object... tickData) {
        if (!ready) {
            return;
        }

        if (serverReHash > 0) {
            serverReHash--;
        }

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
            UserCape capeObj = capes.get(username);
            if (capeObj == null && serverCapes.size() > 0 && serverCapes.containsKey(getServerHash())) {
                capeObj = new UserCape();
                capes.put(username, capeObj);
            }

            if (capeObj != null) {
                capeObj.update(player);
                String cape = capeObj.getImage(player);

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

    private class ServerCape {
        private List<Cape> capes = new ArrayList<Cape>();
        private void add(Cape cape) {
            capes.add(cape);
        }
    }

    private class UserCape {
        private Cape activeCape;
        private int activeCheck = 100;
        private List<Cape> capes = new ArrayList<Cape>();
        private boolean hasMojangCape;
        private boolean doneMojangCapeCheck;

        private void update(AbstractClientPlayer player) {
            updateMojangCapeState(player);

            if (activeCape != null) {
                activeCape.update();
            }

            if (++activeCheck >= 100 || (activeCheck >= 20 && player.equals(Minecraft.getMinecraft().thePlayer))) {
                activeCheck = 0;

                updateActive(player);
            }
        }

        private void updateMojangCapeState(AbstractClientPlayer player) {
            if (!doneMojangCapeCheck) {
                ThreadDownloadImageData capeData = player.getTextureCape();
                Thread thread = ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, capeData, 3);
                if (thread != null && !thread.isAlive()) {
                    //1 finished looking for cape
                    //2 managed to download a cape
                    //3 has Mojang cape path

                    hasMojangCape = capeData.isTextureUploaded();
                    doneMojangCapeCheck = true;
                    updateActive(player);
                }
            }
        }

        private void updateActive(AbstractClientPlayer player) {
            activeCape = null;

            if (!doneMojangCapeCheck) {
                return;
            }



            int highest = Integer.MIN_VALUE;
            for (Cape cape : capes) {
                highest = findHighPriorityCape(player, cape, highest);
            }
            ServerCape serverCape = serverCapes.get(getServerHash());
            if (serverCape != null) {
                for (Cape cape : serverCape.capes) {
                    highest = findHighPriorityCape(player, cape, highest);
                }
            }

        }

        private int findHighPriorityCape(AbstractClientPlayer player, Cape cape, int highest) {
            if (cape.isValid(player, hasMojangCape, isUsingMojangCape(player))) {
                if (cape.priority > highest) {
                    highest = cape.priority;
                    activeCape = cape;
                }
            }
            return highest;
        }

        private boolean isUsingMojangCape(AbstractClientPlayer player) {
            return hasMojangCape && AbstractClientPlayer.getLocationCape(player.username).equals(player.getLocationCape());
        }

        private void add(Cape cape) {
            capes.add(cape);
        }

        private String getImage(AbstractClientPlayer player) {
            if (activeCape != null) {
                return activeCape.getImage();
            }else {
                //always go back to the default cape (even if it doesn't exist)
                return AbstractClientPlayer.getCapeUrl(player.username);
            }
        }
    }

    private class Cape {

        public int priority = 0;
        private String[] images;
        private int currentImage = 0;
        private EnumSet<ANIMATION_TYPE> types;
        private int interval = 50;
        private int intervalWait = 200;
        private int ticks;
        private String[] servers;
        private boolean hasServerBlackList;
        private LOAD_TYPE loadType = LOAD_TYPE.KEEP;
        private List<SpecialLoadSetting> loadPixelSettings;
        private String[] teams;
        private List<int[]> dimensions;
        private List<long[]> times;
        private boolean useLocalTime = true;

        public Cape(HashMap<String, String[]> entries) {
            addImages(entries.get("Image"));
            setTypes(entries.get("Animation"));
            setInterval(entries.get("Interval"));
            setPriority(entries.get("Priority"));
            setServers(entries.get("Server"), entries.get("Blocked"));
            setLoad(entries.get("Load"));
            setTeams(entries.get("Team"));
            setDimensions(entries.get("World"));
            setTimes(entries.get("Time"));
            setTimeMode(entries.get("Zone"));
        }

        private void setTimeMode(String[] zones) {
            if (zones != null && zones.length> 0) {
                if (zones[0].equals("Local")) {
                    useLocalTime = true;
                }else if(zones[0].equals("GMT")) {
                    useLocalTime = false;
                }
            }
        }

        public void setTimes(String[] times) {
            if (times != null) {
                this.times = new ArrayList<long[]>();


                for (String level : times) {
                    String[] split = level.split(";");
                    if (split.length == 2) {

                        try {
                            long[] result = new long[split.length];

                            for (int i = 0; i < split.length; i++) {
                                result[i] = Long.parseLong(split[i]);
                            }
                            this.times.add(result);

                        }catch (Exception ignored) {}
                    }
                }

                if (this.times.size() == 0) {
                    this.times = null;
                }
            }
        }

        public void setDimensions(String[] dimensions) {
            if (dimensions != null) {
                this.dimensions = new ArrayList<int[]>();


                for (String dimension : dimensions) {
                    String[] split = dimension.split(";");
                    if (split.length == 1 || split.length == 2) {
                        try {
                            int[] result = new int[split.length];
                            for (int i = 0; i < split.length; i++) {
                                result[i] = Integer.parseInt(split[i].trim());
                            }
                            this.dimensions.add(result);
                        }catch (Exception ignored) {}
                    }
                }

                if (this.dimensions.size() == 0) {
                    this.dimensions = null;
                }
            }
        }

        private void update() {
            if (types.contains(ANIMATION_TYPE.PAUSE) && currentImage == images.length - 1 && intervalWait >= 0) {
                if (++ticks >= intervalWait) {
                    ticks = 0;
                    nextImage();
                }
            }else if(!types.contains(ANIMATION_TYPE.STILL) && interval >= 0) {
                if (++ticks >= interval) {
                    ticks = 0;
                    nextImage();
                }
            }
        }

        private void setTypes(String[] typeStrings) {
            types = EnumSet.noneOf(ANIMATION_TYPE.class);
            if (typeStrings != null) {
                for (String typeString : typeStrings) {
                    for (ANIMATION_TYPE animation : ANIMATION_TYPE.values()) {
                        if (animation.code.equals(typeString)) {
                            types.add(animation);
                            break;
                        }
                    }
                }
            }

            if (types.size() == 0) {
                types.add(ANIMATION_TYPE.STILL);
            }else if(types.contains(ANIMATION_TYPE.RANDOM)) {
                nextImage();
            }
        }

        private void nextImage() {
            if (types.contains(ANIMATION_TYPE.RANDOM)) {
                currentImage = (int)(Math.random() * images.length);
            }else{
                currentImage = (currentImage + 1) % images.length;
            }
        }

        public String getImage() {
            if (images == null) {
                return null;
            }

            return images[currentImage];
        }

        public void setInterval(String[] intervals) {
            if (intervals != null) {
                if (intervals.length >= 1) {
                    try {
                        interval = Integer.parseInt(intervals[0]);
                        if (intervals.length >= 2) {
                            intervalWait = Integer.parseInt(intervals[1]);
                        }
                    }catch (Exception ignored) {}
                }
            }
        }

        public void addImages(String[] images) {
            if (images != null) {
                this.images = new String[images.length];

                for (int i = 0; i < images.length; i++) {
                    String image = images[i];

                    if (image.startsWith("^")) {
                        image = image.substring(1);
                    }else{
                        image = "https://dl.dropbox.com/u/46486053/" + image + ".png";
                    }

                    this.images[i] = image;
                }
            }
        }

        public void setPriority(String[] priorities) {
            if (priorities != null) {
                try {
                    priority = Integer.parseInt(priorities[0]);
                }catch (Exception ignored) {}
            }
        }

        public void setServers(String[] serversWhiteList, String[] serversBlackList) {
            if(serversWhiteList != null && serversWhiteList.length > 0) {
                servers = serversWhiteList;
                hasServerBlackList = false;
            }else if(serversBlackList != null && serversBlackList.length > 0) {
                servers = serversBlackList;
                hasServerBlackList = true;
            }
        }

        public boolean isValid(AbstractClientPlayer player, boolean hasMojangCape, boolean usingMojangCape) {
            if (!isServerValid()) {
                return false;
            }

            if (!isTimeValid()) {
                return false;
            }

           if (!isTeamValid(player)) {
               return false;
           }

            if (!isDimensionValid(player)) {
                return false;
            }

            if (hasMojangCape && !usingMojangCape && loadPixelSettings != null) {
                return false;
            }


            boolean specialCheck = loadPixelSettings == null || (usingMojangCape && doSpecialCheck(player));

            switch (loadType) {
                case KEEP:
                    return !hasMojangCape || !specialCheck;
                case OVERRIDE:
                    return !hasMojangCape || specialCheck;
                case REQUIRE:
                    return hasMojangCape && specialCheck;
            }

            return false;
        }

        private boolean isTimeValid() {
            if (times == null) {
                return true;
            }else{
                Calendar calendar;
                if (useLocalTime) {
                    calendar = Calendar.getInstance();
                }else{
                   calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                }

                long now = (calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis())) / 1000 ;
                for (long[] time : times) {
                    if (time[0] <= now && now <= time[1]) {
                        return true;
                    }
                }

                return false;
            }
        }

        private boolean isDimensionValid(AbstractClientPlayer player) {
            if (dimensions == null || dimensions.size() == 0) {
                return true;
            }else if (player.worldObj == null || player.worldObj.provider == null) {
                return false;
            }

            int id = player.worldObj.provider.dimensionId;

            for (int[] levelRange : dimensions) {
                if ((levelRange.length == 1 && levelRange[0] == id) || (levelRange.length == 2 && levelRange[0] <= id && id <= levelRange[1])) {
                    return true;
                }
            }

            return false;
        }

        private boolean isTeamValid(AbstractClientPlayer player) {
            if (teams == null || teams.length == 0) {
                return true;
            }

            Team team = player.getTeam();
            String requiredTeam = team == null ? "~" : team.func_96661_b();

            for (String teamName : teams) {
                if (teamName.equals(requiredTeam)) {
                    return true;
                }
            }
            return false;
        }

        private boolean doSpecialCheck(AbstractClientPlayer player) {
            BufferedImage image = ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, player.getTextureCape(), 2);
            if (image != null) {
                for (SpecialLoadSetting loadPixelSetting : loadPixelSettings) {
                    int color = image.getRGB(loadPixelSetting.x, loadPixelSetting.y);
                    int R = (color >>> 16) & 0xFF;
                    int G = (color >>> 8) & 0xFF;
                    int B = color & 0xFF;

                    if (R == loadPixelSetting.r && G == loadPixelSetting.g && B == loadPixelSetting.b) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean isServerValid() {
            if (servers == null) {
                return true;
            }else{
                for (String server : servers) {
                    if(server.equals(getServerHash())) {
                        return !hasServerBlackList;
                    }
                }

                return hasServerBlackList;
            }
        }

        public void setLoad(String[] loadSettings) {
            if (loadSettings != null && loadSettings.length > 0) {
                String loadTypeString = loadSettings[0];

                for (LOAD_TYPE load : LOAD_TYPE.values()) {
                    if (load.code.equals(loadTypeString)) {
                        loadType = load;
                        break;
                    }
                }

                if (loadSettings.length > 1) {
                    loadPixelSettings = new ArrayList<SpecialLoadSetting>();
                    for (int i = 1; i < loadSettings.length; i++) {
                        String setting = loadSettings[i];
                        String[] split = setting.split(";");
                        if (split.length == 5) {
                            try {
                                SpecialLoadSetting special = new SpecialLoadSetting();
                                special.x = Integer.parseInt(split[0].trim());
                                special.y = Integer.parseInt(split[1].trim());
                                special.r = Integer.parseInt(split[2].trim());
                                special.g = Integer.parseInt(split[3].trim());
                                special.b = Integer.parseInt(split[4].trim());
                                loadPixelSettings.add(special);
                            }catch (Exception ignored) {}
                        }
                    }
                    if (loadPixelSettings.size() == 0)  {
                        loadPixelSettings = null;
                    }
                }
            }
        }

        public void setTeams(String[] teams) {
            this.teams = teams;
        }

        private class SpecialLoadSetting {
            private int x, y, r, g, b;
        }


    }

    private enum LOAD_TYPE {
        KEEP("Keep"),
        OVERRIDE("Override"),
        REQUIRE("Require");

        private String code;

        private LOAD_TYPE(String code) {
            this.code = code;
        }
    }

    private enum ANIMATION_TYPE {
        STILL("Still"),
        ANIMATION("Loop"),
        PAUSE("Pause"),
        RANDOM("Random");

        private String code;

        private ANIMATION_TYPE(String code) {
            this.code = code;
        }
    }
}