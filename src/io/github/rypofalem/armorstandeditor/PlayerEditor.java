package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.Menu;
import io.github.rypofalem.armorstandeditor.modes.AdjustmentMode;
import io.github.rypofalem.armorstandeditor.modes.ArmorStandData;
import io.github.rypofalem.armorstandeditor.modes.Axis;
import io.github.rypofalem.armorstandeditor.modes.CopySlots;
import io.github.rypofalem.armorstandeditor.modes.EditMode;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

public class PlayerEditor {
	public ArmorStandEditorPlugin plugin;
	private UUID uuid;
	EditMode eMode;
	AdjustmentMode adjMode;
	CopySlots copySlots;
	Axis axis;
	final double FULLCIRCLE = Math.PI*2;
	double coarseAdj;
	double fineAdj;
	double angleChange;
	Menu chestMenu;
	boolean cancelMenuOpen = false;


	public PlayerEditor(UUID uuid, ArmorStandEditorPlugin plugin){
		this.uuid =uuid;
		this.plugin = plugin;
		eMode = EditMode.NONE;
		adjMode = AdjustmentMode.COARSE;
		axis = Axis.X;
		copySlots = new CopySlots();
		coarseAdj = FULLCIRCLE / plugin.coarseRot;
		fineAdj = FULLCIRCLE / plugin.fineRot;
		angleChange = coarseAdj;
		chestMenu = new Menu(this);
	}

	public void setMode(EditMode editMode){
		this.eMode = editMode;
		sendMessage("Setting mode to " + editMode.toString());
	}

	public void setAxis(Axis axis){
		this.axis = axis;
		sendMessage("Setting axis to " + axis.toString());
	}

	public void setAdjMode(AdjustmentMode adjMode){
		this.adjMode = adjMode;
		if(adjMode == AdjustmentMode.COARSE){
			angleChange = coarseAdj;
		}else{
			angleChange = fineAdj;
		}
		sendMessage("Set adjustment to " + adjMode.toString());
	}
	
	public void setCopySlot(byte slot){
		copySlots.changeSlots(slot);
		sendMessage("Set copy slot to " + (slot + 1));
	}

	public void editArmorStand(ArmorStand armorStand) {
		switch(eMode){
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
		break;
		case COPY: copy(armorStand);
		break;
		case PASTE: paste(armorStand);
		break;
		case NONE: sendMessage("You need to select and editing mode from the menu before editing an armorstand!"); break;
		}
	}

	private void copy(ArmorStand armorStand) {
		copySlots.copyDataToSlot(armorStand);
		sendMessage("ArmorStand state copied to slot " + (copySlots.currentSlot + 1) + ".");
		setMode(EditMode.PASTE);
		sendMessage("Setting mode to paste!");
	}

	private void paste(ArmorStand armorStand){
		ArmorStandData data = copySlots.getDataToPaste();
		if(data == null ) return;
		armorStand.setHeadPose(data.headPos);
		armorStand.setBodyPose(data.bodyPos);
		armorStand.setLeftArmPose(data.leftArmPos);
		armorStand.setRightArmPose(data.rightArmPos);
		armorStand.setLeftLegPose(data.leftLegPos);
		armorStand.setRightLegPose(data.rightLegPos);
		armorStand.setSmall(data.size);
		armorStand.setGravity(data.gravity);
		armorStand.setBasePlate(data.basePlate);
		armorStand.setArms(data.showArms);
		armorStand.setVisible(data.visible);
	}

	public void reverseEditArmorStand(ArmorStand armorStand){
		switch(eMode){
		case LEFTARM: armorStand.setLeftArmPose(subEulerAngle(armorStand.getLeftArmPose()));
		break;
		case RIGHTARM: armorStand.setRightArmPose(subEulerAngle(armorStand.getRightArmPose()));
		break;
		case BODY: armorStand.setBodyPose(subEulerAngle(armorStand.getBodyPose()));
		break;
		case HEAD: armorStand.setHeadPose(subEulerAngle(armorStand.getHeadPose()));
		break;
		case LEFTLEG: armorStand.setLeftLegPose(subEulerAngle(armorStand.getLeftLegPose()));
		break;
		case RIGHTLEG: armorStand.setRightLegPose(subEulerAngle(armorStand.getRightLegPose()));
		break;
		default: break;	
		}
	}

	private void toggleGravity(ArmorStand armorStand) {
		armorStand.setGravity(toggleFlag(armorStand.hasGravity()));
		String state = armorStand.hasGravity() ? "on" : "off";
		sendMessage("Gravity turned " + state + ".");
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
	
	//lol
	void cycleAxis(int i) {
		int index = axis.ordinal();
		index += i;
		index = index % Axis.values().length;
		while(index < 0){
			index += Axis.values().length;
		}
		setAxis(Axis.values()[index]);
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

	private EulerAngle subEulerAngle(EulerAngle angle) {
		switch(axis){
		case X: angle = angle.setX(subAngle(angle.getX()));
		break;
		case Y: angle = angle.setY(subAngle(angle.getY()));
		break;
		case Z: angle = angle.setZ(subAngle(angle.getZ()));
		break;
		default:
			break;
		}
		return angle;
	}

	private double addAngle(double current) {
		current += angleChange;
		current = fixAngle(current);
		return current;
	}

	private double subAngle(double current){
		current -= angleChange;
		current = fixAngle(current);
		return current;
	}

	//clamps angle to 0 if it exceeds 2PI rad (360 degrees), is closer to 0 than angleChange value, or is closer to 2PI rad than 2PI rad - angleChange value.
	private double fixAngle(double angle){
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

	void sendMessage(String message){
		plugin.getServer().getPlayer(getUUID()).sendMessage(ChatColor.GREEN + message);
	}
	
	public PlayerEditorManager getManager(){
		return plugin.editor;
	}

	public void openMenu() {
		plugin.getServer().getScheduler().runTaskLater(plugin, new OpenMenuTask(), 1);
	}

	public void cancelOpenMenu() {
		cancelMenuOpen = true;
		plugin.getServer().getScheduler().runTaskLater(plugin, new MenuUncancelTask(), 1);
	}
	
	public UUID getUUID() {
		return uuid;
	}

	class OpenMenuTask implements Runnable{
		
		@Override
		public void run() {
			if(!cancelMenuOpen){
				chestMenu.openMenu();
			}
		}
	}
	
	class MenuUncancelTask implements Runnable{

		@Override
		public void run() {
			cancelMenuOpen = false;
		}
		
	}
}