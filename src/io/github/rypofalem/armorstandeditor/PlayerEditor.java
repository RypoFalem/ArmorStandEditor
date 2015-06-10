package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.Menu;
import io.github.rypofalem.armorstandeditor.modes.AdjustmentMode;
import io.github.rypofalem.armorstandeditor.modes.ArmorStandData;
import io.github.rypofalem.armorstandeditor.modes.Axis;
import io.github.rypofalem.armorstandeditor.modes.CopySlots;
import io.github.rypofalem.armorstandeditor.modes.EditMode;

import java.util.UUID;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
	double angleChange;
	double movChange;
	Menu chestMenu;
	boolean cancelMenuOpen = false;
	int uncancelTaskID =0;

	public PlayerEditor(UUID uuid, ArmorStandEditorPlugin plugin){
		this.uuid =uuid;
		this.plugin = plugin;
		eMode = EditMode.NONE;
		adjMode = AdjustmentMode.COARSE;
		axis = Axis.X;
		copySlots = new CopySlots();
		angleChange = getManager().coarseAdj;
		movChange = getManager().coarseMov;
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
			angleChange = getManager().coarseAdj;
			movChange = getManager().coarseMov;
		}else{
			angleChange = getManager().fineAdj;
			movChange = getManager().fineMov;
		}
		sendMessage("Set adjustment to " + adjMode.toString());
	}

	public void setCopySlot(byte slot){
		copySlots.changeSlots(slot);
		sendMessage("Set copy slot to " + (slot + 1));
	}

	public void editArmorStand(ArmorStand armorStand) {
		if(canBuild(armorStand.getLocation())){
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
			case NONE: sendMessage("Click with the edit tool away from the armorstand to select an editing mode first!"); break;
			}
		}else{
			cannotBuildMessage();
		}
	}

	public void reverseEditArmorStand(ArmorStand armorStand){
		if(canBuild(armorStand.getLocation())){
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

	private void copy(ArmorStand armorStand) {
		if(canBuild(armorStand.getLocation())){
			copySlots.copyDataToSlot(armorStand);
			sendMessage("ArmorStand state copied to slot " + (copySlots.currentSlot + 1) + ".");
			setMode(EditMode.PASTE);
		}else{
			cannotBuildMessage();
		}
	}

	private void paste(ArmorStand armorStand){
		if(canBuild(armorStand.getLocation())){
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
				armorStand.setItemInHand(data.hand);
			}
			sendMessage("ArmorStand state pasted from slot " + (copySlots.currentSlot + 1) + ".");
		}else{
			cannotBuildMessage();
		}
	}

	private void toggleGravity(ArmorStand armorStand) {
		armorStand.setGravity(Util.toggleFlag(armorStand.hasGravity()));
		String state = armorStand.hasGravity() ? "on" : "off";
		sendMessage("Gravity turned " + state + ".");
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
		case X: angle = angle.setX(Util.addAngle(angle.getX(), angleChange));
		break;
		case Y: angle = angle.setY(Util.addAngle(angle.getY(), angleChange));
		break;
		case Z: angle = angle.setZ(Util.addAngle(angle.getZ(), angleChange));
		break;
		default:
			break;
		}
		return angle;
	}

	private EulerAngle subEulerAngle(EulerAngle angle) {
		switch(axis){
		case X: angle = angle.setX(Util.subAngle(angle.getX(), angleChange));
		break;
		case Y: angle = angle.setY(Util.subAngle(angle.getY(), angleChange));
		break;
		case Z: angle = angle.setZ(Util.subAngle(angle.getZ(), angleChange));
		break;
		default:
			break;
		}
		return angle;
	}

	void sendMessage(String message){
		plugin.getServer().getPlayer(getUUID()).sendMessage(ChatColor.GREEN + message);
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
	
	private boolean canBuild(Location location) {
		if(plugin.isPluginEnabled("WorldGuard")){
			if(!plugin.getWGPlugin().canBuild(getPlayer(), location)){
				return false;
			}
		}
		if(plugin.isPluginEnabled("GriefPrevention")){
			Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
			if(claim != null 
					&& claim.allowBuild(getPlayer(), Material.DIAMOND_BLOCK ) != null){ //claim.allowBuild returns null if player has permission to build
				return false;
			}
		}
		return true;
	}

	private void cannotBuildMessage(){
		getPlayer().sendMessage(ChatColor.RED + "Sorry, you cannot edit armor stands here!");
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