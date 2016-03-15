package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.BukkitMain;

public class PlotSqProtection implements Protection{

	private BukkitMain plotSqPlugin;
	private PlotAPI plotAPI;

	@SuppressWarnings("deprecation")
	public PlotSqProtection(BukkitMain plotSPlugin) {
		this.plotSqPlugin = plotSPlugin;
		plotAPI = new PlotAPI();
	}

	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(plotSqPlugin == null || plotAPI ==null || !plotSqPlugin.isEnabled()) return true; 
		if(!PS.get().hasPlotArea(player.getWorld().getName())) return true; //if the world isn't a plot world
		Plot plot = plotAPI.getPlot(armorstand.getLocation());
		if(plot == null) return false;
		if(plot.isDenied(player.getUniqueId())) return false;
		if(plot.isOwner(player.getUniqueId()) || plot.isAdded(player.getUniqueId())) return true;
		return false;
	}
}
