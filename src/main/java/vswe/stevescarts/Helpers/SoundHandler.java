package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import vswe.stevescarts.StevesCarts;
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

    private static class PlayerSound extends PositionedSound implements IUpdatePlayerListBox {

        private EntityPlayer player;
        protected PlayerSound(EntityPlayer player, String name, float volume, float pitch) {
            super(new ResourceLocation(name));

            this.player = player;
            this.volume = volume;
            this.field_147663_c = pitch;
        }

        @Override
        public void update() {
            xPosF = (float)player.posX;
            yPosF = (float)player.posY;
            zPosF = (float)player.posZ;
        }

    }
}

	