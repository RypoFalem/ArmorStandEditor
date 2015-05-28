package io.github.rypofalem.armorstandmanipulation.editor;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class PlayerEditor {
	ArmorStandEditorPlugin plugin;
	UUID uuid;
	EditMode mode;
	Axis axis;
	double angleChange = Math.PI/6;
	final double FULLCIRCLE = Math.PI*2;


	public PlayerEditor(UUID uuid, ArmorStandEditorPlugin plugin){
		this.uuid = uuid;
		this.plugin = plugin;
		mode = EditMode.NONE;
		axis = Axis.X;
	}
	
	public void setMode(EditMode editMode){
		this.mode = editMode;
	}
	
	public void setAxis(Axis axis){
		this.axis = axis;
	}

	public void editArmorStand(ArmorStand armorStand) {
		switch(mode){
		case LEFTARM: armorStand.setLeftArmPose(addEulerAngle(armorStand.getLeftArmPose()));
		break;
		case RIGHTARM: armorStand.setRightArmPose(addEulerAngle(armorStand.getRightArmPose()));
		break;
		case BODY: armorStand.setBodyPose(addEulerAngle(armorStand.getBodyPose()));
		break;
		case HEAD: armorStand.setHeadPose(addEulerAngle(armorStand.getHeadPose()));
		break;
		case LEFTLEG: armorStand.setLeftLegPose(addEulerAngle(armorStand.getLeftLegPose()));
		break;
		case RIGHTLEG: armorStand.setRightLegPose(addEulerAngle(armorStand.getRightLegPose()));
		break;
		case SHOWARMS: toggleArms(armorStand);
		break;
		case SIZE: toggleSize(armorStand);
		break;
		case INVISIBLE: toggleVisible(armorStand);
		break;
		case BASEPLATE: togglePlate(armorStand);
		break;
		case GRAVITY: toggleGravity(armorStand);
		default:
		case NONE: sendMessage("You need to select and editing mode from the menu before editing an armorstand!"); break;
		}
	}

	private void toggleGravity(ArmorStand armorStand) {
		armorStand.setGravity(toggleFlag(armorStand.hasGravity()));
	}

	void togglePlate(ArmorStand armorStand) {
		armorStand.setBasePlate(toggleFlag(armorStand.hasBasePlate()));
	}

	void toggleArms(ArmorStand armorStand){
		armorStand.setArms(toggleFlag(armorStand.hasArms()));
	}

	void toggleVisible(ArmorStand armorStand){
		armorStand.setVisible(toggleFlag(armorStand.isVisible()));
	}

	void toggleSize(ArmorStand armorStand){
		armorStand.setSmall(toggleFlag(armorStand.isSmall()));
	}

	boolean toggleFlag(boolean flag){
		if(flag){
			return false;
		}else{
			return true;
		}
	}

	private EulerAngle addEulerAngle(EulerAngle angle) {
		switch(axis){
		case X: angle = angle.setX(addAngle(angle.getX()));
		break;
		case Y: angle = angle.setY(addAngle(angle.getY()));
		break;
		case Z: angle = angle.setZ(addAngle(angle.getZ()));
		break;
		default:
			break;
		}
		return angle;
	}

	//adds the angle and clamps it to 0 if it is between zero and the addition amount and is closer to zero than the angleChange value.
	private double addAngle(double current) {
		current += angleChange;
		if(current > FULLCIRCLE){
			current = 0;
		}
		if(current > 0 && current < angleChange){
			if(current < angleChange/2){
				current = 0;
			}
		}
		if(current > FULLCIRCLE-current){
			if(current > FULLCIRCLE - (angleChange/2)){
				current = 0;
			}
		}
		return current;
	}

	void sendMessage(String message){
		plugin.getServer().getPlayer(uuid).sendMessage(ChatColor.GREEN + message);
	}


}