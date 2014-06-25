package vswe.stevescarts.old.Computer;
import java.util.ArrayList;

import vswe.stevescarts.old.Modules.Workers.ModuleComputer;
public class ComputerProg {

	private ModuleComputer module;
	public ComputerProg(ModuleComputer module) {
		this.module = module;
		tasks = new ArrayList<ComputerTask>();
		vars = new ArrayList<ComputerVar>();
		info = (short)1;
	}

	public void start() {
		module.setActiveProgram(this);
		
		if (activeTaskId < 0 || activeTaskId >= tasks.size()) {
			activeTaskId = 0;
			return;
		}
		
		activeTaskId = tasks.get(activeTaskId).preload(this, activeTaskId);
	}
	
	private int activeTaskId;
	public int getActiveId() {
		return activeTaskId;
	}
	
	public void setActiveId(int val) {
		activeTaskId = val;
	}
	
	public int getRunTime() {
		if (activeTaskId < 0 || activeTaskId >= tasks.size()) {
			activeTaskId = 0;
			return 0;
		}else{
			return tasks.get(activeTaskId).getTime();
		}		
	}
	
	public boolean run() {
		if (activeTaskId < 0 || activeTaskId >= tasks.size()) {
			activeTaskId = 0;
			return false;
		}
	
		int result = tasks.get(activeTaskId).run(this, activeTaskId);
		if (result == -1) {
			activeTaskId++;
		}else{
			activeTaskId = result;
		}
		
		if (activeTaskId < 0 || activeTaskId >= tasks.size()) {
			activeTaskId = 0;
			return false;
		}else if(result == -1){
			activeTaskId = tasks.get(activeTaskId).preload(this, activeTaskId);
		}
		

		
		return true;
	}
	
	
	private ArrayList<ComputerTask> tasks;
    private ArrayList<ComputerVar> vars;

	public ArrayList<ComputerTask> getTasks() {
		return tasks;
	}

	public ArrayList<ComputerVar> getVars() {
		return vars;
	}
	
	//the predicted number of tasks, used when syncing the tasks
	public void setTaskCount(int count) {
		while (tasks.size() > count) {
			tasks.remove(tasks.size()-1);
		}
		while (tasks.size() < count) {
			tasks.add(new ComputerTask(module, this));
		}		
	}

	//the predicted number of vars, used when syncing the vars
	public void setVarCount(int count) {
		while (vars.size() > count) {
			vars.remove(vars.size()-1);
		}
		while (vars.size() < count) {
			vars.add(new ComputerVar(module));
		}
	}
	
	public short getInfo() {
		return info;
	}
	
	public void setInfo(short val) {
		info = val;
	}
	
	private short info;
	/**
		0   isProgram
		1-4 TriggeredBy
			0 = None
			1 = Detector Rail
			2 = Adv Detector Rail
			3 = Powered rail
			4 - 15 = Unused
		5 - WillOverride
			0 = False
			1 = True
		6 - CanOverrideSelf
			0 = False
			1 = True
		7-15 - Unused
	**/	
	
	private String myName;
    public void setName(String name)
    {
        myName = name;
    }
    public String getName()
    {
        return myName;
    }
	public String toString() {
		return getName();
	}

}