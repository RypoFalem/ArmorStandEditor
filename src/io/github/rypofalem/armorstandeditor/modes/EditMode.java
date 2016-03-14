package io.github.rypofalem.armorstandeditor.modes;

public enum EditMode {
	NONE("None"), INVISIBLE("Invisible"), SHOWARMS("ShowArms"), GRAVITY("Gravity"), BASEPLATE("BasePlate"), SIZE("Size"), COPY("Copy"), PASTE("Paste"),
	HEAD("Head"), BODY("Body"), LEFTARM("LeftArm"), RIGHTARM("RightArm"), LEFTLEG("LeftLeg"), RIGHTLEG("RightLeg"),
	PLACEMENT("Placement"), DISABLESLOTS("DisableSlots"), ROTATE("Rotate"), TARGET("Target"), EQUIPMENT("Equipment");
	
	private String name;
	
	EditMode(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
