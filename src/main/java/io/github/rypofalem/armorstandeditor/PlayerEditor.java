/*
 * ArmorStandEditor: Bukkit plugin to allow editing armor stand attributes
 * Copyright (C) 2016  RypoFalem
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.rypofalem.armorstandeditor;

import io.github.rypofalem.armorstandeditor.menu.EquipmentMenu;
import io.github.rypofalem.armorstandeditor.menu.Menu;
import io.github.rypofalem.armorstandeditor.modes.AdjustmentMode;
import io.github.rypofalem.armorstandeditor.modes.ArmorStandData;
import io.github.rypofalem.armorstandeditor.modes.Axis;
import io.github.rypofalem.armorstandeditor.modes.CopySlots;
import io.github.rypofalem.armorstandeditor.modes.EditMode;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
	ArmorStand target;
	ArrayList<ArmorStand> targetList = null;
	int targetIndex = 0;
	EquipmentMenu equipMenu;
	long lastCancelled = 0;

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
		armorStand = attemptTarget(armorStand);
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
		case RESET: resetPosition(armorStand);
		break;
		case NONE: sendMessage("nomode", null); break;
		}
	}

	private void resetPosition(ArmorStand armorStand) {
		armorStand.setHeadPose(new EulerAngle(0,0,0));
		armorStand.setBodyPose(new EulerAngle(0,0,0));
		armorStand.setLeftArmPose(new EulerAngle(0,0,0));
		armorStand.setRightArmPose(new EulerAngle(0,0,0));
		armorStand.setLeftLegPose(new EulerAngle(0,0,0));
		armorStand.setRightLegPose(new EulerAngle(0,0,0));
	}

	private void openEquipment(ArmorStand armorStand) {
		equipMenu = new EquipmentMenu(this, armorStand);
		equipMenu.open();
	}

	public void reverseEditArmorStand(ArmorStand armorStand){
		armorStand = attemptTarget(armorStand);
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
	}

	private void toggleDisableSlots(ArmorStand armorStand) {
		//TODO
	}

	private void toggleGravity(ArmorStand armorStand) {
		armorStand.setGravity(!armorStand.hasGravity());
		String state = armorStand.hasGravity() ? "on" : "off";
		sendMessage("setgravity", state);
	}

	void togglePlate(ArmorStand armorStand) {
		armorStand.setBasePlate(!armorStand.hasBasePlate());
	}

	void toggleArms(ArmorStand armorStand){
		armorStand.setArms(!armorStand.hasArms());
	}

	void toggleVisible(ArmorStand armorStand){
		armorStand.setVisible(!armorStand.isVisible());
	}

	void toggleSize(ArmorStand armorStand){
		armorStand.setSmall(!armorStand.isSmall());
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

	public void setTarget(ArrayList<ArmorStand> armorStands){
		if(armorStands == null || armorStands.isEmpty()){
			target = null;
			targetList = null;
			sendMessage("notarget", null);
			return;
		}

		if(targetList == null){
			targetList = armorStands;
			targetIndex = 0;
			sendMessage("target", null);
		} else{
			boolean same = targetList.size() == armorStands.size();
			if(same) for(ArmorStand as : armorStands){
				same = targetList.contains(as);
				if(!same) break;
			}

			if(same){
				targetIndex = ++targetIndex % targetList.size();
			}else{
				targetList = armorStands;
				targetIndex = 0;
				sendMessage("target", null);
			}
		}
		target = targetList.get(targetIndex);
		highlight(target);
	}

	ArmorStand attemptTarget(ArmorStand armorStand){
		if(target == null) return armorStand;
		if(target.getWorld() != getPlayer().getWorld()) return armorStand;
		if(target.getLocation().distanceSquared(getPlayer().getLocation()) > 100) return armorStand;
		armorStand = target;
		highlight(armorStand);
		return armorStand;
	}

	void sendMessage(String path, String format, String option){
		if(plugin.sendToActionBar){
			String rawText = plugin.getLang().getRawMessage(path, format, option);
			String command = String.format("title %s actionbar %s", plugin.getServer().getPlayer(getUUID()).getName(), rawText);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender() ,command);
		} else{
			String message = plugin.getLang().getMessage(path, format, option);
			plugin.getServer().getPlayer(getUUID()).sendMessage(message);
		}
	}

	void sendMessage(String path, String option){
		sendMessage(path, "info", option);
	}

	private void highlight(ArmorStand armorStand){
		armorStand.removePotionEffect(PotionEffectType.GLOWING);
		armorStand.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 15, 1, false, false));
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

	public void openMenu() {
		if(!isMenuCancelled()){
			plugin.getServer().getScheduler().runTaskLater(plugin, new OpenMenuTask(), 1).getTaskId();
		}
	}

	public void cancelOpenMenu() {
		lastCancelled = getManager().getTime();
	}
	
	boolean isMenuCancelled(){
		return getManager().getTime() - lastCancelled < 2;
	}

	private class OpenMenuTask implements Runnable{
		
		@Override
		public void run() {
			if(isMenuCancelled()) return;
			chestMenu.openMenu();
		}
	}
}