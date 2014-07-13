package vswe.stevesvehicles.fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.StringUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@SideOnly(Side.CLIENT)
public class FancyPancy {

    private FancyPancyHandler fancyPancyHandler;
    public int priority = 0;
    private String[] images;
    private int currentImage = 0;
    private EnumSet<AnimationType> types;
    private int interval = 50;
    private int intervalWait = 200;
    private int ticks;
    private String[] servers;
    private boolean hasServerBlackList;
    private LoadType loadType;
    private List<SpecialLoadSetting> loadPixelSettings;
    private String[] teams;
    private List<int[]> dimensions;
    private List<long[]> times;
    private boolean useLocalTime = true;
    private String[] observers;
    private boolean hasObserverBlacklist;
    private String[] observerTeams;

    public FancyPancy(FancyPancyHandler fancyPancyHandler, HashMap<String, String[]> entries) {
        this.fancyPancyHandler = fancyPancyHandler;
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
        setObservers(entries.get("Observer"), entries.get("BlockedObserver"), entries.get("ObserverTeam"));
        loadType = fancyPancyHandler.getDefaultLoadType();
    }


    private void setObservers(String[] observers, String[] blockedObservers, String[] observerTeams) {
        if (observers != null && observers.length > 0) {
            this.observers = observers;
            hasObserverBlacklist = false;
        }else if(blockedObservers != null && blockedObservers.length > 0) {
            this.observers = blockedObservers;
            hasObserverBlacklist = true;
        }

        this.observerTeams = observerTeams;
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

    public void update() {
        if (types.contains(AnimationType.PAUSE) && currentImage == images.length - 1 && intervalWait >= 0) {
            if (++ticks >= intervalWait) {
                ticks = 0;
                nextImage();
            }
        }else if(!types.contains(AnimationType.STILL) && interval >= 0) {
            if (++ticks >= interval) {
                ticks = 0;
                nextImage();
            }
        }
    }

    private void setTypes(String[] typeStrings) {
        types = EnumSet.noneOf(AnimationType.class);
        if (typeStrings != null) {
            for (String typeString : typeStrings) {
                for (AnimationType animation : AnimationType.values()) {
                    if (animation.getCode().equals(typeString)) {
                        types.add(animation);
                        break;
                    }
                }
            }
        }

        if (types.size() == 0) {
            types.add(AnimationType.STILL);
        }else if(types.contains(AnimationType.RANDOM)) {
            nextImage();
        }
    }

    private void nextImage() {
        if (types.contains(AnimationType.RANDOM)) {
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
                }else if (image.startsWith("*")) {
                    String defaultPath = fancyPancyHandler.getDefaultUrl();
                    if (defaultPath == null) {
                        defaultPath = "https://dl.dropbox.com/u/46486053/";
                    }
                    image = defaultPath + image.substring(1) + ".png";
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

    public boolean isValid(AbstractClientPlayer player, boolean hasMojangFancy, boolean usingMojangFancy) {
        if (!isObserverValid()) {
            return false;
        }

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

        if (hasMojangFancy && !usingMojangFancy && loadPixelSettings != null) {
            return false;
        }


        boolean specialCheck = loadPixelSettings == null || (usingMojangFancy && doSpecialCheck(player));

        switch (loadType) {
            case KEEP:
                return !hasMojangFancy || !specialCheck;
            case OVERRIDE:
                return !hasMojangFancy || specialCheck;
            case REQUIRE:
                return hasMojangFancy && specialCheck;
        }

        return false;
    }


    private boolean isObserverValid() {
        AbstractClientPlayer observerPlayer = Minecraft.getMinecraft().thePlayer;
        String observerName = StringUtils.stripControlCodes(observerPlayer.getDisplayName());

        if (observers != null && observers.length != 0) {
            boolean foundObserver = false;
            for (String observer : observers) {
                if (observer.equals(observerName)) {
                    foundObserver = true;
                    break;
                }
            }

            if (hasObserverBlacklist == foundObserver) {
                return false;
            }
        }

        return isTeamValid(observerPlayer, observerTeams);
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
        return isTeamValid(player, teams);
    }
    private boolean isTeamValid(AbstractClientPlayer player, String[] teams) {
        if (teams == null || teams.length == 0) {
            return true;
        }

        Team team = player.getTeam();
        String requiredTeam = team == null ? "~" : team.getRegisteredName();

        for (String teamName : teams) {
            if (teamName.equals(requiredTeam)) {
                return true;
            }
        }
        return false;
    }

    private boolean doSpecialCheck(AbstractClientPlayer player) {
        ThreadDownloadImageData data = fancyPancyHandler.getCurrentTexture(player);
        if (data == null) {
            return false;
        }
        BufferedImage image = ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, data, 2);
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
                if(server.equals(fancyPancyHandler.getServerHash())) {
                    return !hasServerBlackList;
                }
            }

            return hasServerBlackList;
        }
    }

    public void setLoad(String[] loadSettings) {
        if (loadSettings != null && loadSettings.length > 0) {
            String loadTypeString = loadSettings[0];

            for (LoadType load : LoadType.values()) {
                if (load.getCode().equals(loadTypeString)) {
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
