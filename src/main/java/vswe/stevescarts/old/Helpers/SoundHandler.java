package vswe.stevescarts.old.Helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler {

    public static void playDefaultSound(String name, float volume, float pitch) {
        ISound soundObj = new PlayerSound(Minecraft.getMinecraft().thePlayer, name, volume, pitch);
        Minecraft.getMinecraft().getSoundHandler().playSound(soundObj);
    }


    public static void playSound(String name, float volume, float pitch) {
        playDefaultSound("stevescarts:" + name, volume, pitch);
    }

    private static class PlayerSound extends PositionedSound {

        private EntityPlayer player;
        protected PlayerSound(EntityPlayer player, String name, float volume, float pitch) {
            super(new ResourceLocation(name));

            this.player = player;
            this.volume = volume;
            this.field_147663_c = pitch;
            update();
        }


        private void update() {
            xPosF = (float)player.posX;
            yPosF = (float)player.posY;
            zPosF = (float)player.posZ;
        }

    }
}

	