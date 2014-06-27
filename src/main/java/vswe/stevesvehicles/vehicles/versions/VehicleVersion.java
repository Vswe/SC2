package vswe.stevesvehicles.vehicles.versions;
import vswe.stevesvehicles.old.Items.ItemCarts;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import vswe.stevesvehicles.vehicles.VehicleBase;

public abstract class VehicleVersion {

	private static List<VehicleVersion> versions;

	public VehicleVersion() {
		versions.add(this);
	}

    public static final String NBT_VERSION_STRING = "VehicleVersion";
	public abstract void update(List<Integer> modules);

	
	static {
		versions = new ArrayList<VehicleVersion>();
		
		/**
			------- THE LIQUID UPDATE -------
			
			The Hydrators loses their liquid storages. Therefore the Large
			Hydrator will be replaced by the normal one. Also, if a cart
			had a Hydrator that cart will receive a set of side tanks.
		**/
		new VehicleVersion() {
			public void update(List<Integer> modules) {
				/*
					Replace the large hydrator with a "normal" one.
				*/
				int index = modules.indexOf(17);
				if (index != -1) {
					modules.set(index, 16);
				}
				
				/*
					Add side tanks to compensate that the Hydrators lost of liquid storage.
				*/
				if (modules.contains(16)) {
					modules.add(64);
				}
			}
		};
		
		/**
			------- THE DURABILITY UPDATE -------
			
			All tools has their durability set to 100%.
			Note that nothing is actually done here, this
			is because the carts and items manages to 
			handle everything pretty well. The only thing
			that has to be done is to move items in some
			carts(since the Tool Modules now has an extra slot)
			and that is solved elsewhere (this is just a 
			placeholder to give that change an id).
		 **/
		new VehicleVersion() {
			public void update(List<Integer> modules) {
				
			}
		};
	}

	public static int[] updateCart(VehicleBase cart, int[] data) {
		if (cart.cartVersion != getCurrentVersion()) {	
			data = updateArray(data, cart.cartVersion);
			cart.cartVersion = (byte)getCurrentVersion();
		}
		
		return data;
	}
	
	private static int[] updateArray(int[] data, int version) {
		List<Integer> modules = new ArrayList<Integer>();
		for (int id : data) {
			modules.add(id);
		}
		
		while (version < getCurrentVersion()) {
			versions.get(version++).update(modules);
		}

		data = new int[modules.size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = modules.get(i);
		}	
		return data;
	}
	
	public static void updateItemStack(ItemStack item) {
		if (item != null && item.getItem() instanceof ItemCarts) {
			NBTTagCompound info = item.getTagCompound();
			if (info != null) {
				int version = info.getByte(NBT_VERSION_STRING);
				if (version != getCurrentVersion()) {					
					//info.setByteArray("Modules", updateArray(info.getByteArray("Modules"), version)); //TODO
					addVersion(info);
				}	
			}
		}
	}
	
	public static void addVersion(ItemStack item) {
		if (item != null && item.getItem() instanceof ItemCarts) {
			NBTTagCompound info = item.getTagCompound();
			if (info != null) {
				addVersion(info);
			}
		}
	}
	
	private static void addVersion(NBTTagCompound info) {
		info.setByte(NBT_VERSION_STRING, (byte)getCurrentVersion());
	}

	private static int getCurrentVersion() {
		return versions.size();
	}

}