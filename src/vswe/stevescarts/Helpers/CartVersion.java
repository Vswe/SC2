package vswe.stevescarts.Helpers;
import vswe.stevescarts.Items.ItemCarts;
import java.util.ArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
public abstract class CartVersion {

	private static ArrayList<CartVersion> versions;

	public CartVersion() {
		versions.add(this);
	}

	public abstract void update(ArrayList<Byte> modules);

	
	static {
		versions = new ArrayList<CartVersion>();
		
		/**
			------- THE LIQUID UPDATE -------
			
			The Hydrators loses their liquid storages. Therefore the Large
			Hydrator will be replaced by the normal one. Also, if a cart
			had a Hydrator that cart will receive a set of side tanks.
		**/
		new CartVersion() {
			public void update(ArrayList<Byte> modules) {
				/*
					Replace the large hydrator with a "normal" one.
				*/
				int index = modules.indexOf((byte)17);
				if (index != -1) {
					modules.set(index, (byte)16);
				}
				
				/*
					Add side tanks to compensate that the Hydrators lost of liquid storage.
				*/
				if (modules.contains((byte)16)) {
					modules.add((byte)64);
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
		new CartVersion() {
			public void update(ArrayList<Byte> modules) {
				
			}
		};
	}

	public static byte[] updateCart(MinecartModular cart, byte[] data) {
		if (cart.cartVersion != getCurrentVersion()) {	
			data = updateArray(data, cart.cartVersion);
			cart.cartVersion = (byte)getCurrentVersion();
		}
		
		return data;
	}
	
	private static byte[] updateArray(byte[] data, int version) {
		ArrayList<Byte> modules = new ArrayList<Byte>();
		for (byte b : data) {
			modules.add(b);
		}
		
		while (version < getCurrentVersion()) {
			versions.get(version++).update(modules);
		}
		
		data = new byte[modules.size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = modules.get(i);
		}	
		return data;
	}
	
	public static void updateItemStack(ItemStack item) {
		if (item != null && item.getItem() instanceof ItemCarts) {
			NBTTagCompound info = item.getTagCompound();
			if (info != null) {
				int version = info.getByte("CartVersion");
				if (version != getCurrentVersion()) {					
					info.setByteArray("Modules", updateArray(info.getByteArray("Modules"), version));
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
		info.setByte("CartVersion", (byte)getCurrentVersion());
	}

	private static int getCurrentVersion() {
		return versions.size();
	}

}