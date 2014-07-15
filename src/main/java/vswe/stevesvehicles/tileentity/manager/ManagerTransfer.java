package vswe.stevesvehicles.tileentity.manager;

import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

public class ManagerTransfer {
	private int side;
	private int setting;
	private int lastSetting;
	private int lowestSetting;
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
		lastSetting = 0;
		lowestSetting = 0;
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
    	return lastSetting;
    }    
    
    public void setLastSetting(int val) {
    	lastSetting = val;
    }   
    
    public int getLowestSetting() {
    	return lowestSetting;
    }    
    
    public void setLowestSetting(int val) {
    	lowestSetting = val;
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
