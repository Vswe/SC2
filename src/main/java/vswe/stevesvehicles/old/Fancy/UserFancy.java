package vswe.stevesvehicles.old.Fancy;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class UserFancy {
    private FancyPancyHandler fancyPancyHandler;
    private FancyPancy activeFancyPancy;
    private int activeCheck = 100;
    private List<FancyPancy> fancies = new ArrayList<FancyPancy>();
    private boolean hasMojangFancy;
    private boolean doneMojangFancyCheck;

    public UserFancy(FancyPancyHandler fancyPancyHandler) {
        this.fancyPancyHandler = fancyPancyHandler;
    }

    public void update(AbstractClientPlayer player) {
        updateMojangFancyState(player);

        if (activeFancyPancy != null) {
            activeFancyPancy.update();
        }

        if (++activeCheck >= 100 || (activeCheck >= 20 && player.equals(Minecraft.getMinecraft().thePlayer))) {
            activeCheck = 0;

            updateActive(player);
        }
    }

    private void updateMojangFancyState(AbstractClientPlayer player) {
        if (!doneMojangFancyCheck) {
            ThreadDownloadImageData fancyData = fancyPancyHandler.getCurrentTexture(player);
            if (fancyData != null) {
                Thread thread = ReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, fancyData, 5);
                if (thread != null && !thread.isAlive()) {
                    //1 finished looking for fancy
                    //2 managed to download a fancy
                    //3 has Mojang fancy path

                    hasMojangFancy = fancyData.isTextureUploaded();
                    doneMojangFancyCheck = true;
                    updateActive(player);
                }
            }else{
                doneMojangFancyCheck = true;
                updateActive(player);
            }
        }
    }

    private void updateActive(AbstractClientPlayer player) {
        activeFancyPancy = null;

        if (!doneMojangFancyCheck) {
            return;
        }



        int highest = Integer.MIN_VALUE;
        for (FancyPancy fancyPancy : fancies) {
            highest = findHighPriorityFancy(player, fancyPancy, highest);
        }
        ServerFancy serverFancy = fancyPancyHandler.getServerFancies().get(fancyPancyHandler.getServerHash());
        if (serverFancy != null) {
            for (FancyPancy fancyPancy : serverFancy.getFancies()) {
                highest = findHighPriorityFancy(player, fancyPancy, highest);
            }
        }

    }

    private int findHighPriorityFancy(AbstractClientPlayer player, FancyPancy fancyPancy, int highest) {
        if (fancyPancy.isValid(player, hasMojangFancy, isUsingMojangFancy(player))) {
            if (fancyPancy.priority > highest) {
                highest = fancyPancy.priority;
                activeFancyPancy = fancyPancy;
            }
        }
        return highest;
    }

    private boolean isUsingMojangFancy(AbstractClientPlayer player) {
        return hasMojangFancy && fancyPancyHandler.getDefaultResource(player).equals(fancyPancyHandler.getCurrentResource(player));
    }

    public void add(FancyPancy fancyPancy) {
        fancies.add(fancyPancy);
    }

    public String getImage(AbstractClientPlayer player) {
        if (activeFancyPancy != null) {
            return activeFancyPancy.getImage();
        }else {
            //always go back to the default fancy (even if it doesn't exist)
            return fancyPancyHandler.getDefaultUrl(player);
        }
    }
}
