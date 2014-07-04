package vswe.stevesvehicles.arcade.tracks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.old.Helpers.Localization;

public class TrackLevel {

	public static final TrackLevel editor =  new TrackLevel(Localization.STORIES.THE_BEGINNING.MAP_EDITOR, 0, 0, TrackOrientation.Direction.RIGHT, 26, 9);

	
	//length of name [1 byte]
	//the name  [length of name bytes]
	
	//map header (start player (+ direction), start map, number of tracks) [4 byte]

	//for every track:
	//position [9 bits]
	//type [3 bits]
	//orientation [6 bits]
	//extra length[6 bits]
	//extra data[extra length bytes]	
	
	private static byte getFileVersion() {
		return 0;
	}
	
	private static String MAP_FOLDER_PATH = "sc2/arcade/trackoperator/";
	
	@SideOnly(Side.CLIENT)
	public static ArrayList<TrackLevel> loadMapsFromFolder() {
		ArrayList<TrackLevel> maps = new ArrayList<TrackLevel>();
		try {
			File dir = new File(Minecraft.getMinecraft().mcDataDir, MAP_FOLDER_PATH);

			File[] children = dir.listFiles();
			
			if (children != null) {
				for (File child : children) {
					if (child.isFile()) {
						String name = child.getName();
						
						TrackLevel map = loadMap(name);
						if (map != null) {
							maps.add(map);
						}
					}					
				}
			}
		}catch (Exception exception) {
			System.out.println("Failed to load the maps");
		}
		
		return maps;
	}
	
	@SideOnly(Side.CLIENT)
	public static TrackLevel loadMap(String filename) {
		try {
			byte[] bytes = readFromFile(new File(Minecraft.getMinecraft().mcDataDir, MAP_FOLDER_PATH + filename));

			return loadMapData(bytes);
		}catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	
	public static TrackLevel loadMap(byte[] bytes) {
		try {
			return loadMapData(bytes);
		}catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}	
	
	
	public static TrackLevel loadMapData(byte[] bytes) throws IOException {
		ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		
		
		int version = data.read();
		
		int nameLength = data.read();
		byte[] nameBytes = new byte[nameLength];
		data.read(nameBytes, 0, nameLength);
		String name = new String(nameBytes, Charset.forName("UTF-8"));
		
		int header = (data.read() << 24) | (data.read() << 16) | (data.read() << 8) | (data.read() << 0);
		int playerX = header & 31; //5 bits
		int playerY = (header >> 5) & 15; //4 bits
		TrackOrientation.Direction playerDir = TrackOrientation.Direction.fromInteger((header >> 9) & 3); //2 bits
		int itemX = (header >> 11) & 31; //5 bits
		int itemY = (header >> 16) & 15; //4 bits
		int trackSize = (header >> 20) & 511; //9 bits

		TrackLevel map = new TrackLevel(null, playerX, playerY, playerDir, itemX, itemY);
		
		
		for (int i = 0; i < trackSize; i++) {
			int trackData = (data.read() << 16) | (data.read() << 8) | (data.read() << 0);
			
			int trackX = trackData & 31; //5 bits
			int trackY = (trackData >> 5) & 15; //4 bits
			int type = (trackData >> 9) & 7; //3 bits
			TrackOrientation orientation = TrackOrientation.ALL.get((trackData >> 12) & 63); // 6 bits
			int extraLength = (trackData >> 18) & 63; //6 bits
			
			Track track = TrackEditor.getRealTrack(trackX, trackY, type, orientation);
			
			byte[] extraData = new byte[extraLength];
			data.read(extraData);
			track.setExtraInfo(extraData);

			map.getTracks().add(track);
		}
		
		return map;		
	}
	
	
	
	@SideOnly(Side.CLIENT)
	public static boolean saveMap(String name, int playerX, int playerY, TrackOrientation.Direction playerDir, int itemX, int itemY, ArrayList<Track> tracks) {
		try {
			byte[] bytes = saveMapData(name, playerX, playerY, playerDir, itemX, itemY, tracks);
			writeToFile(new File(Minecraft.getMinecraft().mcDataDir,"sc2/arcade/trackoperator/" + name.replace(" ", "_") + ".dat"), bytes);
			return true;
		}catch(IOException ex) {		
			return false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static String saveMapToString(String name, int playerX, int playerY, TrackOrientation.Direction playerDir, int itemX, int itemY, ArrayList<Track> tracks) {
		try {
			byte[] bytes = saveMapData(name, playerX, playerY, playerDir, itemX, itemY, tracks);
			String str = "TrackLevel.loadMap(new byte[] {";
					
			for (int i = 0; i < bytes.length; i++) {
				if (i != 0) {
					str += ",";
				}
				
				str += bytes[i];
			}
					
			str += "});";
			return str;
		}catch(IOException ex) {		
			return "";
		}
	}	

	@SideOnly(Side.CLIENT)
	public static byte[] saveMapData(String name, int playerX, int playerY, TrackOrientation.Direction playerDir, int itemX, int itemY, ArrayList<Track> tracks) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(stream);
		

		data.writeByte(getFileVersion());
		
		data.writeByte(name.length());
		data.writeBytes(name);
		
		int header = 0;
		header |= playerX; //5 bits
		header |= playerY << 5; //4 bits
		header |= playerDir.toInteger() << 9; // 2 bits
		header |= itemX << 11; //5 bits
		header |= itemY << 16; //4 bits
		header |= tracks.size() << 20; //9 bits
		
		data.writeInt(header);
		
		for (Track track : tracks) {
			int trackdata = 0;
			byte[] extraData = track.getExtraInfo();
			
			trackdata |= track.getX(); //5 bits
			trackdata |= track.getY() << 5; //4 bits
			trackdata |= track.getU() << 9; //3 bits
			trackdata |= track.getOrientation().toInteger() << 12; // 6 bits
			trackdata |= extraData.length << 18; // 6 bits
			

			//only need 3 bytes
			data.write((trackdata & (255 << 16)) >> 16);
			data.write((trackdata & (255 << 8)) >> 8);
			data.write(trackdata & 255);
			
			

			data.write(extraData);
			

		}
		
		return stream.toByteArray();		
	}
	
	@SideOnly(Side.CLIENT)
	private static void writeToFile(File file, byte[] bytes) throws IOException {		
		createFolder(file.getParentFile());		
		
		FileOutputStream writer = new FileOutputStream(file);			 
		writer.write(bytes);
		writer.close();		
	}
	
	@SideOnly(Side.CLIENT)
	private static byte[] readFromFile(File file) throws IOException {		
		createFolder(file.getParentFile());	
		FileInputStream reader = new FileInputStream(file);
	    byte bytes[] = new byte[(int)file.length()];  
	    
	    reader.read(bytes);	   
	    reader.close();		
	    
	    return bytes;
	}

	@SideOnly(Side.CLIENT)
	private static void createFolder(File dir) throws IOException {
		if (dir == null) {
			return;
		}
		
		File parent = dir.getParentFile();
		createFolder(parent);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
	}
	

	
	private ILocalizedText name;
	private int playerX;
	private int playerY;
	private TrackOrientation.Direction playerDir;
	private int itemX;
	private int itemY;
	private ArrayList<Track> tracks;
	private ArrayList<LevelMessage> messages;

	
	public TrackLevel(Localization.STORIES.THE_BEGINNING name, int playerX, int playerY, TrackOrientation.Direction playerDir, int itemX, int itemY) {
		this.name = name;
		this.playerX = playerX;
		this.playerY = playerY;
		this.playerDir = playerDir;
		this.itemX = itemX;
		this.itemY = itemY;
		
		tracks = new ArrayList<Track>();
		messages = new ArrayList<LevelMessage>();
	}
	
	
	public String getName() {
		return name.translate();
	}

    public void setName(ILocalizedText name) {
        this.name = name;
    }
		
	public int getPlayerStartX() {
		return playerX;
	}
	
	public int getPlayerStartY() {
		return playerY;
	}
	
	public TrackOrientation.Direction getPlayerStartDirection() {
		return playerDir;
	}
	
	public int getItemX() {
		return itemX;
	}
	
	public int getItemY() {
		return itemY;
	}
	
	public ArrayList<Track> getTracks() {
		return tracks;
	}
	
	public ArrayList<LevelMessage> getMessages() {
		return messages;
	}

	public void addMessage(LevelMessage levelMessage) {
		messages.add(levelMessage);
	}
	
	
}
