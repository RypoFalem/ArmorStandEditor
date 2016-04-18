package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.EquipmentMenu;
import io.github.rypofalem.armorstandeditor.menu.Menu;
import io.github.rypofalem.armorstandeditor.modes.AdjustmentMode;
import io.github.rypofalem.armorstandeditor.modes.ArmorStandData;
import io.github.rypofalem.armorstandeditor.modes.Axis;
import io.github.rypofalem.armorstandeditor.modes.CopySlots;
import io.github.rypofalem.armorstandeditor.modes.EditMode;
import io.github.rypofalem.armorstandeditor.protection.ASEProtection;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

public class PlayerEditor {
	public ArmorStandEditorPlugin plugin;
	private UUID uuid;
	EditMode eMode;
	AdjustmentMode adjMode;
	CopySlots copySlots;
	Axis axis;
	double eulerAngleChange;
	double degreeAngleChange;
	double movChange;
	Menu chestMenu;
	boolean cancelMenuOpen = false;
	int uncancelTaskID =0;
	ArmorStand target;
	EquipmentMenu equipMenu;

	public PlayerEditor(UUID uuid, ArmorStandEditorPlugin plugin){
		this.uuid =uuid;
		this.plugin = plugin;
		eMode = EditMode.NONE;
		adjMode = AdjustmentMode.COARSE;
		axis = Axis.X;
		copySlots = new CopySlots();
		eulerAngleChange = getManager().coarseAdj;
		degreeAngleChange = eulerAngleChange /Math.PI * 180;
		movChange = getManager().coarseMov;
		chestMenu = new Menu(this);
	}

	public void setMode(EditMode editMode){
		this.eMode = editMode;
		sendMessage("setmode", editMode.toString().toLowerCase());
	}

	public void setAxis(Axis axis){
		this.axis = axis;
		sendMessage("setaxis", axis.toString().toLowerCase());
	}

	public void setAdjMode(AdjustmentMode adjMode){
		this.adjMode = adjMode;
		if(adjMode == AdjustmentMode.COARSE){
			eulerAngleChange = getManager().coarseAdj;
			movChange = getManager().coarseMov;
		}else{
			eulerAngleChange = getManager().fineAdj;
			movChange = getManager().fineMov;
		}
		degreeAngleChange = eulerAngleChange /Math.PI * 180;
		sendMessage("setadj", adjMode.toString().toLowerCase());
	}

	public void setCopySlot(byte slot){
		copySlots.changeSlots(slot);
		sendMessage("setslot" , String.valueOf((slot + 1)));
	}

	public void editArmorStand(ArmorStand armorStand) {
		if(canBuild(armorStand)){
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
			case PLACEMENT: move(armorStand);
			break;
			case ROTATE: rotate(armorStand);
			break;
			case DISABLESLOTS: toggleDisableSlots(armorStand);
			break;
			case EQUIPMENT: openEquipment(armorStand);
			break;
			case TARGET: setTarget(armorStand);
			break;
			case NONE: sendMessage("nomode", null); break;
			}
		}else{
			cannotBuildMessage();
		}
	}

	private void openEquipment(ArmorStand armorStand) {
		equipMenu = new EquipmentMenu(this, armorStand);
		equipMenu.open();
	}

	public void reverseEditArmorStand(ArmorStand armorStand){
		if(canBuild(armorStand)){
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
			case PLACEMENT: reverseMove(armorStand);
			break;
			case ROTATE: reverseRotate(armorStand);
			break;
			default: editArmorStand(armorStand);	
			}
		}else{
			cannotBuildMessage();
		}
	}

	private void move(ArmorStand armorStand) {
		Location loc = armorStand.getLocation();
		switch(axis){
		case X: loc.add(movChange, 0, 0);
		break;
		case Y: loc.add(0, movChange, 0);
		break;
		case Z: loc.add(0, 0, movChange);
		break;
		}
		armorStand.teleport(loc);
	}

	private void reverseMove(ArmorStand armorStand) {
		Location loc = armorStand.getLocation();
		switch(axis){
		case X: loc.subtract(movChange, 0, 0);
		break;
		case Y: loc.subtract(0, movChange, 0);
		break;
		case Z: loc.subtract(0, 0, movChange);
		break;
		}
		armorStand.teleport(loc);
	}

	private void rotate(ArmorStand armorStand){
		Location loc = armorStand.getLocation();
		float yaw = loc.getYaw();
		loc.setYaw((yaw + 180 + (float)degreeAngleChange)%360 - 180);
		armorStand.teleport(loc);
	}

	private void reverseRotate(ArmorStand armorStand){
		Location loc = armorStand.getLocation();
		float yaw = loc.getYaw();
		loc.setYaw((yaw + 180 - (float)degreeAngleChange)%360 - 180);
		armorStand.teleport(loc);
	}

	private void copy(ArmorStand armorStand) {
			copySlots.copyDataToSlot(armorStand);
			sendMessage("copied" , "" + (copySlots.currentSlot + 1));
			setMode(EditMode.PASTE);
	}

	private void paste(ArmorStand armorStand){
		if(canBuild(armorStand)){
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
			if(this.getPlayer().getGameMode() == GameMode.CREATIVE){
				armorStand.setHelmet(data.head);
				armorStand.setChestplate(data.body);
				armorStand.setLeggings(data.legs);
				armorStand.setBoots(data.feetsies);
				armorStand.setItemInHand(data.rightHand);
				armorStand.getEquipment().setItemInOffHand(data.leftHand);
			}
			sendMessage("pasted", ""+ (copySlots.currentSlot + 1));
		}else{
			cannotBuildMessage();
		}
	}

	private void toggleDisableSlots(ArmorStand armorStand) {

	}

	private void toggleGravity(ArmorStand armorStand) {
		armorStand.setGravity(Util.toggleFlag(armorStand.hasGravity()));
		String state = armorStand.hasGravity() ? "on" : "off";
		sendMessage("setgravity", state);
	}

	void togglePlate(ArmorStand armorStand) {
		armorStand.setBasePlate(Util.toggleFlag(armorStand.hasBasePlate()));
	}

	void toggleArms(ArmorStand armorStand){
		armorStand.setArms(Util.toggleFlag(armorStand.hasArms()));
	}

	void toggleVisible(ArmorStand armorStand){
		armorStand.setVisible(Util.toggleFlag(armorStand.isVisible()));
	}

	void toggleSize(ArmorStand armorStand){
		armorStand.setSmall(Util.toggleFlag(armorStand.isSmall()));
	}

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
		case X: angle = angle.setX(Util.addAngle(angle.getX(), eulerAngleChange));
		break;
		case Y: angle = angle.setY(Util.addAngle(angle.getY(), eulerAngleChange));
		break;
		case Z: angle = angle.setZ(Util.addAngle(angle.getZ(), eulerAngleChange));
		break;
		default:
			break;
		}
		return angle;
	}

	private EulerAngle subEulerAngle(EulerAngle angle) {
		switch(axis){
		case X: angle = angle.setX(Util.subAngle(angle.getX(), eulerAngleChange));
		break;
		case Y: angle = angle.setY(Util.subAngle(angle.getY(), eulerAngleChange));
		break;
		case Z: angle = angle.setZ(Util.subAngle(angle.getZ(), eulerAngleChange));
		break;
		default:
			break;
		}
		return angle;
	}

	public void setTarget(ArmorStand armorstand){
		this.target = armorstand;
	}

	void sendMessage(String path, String option){
		String message = plugin.getLang().getMessage(path, "info", option);
		plugin.getServer().getPlayer(getUUID()).sendMessage(message);
	}

	public PlayerEditorManager getManager(){
		return plugin.editorManager;
	}

	public Player getPlayer(){
		return plugin.getServer().getPlayer(getUUID());
	}

	public UUID getUUID() {
		return uuid;
	}

	boolean canBuild(ArmorStand armorstand) {
		for(ASEProtection prot : plugin.getProtections()){
			if(!prot.canEdit(getPlayer(), armorstand)) return false;
		}
		return true;
	}

	private void cannotBuildMessage(){
		getPlayer().sendMessage(plugin.getLang().getMessage("cantedit", "warn"));
	}

	public void openMenu() {
		plugin.getServer().getScheduler().runTaskLater(plugin, new OpenMenuTask(), 1);
	}

	public void cancelOpenMenu() {
		if(cancelMenuOpen){
			plugin.getServer().getScheduler().cancelTask(uncancelTaskID);
		}else{
			cancelMenuOpen = true;
		}
		uncancelTaskID = plugin.getServer().getScheduler().runTaskLater(plugin, new MenuUncancelTask(), 3).getTaskId();
	}

	class OpenMenuTask implements Runnable{

		@Override
		public void run() {
			if(!cancelMenuOpen){
				chestMenu.openMenu();
			}
		}
	}

	class MenuUncancelTask implements Runnable {

		@Override
		public void run() {
			cancelMenuOpen = false;
		}
	}
}