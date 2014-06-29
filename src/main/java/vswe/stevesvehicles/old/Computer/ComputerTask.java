package vswe.stevesvehicles.old.Computer;
import vswe.stevesvehicles.module.cart.attachment.ModuleComputer;
import vswe.stevesvehicles.vehicle.entity.EntityModularCart;

import java.util.Random;

public class ComputerTask
{
	private static Random rand = new Random();
	private ModuleComputer module;
	private ComputerProg prog;
    public ComputerTask(ModuleComputer module, ComputerProg prog)
    {
		this.module = module;
		this.prog = prog;
    }

	public int getTime() {
		return 5;
	}
	
	//returns the id of the next task, the given id is the id of the current task
    public int run(ComputerProg prog, int id) {
		if (isFlowGoto()) {
			int labelId = getFlowLabelId();
			for (int i = 0; i < prog.getTasks().size(); i++) {
				ComputerTask task = prog.getTasks().get(i);
				if (task.isFlowLabel() && task.getFlowLabelId() == labelId) {
					return i;
				}
			}
		}else if(isFlowCondition()) {
			boolean condition = evalFlowCondition();
			int nested = 0;
			if (!condition) {
				if (isFlowIf() || isFlowElseif()) {
					for (int i = id + 1; i < prog.getTasks().size(); i++) {
						ComputerTask task = prog.getTasks().get(i);
						
						if (task.isFlowIf()) {
							nested++;
						}else if(task.isFlowElseif() || task.isFlowElse() || task.isFlowEndif()) {
							if (nested == 0) {
								return i;
							}else if(task.isFlowEndif()){
								nested--;
							}
							
						}
					}
				}else if(isFlowWhile()) {
					for (int i = id + 1; i < prog.getTasks().size(); i++) {
						ComputerTask task = prog.getTasks().get(i);
						
						if (task.isFlowWhile()) {
							nested++;
						}else if(task.isFlowEndwhile()) {
							if (nested == 0) {
								return i;
							}else{
								nested--;
							}							
						}
					}				
				}
			}
		}else if(isFlowFor()) {
			boolean condition = evalFlowFor();
			if (!condition) {
				int nested = 0;
				for (int i = id + 1; i < prog.getTasks().size(); i++) {
					ComputerTask task = prog.getTasks().get(i);
					
					if (task.isFlowFor()) {
						nested++;
					}else if(task.isFlowEndfor()) {
						if (nested == 0) {
							return i;
						}else{
							nested--;
						}							
					}
				}
			}	
		}else if(isFlowContinue() || isFlowBreak()) {
			int nested = 0;
			for (int i = id + 1; i < prog.getTasks().size(); i++) {
				ComputerTask task = prog.getTasks().get(i);
				
				if (task.isFlowWhile() || task.isFlowFor()) {
					nested++;
				}else if(task.isFlowEndwhile() || task.isFlowEndfor()) {
					if (nested == 0) {
						if (isFlowContinue()) {
							return task.preload(prog, i);
						}else{
							return i;
						}
					}else{
						nested--;
					}							
				}
			}	
		}else if(isVar(getType()) && !isVarEmpty()) {
			ComputerVar var = getVarVar();
			if (var != null) {
				int value1;
				int value2;
				
				if (getVarUseFirstVar()) {
					ComputerVar var1 = getVarFirstVar();
					if (var1 == null) {
						return -1;
					}else{
						value1 = var1.getByteValue();
					}
				}else{
					value1 = getVarFirstInteger();
				}
				
				if (hasTwoValues()) {
					if (getVarUseSecondVar()) {
						ComputerVar var2 = getVarSecondVar();
						if (var2 == null) {
							return -1;
						}else{
							value2 = var2.getByteValue();
						}
					}else{
						value2 = getVarSecondInteger();
					}		
				}else{
					value2 = 0;
				}				
				
				var.setByteValue(calcVarValue(value1, value2));
			}
		}else if(isControl(getType()) && !isControlEmpty()) {
			ComputerControl control = ComputerControl.getMap().get((byte)getControlType());
			if (control != null && control.isControlValid((EntityModularCart)module.getVehicle().getEntity())) {
				int value;
				if (getControlUseVar()) {
					ComputerVar var = getControlVar();
					if (var == null) {
						return -1;
					}else{
						value = var.getByteValue();
					}
				}else{
					value = getControlInteger();
				}		

				control.runHandler((EntityModularCart)module.getVehicle().getEntity(), (byte)value);
			}	
		}else if(isInfo(getType()) && !isInfoEmpty()) {
			ComputerInfo info = ComputerInfo.getMap().get((byte)getControlType());
			if (info != null && info.isInfoValid((EntityModularCart)module.getVehicle().getEntity())) {
				ComputerVar var = getInfoVar();
				if (var != null) {
					info.getHandler((EntityModularCart)module.getVehicle().getEntity(), var);
				}
			}
		}else if(isVar(getType())) {
			for (ComputerVar var : prog.getVars()) {
				System.out.println(var.getFullInfo());
			}
		}
	
		return -1;
	}

	//allows a command to preload just after the last command ran
    public int preload(ComputerProg prog, int id) { 
		if (isFlowElseif() || isFlowElse()) {
			int nested = 0;		
			for (int i = id + 1; i < prog.getTasks().size(); i++) {
				ComputerTask task = prog.getTasks().get(i);	
				if (task.isFlowIf()) {
					nested++;
				}else if(task.isFlowEndif()) {
					if (nested == 0) {
						return i;
					}else{
						nested--;
					}					
				}
			}
		}else if(isFlowEndwhile()) {
			int nested = 0;		
			for (int i = id - 1; i >= 0; i--) {
				ComputerTask task = prog.getTasks().get(i);	
				if (task.isFlowEndwhile()) {
					nested++;
				}else if(task.isFlowWhile()) {
					if (nested == 0) {
						return i;
					}else{
						nested--;
					}					
				}
			}		
		}else if(isFlowFor()) {
			ComputerVar var = getFlowForVar();
			if (var != null) {
				if (getFlowForUseStartVar()) {
					ComputerVar var2 = getFlowForStartVar();
					if (var2 != null) {
						var.setByteValue(var2.getByteValue());
					}
				}else{
					var.setByteValue(getFlowForStartInteger());
				}
			}
		}else if(isFlowEndfor()) {
			System.out.println("End for");
			int nested = 0;		
			for (int i = id - 1; i >= 0; i--) {
				ComputerTask task = prog.getTasks().get(i);	
				if (task.isFlowEndfor()) {
					nested++;
				}else if(task.isFlowFor()) {
					if (nested == 0) {
						ComputerVar var = task.getFlowForVar();
						if (var != null) {
							int dif = task.getFlowForDecrease() ? -1 : 1;
							var.setByteValue(var.getByteValue() + dif);
						}
						return i;
					}else{
						nested--;
					}					
				}
			}	
		}
	
		return id;
	}
	
    public ComputerTask clone() {
		ComputerTask clone = new ComputerTask(module, prog);
		clone.info = info;
		return clone;
	}

	public ComputerProg getProgram() {
		return prog;
	}	
	
	
	private int info;
	/**
		b0 - active
		b1-b3 - type
			0 = not set
			1 = flow control
				b4-b7 - operator
					0 = not set
					
					1 = label
					2 = goto
						b8-b12 - id
							
					3 = if
					4 = else if
					6 = while
						b8-b12 - var1
							0 = not set
							1-31 = variable 0 to 30
						b13-b15 - comparison
							0 = equals
							1 = not equals
							2 = greater than or equals to
							3 = greater than
							4 = lesser than or equals to
							5 = lesser than
							6-7 = *unused*
						b16 - value type
							0 = integer
								b17-b24 - integer value -128 to 127
							1 = variable
								b17-b21 - var2
									0 = not set
									1-31 = variable 0 to 30
								b22-b24 - *unused*
				
					5 = else

					7 = for
						b8-b12 - loop var
							0 = not set
							1-31 = variable 0 to 30
						b13 - start value type
							0 = integer
								b14-b21 - integer value -128 to 127
							1 = variable
								b14-b18 - start var
									0 = not set
									1-31 = variable 0 to 30
								b19-b21 - *unused*
						b22 - end value type
							0 = integer
								b23-b30 - integer value -128 to 127
							1 = variable
								b23-b27 - end var
									0 = not set
									1-31 = variable 0 to 30
								b28-b30 - *unused*
						b31 - direction
							0 = increase
							1 = decrease 
						
					8 = end
						b8-b9 end type
							9 = not set
							1 = if
							2 = while
							3 = for
							
					9 = break
					10 = continue
					
					11-15 = *unused*
			2 = variable control
				b4-b8 - control type
					0 = not set
					
					
					1 = set value
					10 = not
					15 = abs					
						b9-b13 - varriable
							0 = not set
							1-31 = variable 0 to 30
						b14 - value type
							0 = integer
								b15-b22 - integer value -128 to 127
							1 = value variable
								15-b19 - end var
									0 = not set
									1-31 = variable 0 to 30				
								b20-b22 - *unused*

								
					2 = addition
					3 = subtraction
					4 = multiplication
					5 = int div
					6 = mod
					7 = and
					8 = or
					9 = xor
					11 = shift right
					12 = shift left
					13 = max
					14 = min
					16 = clamp
						b9-b13 - varriable
							0 = not set
							1-31 = variable 0 to 30
						b14 - first value type
							0 = integer
								b15-b22 - integer value -128 to 127
							1 = first value variable
								15-b19 - end var
									0 = not set
									1-31 = variable 0 to 30	
								b20-b22 - *unused*
						b23 - second value type
							0 = integer
								b24-b31 - integer value -128 to 127
							1 = second value variable
								b24-b28 - end var
									0 = not set
									1-31 = variable 0 to 30
								b29-b31 - *unused*
								
					17-31 = *unused*
					
			3 = module control
				b4-b11 - control id
				b12 - second value type
					0 = integer
						b13-b20 - integer value -128 to 127
					1 = second value variable
						b13-b17 - end var
							0 = not set
							1-31 = variable 0 to 30
						b18-b20 - *unused*
			4 = module info	
				b4-b11 - info id
				b12-b16 - end var
					0 = not set
					1-31 = variable 0 to 30			
			5 = build
			6 = addon
			7 = *unused*
			

	**/
	
	public void setInfo(int id, short val) {
		int iVal = val;
		if (iVal < 0){
			iVal += 65536;
		}
	
		boolean oldVal = getIsActivated();
	
		info &= ~(65535 << (id * 16));
		info |= iVal << (id * 16);
		
		if (oldVal != getIsActivated()) {
			module.activationChanged();
		}		
	}
	
	public short getInfo(int id) {
		return (short)((info & (65535 << (id * 16))) >> (id * 16));
	}
	
	
	public void setIsActivated(boolean val) {
		boolean oldVal = getIsActivated();
	
		info &= ~1;
		info |= val ? 1 : 0;
		
		if (oldVal != val) {
			module.activationChanged();
		}
	}
	
	public boolean getIsActivated() {
		return (info & 1) != 0;
	}
	
	/** ==========================
			  GENERAL TYPE
	    ========================== **/
	
	public void setType(int type) {
		int oldType = getType();
		boolean flag = isBuild(oldType);
	
		info &= ~14;
		info |= type << 1;
		
		if (oldType != type && (!flag || !isBuild(type))) {
			info &= 15;
		}
	}
	
	public int getType() {
		return (info & 14) >> 1;
	}
	
	public static boolean isEmpty(int type) {
		return type == 0;
	}
	
	public static boolean isFlow(int type) {
		return type == 1;
	}
	
	public static boolean isVar(int type) {
		return type == 2;
	}
	
	public static boolean isControl(int type) {
		return type == 3;
	}
	
	public static boolean isInfo(int type) {
		return type == 4;
	}	

	public static boolean isBuild(int type) {
		return type == 5 || isAddon(type);
	}	
	
	public static boolean isAddon(int type) {
		return type == 6;
	}	
	
	public int getImage() {
		if (isEmpty(getType())) {
			return -1;
		}else if(isFlow(getType())) {
			return getFlowImageForTask();
		}else if(isVar(getType())) {
			return getVarImage(getVarType());
		}else if(isControl(getType())) {
			return getControlImage(getControlType());
		}else if(isInfo(getType())) {
			return getInfoImage(getInfoType());			
		}else{
			return -1;
		}
	}
	
	public static String getTypeName(int type) {
		switch(type) {
			default:
				return "Empty";
			case 1:
				return "Flow Control";
			case 2:
				return "Variable Control";
			case 3:
				return "Module Control";
			case 4:
				return "Module Info";
			case 5:
				return "Builder";
			case 6:
				return "Addon";
		}
	}
	
	@Override
	public String toString() {
		if (isEmpty(getType())) {
			return "Empty";
		}else if(isFlow(getType())) {
			return getFlowTypeName(getFlowType()) + " " + getFlowText();
		}else if(isVar(getType())) {		
			return getVarTypeName(getVarType()) + ": " + getVarText();
		}else if(isControl(getType())) {		
			return "Set " + getControlTypeName(getControlType()) + " to " + getControlText();
		}else if(isInfo(getType())) {		
			return "Set " + getVarName(getInfoVar()) + " to " + getInfoTypeName(getInfoType());			
		}else{
			return "Unknown";
		}		
	}
	
	
	/** ==========================
				FLOW CONTROL
	    ========================== **/	
		
		
	public int getFlowType() {
		return (info & (15 << 4)) >> 4;
	}
	
	public void setFlowType(int type) {
		int oldType = getFlowType();
		
		if (oldType == type) {
			return;
		}
		
		boolean conditionFlag = isFlowCondition();
	
		//Remove the old type
		info &= ~(15 << 4);
		
		//Add the new type
		info |= type << 4;
		
		//Reset the rest of the flow task
		if (!conditionFlag || !isFlowCondition()) { 
			info &= 255;		
		}
	}
	
	public boolean isFlowEmpty() {
		return isFlow(getType()) && getFlowType() == 0;
	}
	
	public boolean isFlowLabel() {
		return isFlow(getType()) && getFlowType() == 1;
	}
	
	public boolean isFlowGoto() {
		return isFlow(getType()) && getFlowType() == 2;
	}	
	
	public boolean isFlowIf() {
		return isFlow(getType()) && getFlowType() == 3 ;	
	}
	
	public boolean isFlowElseif() {
		return isFlow(getType()) && getFlowType() == 4 ;	
	}	
	
	public boolean isFlowElse() {
		return isFlow(getType()) && getFlowType() == 5 ;	
	}
	
	public boolean isFlowWhile() {
		return isFlow(getType()) && getFlowType() == 6 ;	
	}	
	
	public boolean isFlowFor() {
		return isFlow(getType()) && getFlowType() == 7;	
	}	
	
	public boolean isFlowEnd() {
		return isFlow(getType()) && getFlowType() == 8;	
	}	
	
	public boolean isFlowBreak() {
		return isFlow(getType()) && getFlowType() == 9;	
	}		
	
	public boolean isFlowContinue() {
		return isFlow(getType()) && getFlowType() == 10;	
	}		
	
	public boolean isFlowCondition() {
		return isFlowIf() || isFlowElseif() || isFlowWhile();
	}
	
	public static int getFlowImage(int type) {
		return 12 + type;
	}
	
	public int getFlowImageForTask() {
		if (isFlowEnd()) {
			return getEndImage(getFlowEndType());
		}else{
			return getFlowImage(getFlowType());
		}
	}
	
	public static String getFlowTypeName(int type) {
		switch(type) {
			default:
				return "Empty";
			case 1:
				return "Label";
			case 2:
				return "GoTo";
			case 3:
				return "If";
			case 4:
				return "Else if";
			case 5:
				return "Else";
			case 6:
				return "While";
			case 7:
				return "For";
			case 8:
				return "End";
			case 9:
				return "Break";
			case 10:
				return "Continue";
				
		}
	}	
	
	public String getFlowText() {
		if (isFlowLabel() || isFlowGoto()) {
			return "[" + getFlowLabelId() + "]";
		}else if(isFlowCondition()) {
			ComputerVar var = getFlowConditionVar();
			String str = getVarName(var);
			str += " ";
			str += getFlowOperatorName(getFlowConditionOperator(), false);	
			str += " ";
			
			if (getFlowConditionUseSecondVar()) {
				ComputerVar var2 = getFlowConditionSecondVar();
				str += getVarName(var2);
			}else{
				str += getFlowConditionInteger();
			}
			
			return str;
		}else if(isFlowFor()) {
			String str = getVarName(getFlowForVar());
			str += " = ";
			
			if (getFlowForUseStartVar()) {
				str += getVarName(getFlowForStartVar());
			}else{
				str += getFlowForStartInteger();
			}	

			str += " to ";
			
			if (getFlowForUseEndVar()) {
				str += getVarName(getFlowForEndVar());
			}else{
				str += getFlowForEndInteger();
			}			
			
			str += "  step " + (getFlowForDecrease() ? "-" : "+") + "1";
			
			return str;
		}else if(isFlowEnd()) {
			return getEndTypeName(getFlowEndType());
		}else{
			return "(Not set)";
		}		
	}	
	
	/** ==========================
				LABEL
	    ========================== **/		
	
	public int getFlowLabelId() {
		return (info & (31 << 8)) >> 8;
	}
	
	public void setFlowLabelId(int id) {
		if (id < 0) {
			id = 0;
		}else if(id > 31) {
			id = 31;
		}
	
		info &= ~(31 << 8);
		info |= id << 8;
	}	
	
	/** ==========================
				CONDITION
	    ========================== **/		
	
	public int getFlowConditionVarIndex() {
		return getVarIndex(8);
	}
	
	public ComputerVar getFlowConditionVar() {
		return getVar(8);
	}
	
	public void setFlowConditionVar(int val) {
		setVar(8, val);
	}	
		
	public int getFlowConditionOperator() {
		return ((info & (7 << 13)) >> 13);
	}

	public void setFlowConditionOperator(int val) {
		info &= ~(7 << 13);
		info |= val << 13;
	}	
	
	public boolean isFlowConditionOperatorEquals() {
		return getFlowConditionOperator() == 0;
	}	
	
	public boolean isFlowConditionOperatorNotequals() {
		return getFlowConditionOperator() == 1;
	}		

	public boolean isFlowConditionOperatorGreaterequals() {
		return getFlowConditionOperator() == 2;
	}
	
	public boolean isFlowConditionOperatorGreater() {
		return getFlowConditionOperator() == 3;
	}	
	
	public boolean isFlowConditionOperatorLesserequals() {
		return getFlowConditionOperator() == 4;
	}	
	
	public boolean isFlowConditionOperatorLesser() {
		return getFlowConditionOperator() == 5;
	}	
	
	public boolean getFlowConditionUseSecondVar() {
		return getUseOptionalVar(16);
	}
	
	public void setFlowConditionUseSecondVar(boolean val) {
		setUseOptionalVar(16, val);
	}
	
	public int getFlowConditionInteger() {
		return getInteger(17);
	}
	
	public void setFlowConditionInteger(int val) {
		setInteger(17, val);
	}	
	

	public int getFlowConditionSecondVarIndex() {
		return getVarIndex(17);
	}
	
	public ComputerVar getFlowConditionSecondVar() {
		return getVar(17);
	}
	
	public void setFlowConditionSecondVar(int val) {
		setVar(17, val);
	}	
			
		
	public boolean evalFlowCondition() {
        if (!isFlowCondition()) {
			return false;
		}
		
		ComputerVar var = getFlowConditionVar();
		if (var == null) {
			return false;
		}
		
		int varValue = var.getByteValue();
		int compareWith;
		
		if (getFlowConditionUseSecondVar()) {
			ComputerVar var2 = getFlowConditionVar();
			if (var2 == null) {
				return false;
			}
			
			compareWith = var2.getByteValue();
		}else{
			compareWith = getFlowConditionInteger();
		}
		
		if (isFlowConditionOperatorEquals()) {
			return varValue == compareWith;
		}else if(isFlowConditionOperatorNotequals()) {
			return varValue != compareWith;
		}else if(isFlowConditionOperatorGreaterequals()) {
			return varValue >= compareWith;
		}else if(isFlowConditionOperatorGreater()) {
			return varValue > compareWith;
		}else if(isFlowConditionOperatorLesserequals()) {
			return varValue <= compareWith;
		}else if(isFlowConditionOperatorLesser()) {
			return varValue < compareWith;
		}else{
			return false;
		}
			
	}
	
	
	public static String getFlowOperatorName(int type, boolean isLong) {
		switch (type) {
			default:
				return isLong ? "Unknown" : "?";
			case 0:
				return isLong ? "Equals to" : "=";
			case 1:
				return isLong ? "Not equals to" : "!=";
			case 2:
				return isLong ? "Greater than or equals to" : ">=";
			case 3:
				return isLong ? "Greater than" : ">";
			case 4:
				return isLong ? "Smaller than or equals to" : "<=";
			case 5:
				return isLong ? "Smaller than" : "<";
		}
	}	
	
	/** ==========================
				FOR
	    ========================== **/	
		

	public int getFlowForVarIndex() {
		return getVarIndex(8);
	}
	
	public ComputerVar getFlowForVar() {
		return getVar(8);
	}
	
	public void setFlowForVar(int val) {
		setVar(8, val);
	}		

	
	
	public boolean getFlowForUseStartVar() {
		return getUseOptionalVar(13);
	}
	
	public void setFlowForUseStartVar(boolean val) {
		setUseOptionalVar(13, val);
	}
	
	public int getFlowForStartInteger() {
		return getInteger(14);
	}
	
	public void setFlowForStartInteger(int val) {
		setInteger(14, val);
	}	
	
	public int getFlowForStartVarIndex() {
		return getVarIndex(14);
	}
	
	public ComputerVar getFlowForStartVar() {
		return getVar(14);
	}
	
	public void setFlowForStartVar(int val) {
		setVar(14, val);
	}	
	
	
	
	public boolean getFlowForUseEndVar() {
		return getUseOptionalVar(22);
	}
	
	public void setFlowForUseEndVar(boolean val) {
		setUseOptionalVar(22, val);
	}
	
	public int getFlowForEndInteger() {
		return getInteger(23);
	}
	
	public void setFlowForEndInteger(int val) {
		setInteger(23, val);
	}	
	
	public int getFlowForEndVarIndex() {
		return getVarIndex(23);
	}
	
	public ComputerVar getFlowForEndVar() {
		return getVar(23);
	}
	
	public void setFlowForEndVar(int val) {
		setVar(23, val);
	}		
	
	public boolean getFlowForDecrease() {
		return (info & (1 << 31)) != 0;
	}
	
	public void setFlowForDecrease(boolean val) {
		info &= ~(1 << 31);
		info |= (val ? 1 : 0) << 31;
	}	
	
	public boolean evalFlowFor() {
        if (!isFlowFor()) {
			return false;
		}
		
		ComputerVar var = getFlowForVar();
		if (var == null) {
			return false;
		}
		
		int varValue = var.getByteValue();
		int compareWith;
		
		if (getFlowForUseEndVar()) {
			ComputerVar var2 = getFlowForEndVar();
			if (var2 == null) {
				return false;
			}
			
			compareWith = var2.getByteValue();
		}else{
			compareWith = getFlowForEndInteger();
		}
		

		//int dif = task.getFlowForDecrease() ? -1 : 1;
		return varValue != compareWith; //+ dif;		
	}


	
	
	/** ==========================
				END
	    ========================== **/		

	public int getFlowEndType() {
		return (info & (3 << 8)) >> 8;
	}
	
	public void setFlowEndType(int val) {
		if (val < 0) {
			val = 0;
		}else if(val > 3){
			val = 3;
		}
	
		info &= ~(3 << 8);
		info |= val << 8;
	}		
		
	public boolean isFlowEndif() {
		return isFlowEnd() && getFlowEndType() == 1;
	}

	public boolean isFlowEndwhile() {
		return isFlowEnd() && getFlowEndType() == 2;
	}
	
	public boolean isFlowEndfor() {
		return isFlowEnd() && getFlowEndType() == 3;
	}

	public static String getEndTypeName(int type) {
		switch (type) {
			default:
				return "(not set)";
			case 1:
				return "If";
			case 2:
				return "While";
			case 3:
				return "For";
		}
	}
	
	public static int getEndImage(int type) {
		if (type == 0) {
			return 20;
		}else{
			return 45 + type;
		}
	}
	

	/** ==========================
				VAR CONTROL
	    ========================== **/		
	
	
	public int getVarType() {
		return (info & (31 << 4)) >> 4;
	}
	
	public void setVarType(int val) {
		info &= ~(31 << 4);
		info |= val << 4;
	}

	public boolean isVarEmpty() {
		return isVar(getType()) && getVarType() == 0;
	}	

	public boolean isVarSet() {
		return isVar(getType()) && getVarType() == 1;
	}		
	
	public boolean isVarAdd() {
		return isVar(getType()) && getVarType() == 2;
	}		
	
	public boolean isVarSub() {
		return isVar(getType()) && getVarType() == 3;
	}		
	
	public boolean isVarMult() {
		return isVar(getType()) && getVarType() == 4;
	}		
	
	public boolean isVarDiv() {
		return isVar(getType()) && getVarType() == 5;
	}		
	
	public boolean isVarMod() {
		return isVar(getType()) && getVarType() == 6;
	}

	public boolean isVarAnd() {
		return isVar(getType()) && getVarType() == 7;
	}			

	public boolean isVarOr() {
		return isVar(getType()) && getVarType() == 8;
	}		

	public boolean isVarXor() {
		return isVar(getType()) && getVarType() == 9;
	}	

	public boolean isVarNot() {
		return isVar(getType()) && getVarType() == 10;
	}		

	public boolean isVarShiftR() {
		return isVar(getType()) && getVarType() == 11;
	}

	public boolean isVarShiftL() {
		return isVar(getType()) && getVarType() == 12;
	}	

	public boolean isVarMax() {
		return isVar(getType()) && getVarType() == 13;
	}		
	
	public boolean isVarMin() {
		return isVar(getType()) && getVarType() == 14;
	}	

	public boolean isVarAbs() {
		return isVar(getType()) && getVarType() == 15;
	}

	public boolean isVarClamp() {
		return isVar(getType()) && getVarType() == 16;
	}

	public boolean isVarRand() {
		return isVar(getType()) && getVarType() == 17;
	}	

	public boolean hasOneValue() {
		return isVarSet() || isVarNot() || isVarAbs();
	}
	
	public boolean hasTwoValues() {
		return !isVarEmpty() && !hasOneValue();
	}
	

	
	public int getVarVarIndex() {
		return getVarIndex(9);
	}
	
	public ComputerVar getVarVar() {
		return getVar(9);
	}
	
	public void setVarVar(int val) {
		setVar(9, val);
	}

	public boolean getVarUseFirstVar() {
		return getUseOptionalVar(14);
	}
	
	public void setVarUseFirstVar(boolean val) {
		setUseOptionalVar(14, val);
	}
	
	public int getVarFirstInteger() {
		return getInteger(15);
	}
	
	public void setVarFirstInteger(int val) {
		setInteger(15, val);
	}		
	
	public int getVarFirstVarIndex() {
		return getVarIndex(15);
	}
	
	public ComputerVar getVarFirstVar() {
		return getVar(15);
	}
	
	public void setVarFirstVar(int val) {
		setVar(15, val);
	}			
	
	
	
	public boolean getVarUseSecondVar() {
		return getUseOptionalVar(23);
	}
	
	public void setVarUseSecondVar(boolean val) {
		setUseOptionalVar(23, val);
	}
	
	public int getVarSecondInteger() {
		return getInteger(24);
	}
	
	public void setVarSecondInteger(int val) {
		setInteger(24, val);
	}		
	
	public int getVarSecondVarIndex() {
		return getVarIndex(24);
	}
	
	public ComputerVar getVarSecondVar() {
		return getVar(24);
	}
	
	public void setVarSecondVar(int val) {
		setVar(24, val);
	}	
	

	public static String getVarTypeName(int type) {
		switch(type) {
			default:
				return "Empty";
			case 1:
				return "Set";
			case 2:
				return "Addition";
			case 3:
				return "Subtraction";
			case 4:
				return "Multiplication";
			case 5:
				return "Integer division";
			case 6:
				return "Modulus";
			case 7:
				return "Bitwise And";
			case 8:
				return "Bitwise Or";
			case 9:
				return "Bitwise Xor";
			case 10:
				return "Bitwise Not";	
			case 11:
				return "Right Bitshift";
			case 12:
				return "Left Bitshift";
			case 13:
				return "Maximum Value";
			case 14:
				return "Minimum Value";	
			case 15:
				return "Absolute Value";				
			case 16:
				return "Clamp Value";
			case 17:
				return "Random Value";					
		}
	}	
	

	public String getVarPrefix() {
		if (isVarMax()) {
			return "max(";
		}else if(isVarMin()) {
			return "min(";
		}else if(isVarClamp()) {
			return "clamp(" + getVarName(getVarVar()) + ", ";
		}else if(isVarAbs()) {
			return "abs(";
		}else if(isVarNot()) {
			return "~";
		}else if(isVarRand()) {
			return "rand(";
		}else{
			return "";
		}
	}
	
	public String getVarMidfix() {
		if (isVarMax() || isVarMin() || isVarClamp() || isVarRand()) {
			return ", ";
		}else if(isVarAdd()) {
			return " + ";
		}else if(isVarSub()) {
			return " - ";
		}else if(isVarMult()) {
			return " * ";
		}else if(isVarDiv()) {
			return " / ";
		}else if(isVarMod()) {
			return " % ";
		}else if(isVarAnd()) {
			return " & ";
		}else if(isVarOr()) {
			return " | ";
		}else if(isVarXor()) {
			return " ^ ";
		}else if(isVarShiftR()) {
			return " >> ";
		}else if(isVarShiftL()) {
			return " << ";
		}else{
			return "";
		}
	}

	public String getVarPostfix() {
		if (isVarMax() || isVarMin() || isVarClamp() || isVarAbs() || isVarRand()) {
			return ")";
		}else{
			return "";		
		}
	}	
	
	public String getVarText() {
		if (isVarEmpty()) {
			return "(Not set)";
		}else{
			String str = "";
			str += getVarName(getVarVar());
			str += " = ";
			str += getVarPrefix();
			if (getVarUseFirstVar()) {
				str += getVarName(getVarFirstVar());
			}else{
				str += getVarFirstInteger();
			}
			
			if (hasTwoValues()) {
				str += getVarMidfix();
				if (getVarUseSecondVar()) {
					str += getVarName(getVarSecondVar());
				}else{
					str += getVarSecondInteger();
				}				
			}
			
			str += getVarPostfix();
			return str;
		}
	}
	
	public static int getVarImage(int type) {
		if (type == 17) {
			return 98;
		}else{
			return 49 + type;
		}
	}	
	
	public int calcVarValue(int val1, int val2) {
		if (isVarSet()) {
			return val1;
		}else if (isVarAdd()) {
			return val1 + val2;
		}else if(isVarSub()) {
			return val1 - val2;
		}else if(isVarMult()) {
			return val1 * val2;
		}else if(isVarDiv()) {
			return val1 / val2;
		}else if(isVarMod()) {
			return val1 % val2;
		}else if(isVarAnd()) {
			return val1 & val2;
		}else if(isVarOr()) {
			return val1 | val2;
		}else if(isVarXor()) {
			return val1 ^ val2;
		}else if(isVarNot()) {
			byte b = (byte)val1;
			b = (byte)(~b);
			return b;
		}else if(isVarShiftR()) {
			val2 = Math.max(val2, 8);
			val2 = Math.min(val2, 0);
			return val1 >> val2;
		}else if(isVarShiftL()) {
			val2 = Math.max(val2, 8);
			val2 = Math.min(val2, 0);
			return val1 << val2;
		}else if(isVarMax()) {
			return Math.max(val1, val2);
		}else if(isVarMin()) {
			return Math.min(val1, val2);
		}else if(isVarAbs()) {
			return Math.abs(val1);
		}else if(isVarClamp()) {
			int temp = getVarVar().getByteValue();
			temp = Math.max(temp, val1);
			temp = Math.min(temp, val2);
			return temp;
		}else if(isVarRand()) {
			val2++;
			if (val2 <= val1) {
				return 0;
			}else{
				return rand.nextInt(val2-val1) + val1;
			}
		}else{
			return 0;	
		}

	}

	/** ==========================
				MODULE CONTROL
	    ========================== **/		
	
		
	public int getControlType() {
		return (info & (255 << 4)) >> 4;
	}
	
	public void setControlType(int val) {
		info &= ~(255 << 4);
		info |= val << 4;
		
		if (!getControlUseVar()) {
			int min = getControlMinInteger();
			int max = getControlMaxInteger();
			if (getControlInteger() < min) {
				setControlInteger(min);
			}else if (getControlInteger() > max) {
				setControlInteger(max);
			}
		}
	}	
	
	public boolean isControlEmpty() {
		return getControlType() == 0;
	}
	
	public static String getControlTypeName(int type) {
		if (type == 0) {
			return "Empty";
		}else{
			ComputerControl control = ComputerControl.getMap().get((byte)type);
			if (control == null) {
				return "(not set)";
			}else{
				return control.getName();
			}
		}
	}
	
	public static int getControlImage(int type) {
		if (type == 0) {
			return 68;
		}else{
			ComputerControl control = ComputerControl.getMap().get((byte)type);
			if (control == null) {
				return -1;
			}else{
				return control.getTexture();
			}
		}
	}
	
	public String getControlText() {
		if (isControlEmpty()) {
			return "(not set)";
		}else if(!isControlActivator()){
			if (getControlUseVar()) {
				ComputerVar var = getControlVar();
				return getVarName(var);
			}else{
				return String.valueOf(getControlInteger());
			}
		}else {
			return "Activate";
		}
	}
	

	public boolean getControlUseVar() {
		return getUseOptionalVar(12);
	}
	
	public void setControlUseVar(boolean val) {
		setUseOptionalVar(12, val);
	}
	
	public int getControlInteger() {
		return getInteger(13);
	}
	
	public void setControlInteger(int val) {
		setInteger(13, val);
	}		
	
	public int getControlVarIndex() {
		return getVarIndex(13);
	}
	
	public ComputerVar getControlVar() {
		return getVar(13);
	}
	
	public void setControlVar(int val) {
		setVar(13, val);
	}	

	public int getControlMinInteger() {
		ComputerControl control = ComputerControl.getMap().get((byte)getControlType());
		if (control == null) {
			return -128;
		}else{
			return control.getIntegerMin();
		}
	}
	
	public int getControlMaxInteger() {
		ComputerControl control = ComputerControl.getMap().get((byte)getControlType());
		if (control == null) {
			return 127;
		}else{
			return control.getIntegerMax();
		}		
	}
	
	public boolean getControlUseBigInteger(int size) {
		ComputerControl control = ComputerControl.getMap().get((byte)getControlType());
		if (control == null) {
			return false;
		}else{
			return control.useIntegerOfSize(size);
		}			
	}
	
	public boolean isControlActivator() {
		ComputerControl control = ComputerControl.getMap().get((byte)getControlType());
		if (control == null) {
			return false;
		}else{
			return control.isActivator();
		}		
	}
	
	/** ==========================
				MODULE INFO
	    ========================== **/		
	
		
	public int getInfoType() {
		return (info & (255 << 4)) >> 4;
	}
	
	public void setInfoType(int val) {
		info &= ~(255 << 4);
		info |= val << 4;
	}	
		
	public boolean isInfoEmpty() {
		return getInfoType() == 0;
	}
	
	public static String getInfoTypeName(int type) {
		if (type == 0) {
			return "Empty";
		}else{
			ComputerInfo info = ComputerInfo.getMap().get((byte)type);
			if (info == null) {
				return "(not set)";
			}else{
				return info.getName();
			}
		}
	}
	
	public static int getInfoImage(int type) {
		if (type == 0) {
			return 83;
		}else{
			ComputerInfo info = ComputerInfo.getMap().get((byte)type);
			if (info == null) {
				return -1;
			}else{
				return info.getTexture();
			}
		}
	}
				
	public int getInfoVarIndex() {
		return getVarIndex(12);
	}
	
	public ComputerVar getInfoVar() {
		return getVar(12);
	}
	
	public void setInfoVar(int val) {
		setVar(12, val);
	}			
	
	
	/** ==========================
				GENERIC STUFF
	    ========================== **/		
	
	private static String getVarName(ComputerVar var) {
		if (var == null) {
			return "(not set)";
		}else{
			return var.getText();
		}
	}
	
	private int getInteger(int startBit) {
		int val =((info & (255 << startBit)) >> startBit);
		if (val > 127) {
			return val - 255;
		}else{
			return val;
		}
	}
	
	private void setInteger(int startBit, int val) {
		if (val < -128) {
			val = -128;
		}else if(val > 127) {
			val = 127;
		}
	
		if (val < 0) {
			val += 256;
		}
	
		info &= ~(255 << startBit);
		info |= val << startBit;
	}
	
	private boolean getUseOptionalVar(int startBit) {
		return (info & (1 << startBit)) != 0;
	}
	
	private void setUseOptionalVar(int startBit, boolean val) {
		if (val == getUseOptionalVar(startBit)) {
			return;
		}
		info &= ~(1 << startBit);
		info |= (val ? 1 : 0) << startBit;
		
		setInteger(startBit + 1, 0);
	}	
	
	private int getVarIndex(int startBit) {
		return ((info & (31 << startBit)) >> startBit) - 1;
	}
	
	public ComputerVar getVar(int startBit) {
		int ind = getVarIndex(startBit);
		
		if (ind < 0 || ind >= prog.getVars().size()) {
			return null;
		}else{
			return prog.getVars().get(ind);
		}
	}
	
	public void setVar(int startBit, int val) {
		if (val < -1) {
			val = -1;
		}else if(val >= prog.getVars().size()) {
			val = prog.getVars().size() - 2;
		}
		
		val += 1;
		info &= ~(31 << startBit);
		info |= val << startBit;
	}		
}
