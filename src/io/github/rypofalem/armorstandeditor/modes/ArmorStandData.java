package io.github.rypofalem.armorstandeditor.modes;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class ArmorStandData {
	public EulerAngle headPos, leftArmPos, rightArmPos, bodyPos, leftLegPos, rightLegPos;
	public boolean visible, size, basePlate, gravity, showArms;
	public ItemStack head, body, legs, feetsies, rightHand, leftHand;
	
	ArmorStandData(ArmorStand as){
		this.headPos = as.getHeadPose();
		this.leftArmPos = as.getLeftArmPose();
		this.rightArmPos = as.getRightArmPose();
		this.bodyPos = as.getBodyPose();
		this.leftLegPos = as.getLeftLegPose();
		this.rightLegPos = as.getRightLegPose();
		this.size = as.isSmall();
		this.basePlate = as.hasBasePlate();
		this.gravity = as.hasGravity();
		this.showArms = as.hasArms();
		this.visible = as.isVisible();
		this.head = as.getHelmet();
		this.body = as.getChestplate();
		this.legs = as.getLeggings();
		this.feetsies = as.getBoots();
		this.rightHand = as.getItemInHand();
		this.leftHand = as.getEquipment().getItemInOffHand();
	}
}
