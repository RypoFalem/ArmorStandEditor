package io.github.rypofalem.armorstandeditor.modes;

public enum Axis {
	X("X"),Y("Y"),Z("Z");
	
	String name;
	
	Axis(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
