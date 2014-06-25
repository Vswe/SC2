package vswe.stevesvehicles.old.Helpers;

import vswe.stevesvehicles.vehicles.entities.EntityModularCart;

public class ManagerTransfer {
	private int side;
	private int setting;
	private int lastsetting;
	private int lowestsetting;
	private int workload;
	private EntityModularCart cart;
	private boolean toCartEnabled;
	private boolean fromCartEnabled;
	
	public ManagerTransfer() {
		reset();
	}
	
	public void reset() {
		side = 0;
		setting = -1;
		lastsetting = 0;	
		lowestsetting = 0;
		workload = 0;
		cart = null;
		toCartEnabled = true;
		fromCartEnabled = true;
	}
	
	
	
    public int getSetting() {
    	return setting;
    }
    
    public void setSetting(int val) {
    	setting = val;
    }   

    public int getSide() {
    	return side;
    }
    
    public void setSide(int val) {
    	side = val;
    }    
    
    public int getLastSetting() {
    	return lastsetting;
    }    
    
    public void setLastSetting(int val) {
    	lastsetting = val;
    }   
    
    public int getLowestSetting() {
    	return lowestsetting;
    }    
    
    public void setLowestSetting(int val) {
    	lowestsetting = val;
    }    
    
    public int getWorkload() {
    	return workload;
    }    
    
    public void setWorkload(int val) {
    	workload = val;
    }      
    
    public EntityModularCart getCart() {
    	return cart;
    }
    
    public void setCart(EntityModularCart val) {
    	cart = val;
    }
	
	public boolean getFromCartEnabled() {
		return fromCartEnabled;
	}
	
	public void setFromCartEnabled(boolean val) {
		fromCartEnabled = val;
	}	
	
	public boolean getToCartEnabled() {
		return toCartEnabled;
	}
	
	public void setToCartEnabled(boolean val) {
		toCartEnabled = val;
	}
        
}
