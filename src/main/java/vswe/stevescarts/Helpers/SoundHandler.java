package vswe.stevescarts.Helpers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import vswe.stevescarts.StevesCarts;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler {
		
		public SoundHandler() {
			if (StevesCarts.instance.useArcadeSounds) {
				MinecraftForge.EVENT_BUS.register(this);
			}
		}
	
	    @SubscribeEvent
		public void onSoundsLoad(SoundLoadEvent event) {	    
            addSound(event,  "gearswitch");  
            addSound(event,  "win");  	          
            if (!StevesCarts.instance.useArcadeMobSounds) {
            	addSound(event,  "boop1");  
            	addSound(event,  "boop2");  
            	addSound(event,  "boop3");  
            }
            addSound(event,  "gameover");  	 
            addSound(event,  "1lines");  	 
            addSound(event,  "2lines");  	 
            addSound(event,  "3lines");  	 
            addSound(event,  "4lines");  	 
            addSound(event,  "highscore");  	 
            addSound(event,  "hit");  	 
            addSound(event,  "click");  	 
            addSound(event,  "blobclick");  	 
            addSound(event,  "flagclick");  	 
            addSound(event,  "goodjob"); 
	        

	    }
	    
	    private void addSound(SoundLoadEvent event, String name) {
	    	 event.manager.soundPoolSounds.addSound("stevescarts:" + name + ".ogg");		    
	    }
	    
	    public static void playSound(String sound, float volume, float pitch) {
	    	 Minecraft.getMinecraft().sndManager.playSoundFX("stevescarts:" + sound, volume, pitch);		    
	    }	    
	    
	    public static void playDefaultSound(String sound, float volume, float pitch) {
	    	 Minecraft.getMinecraft().sndManager.playSoundFX(sound, volume, pitch);		    
	    }		    
	}
	