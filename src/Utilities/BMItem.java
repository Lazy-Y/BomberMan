package Utilities;

public class BMItem {
	public static final int speedup = 0;
	public static final int speeddown = 1;
	public static final int powerup = 2;
	public static final int powerdown = 3;
	public static final int coolingfast = 4;
	public static final int coolingslow = 5;
	public static final int detonatingfast = 6;
	public static final int detonatingslow = 7;
	private int value;
	public BMItem(int value){
		value -= 5;
		if (value < 0 || value > 7);
		else this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
}
