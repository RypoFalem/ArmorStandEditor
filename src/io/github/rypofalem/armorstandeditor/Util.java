package io.github.rypofalem.armorstandeditor;

import org.bukkit.ChatColor;

public abstract class Util {
	
	public static final double FULLCIRCLE = Math.PI*2;

	//inserts a formatting character between every character, making it invisible when displayed as lore
	public static String encodeHiddenLore(String lore){
		for(int i = 0; i < lore.length(); i+=2){
			lore = lore.substring(0, i) + ChatColor.COLOR_CHAR + lore.substring(i, lore.length());
		}
		return lore;
	}
	
	//removes the formatting characters
	public static String decodeHiddenLore(String lore){
		while(lore.contains(ChatColor.COLOR_CHAR + "")){
			lore =lore.substring(0, lore.indexOf(ChatColor.COLOR_CHAR)) + lore.substring(lore.indexOf(ChatColor.COLOR_CHAR)+1, lore.length());
		}
		return lore;
	}
	
	public static <T extends Enum<?>> String getEnumList(Class<T> enumType){
		return getEnumList(enumType, " | ");
	}

	public static <T extends Enum<?>> String getEnumList(Class<T> enumType, String delimiter){
		String list = "";
		boolean put =false;
		for(Enum<?> e : enumType.getEnumConstants()){
			list = list + e.toString() + delimiter;
			put = true;
		}
		if(put) list = list.substring(0, list.length() - delimiter.length());
		return list;
	}
	
	public static double addAngle(double current, double angleChange) {
		current += angleChange;
		current = fixAngle(current, angleChange);
		return current;
	}

	public static double subAngle(double current, double angleChange){
		current -= angleChange;
		current = fixAngle(current, angleChange);
		return current;
	}

	//clamps angle to 0 if it exceeds 2PI rad (360 degrees), is closer to 0 than angleChange value, or is closer to 2PI rad than 2PI rad - angleChange value.
	private static double fixAngle(double angle, double angleChange){
		if(angle > FULLCIRCLE){
			return angle = 0;
		}
		if(angle > 0 && angle < angleChange){
			if(angle < angleChange/2){
				return angle = 0;
			}
		}
		if(angle > FULLCIRCLE-angle){
			if(angle > FULLCIRCLE - (angleChange/2)){
				return angle = 0;
			}
		}
		return angle;
	}
	
	static boolean toggleFlag(boolean flag){
		if(flag){
			return false;
		}else{
			return true;
		}
	}
}
