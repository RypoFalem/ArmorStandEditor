package io.github.rypofalem.armorstandeditor.modes;

public enum AdjustmentMode {
	COARSE("Coarse"), FINE("Fine");
	
	private String name;
	
	AdjustmentMode(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
