package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.BukkitMain;
import com.plotsquared.bukkit.util.BukkitUtil;

public class PlotSqProtection implements ASEProtection{

	private BukkitMain plotSqPlugin;

	public PlotSqProtection(BukkitMain plotSPlugin) {
		this.plotSqPlugin = plotSPlugin;
		
	}
	
	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(plotSqPlugin == null || !plotSqPlugin.isEnabled()) return true; 
		if(!PS.get().hasPlotArea(player.getWorld().getName())) return true; //if the world isn't a plot world
		com.intellectualcrafters.plot.object.Location location = BukkitUtil.getLocation(armorstand.getLocation());
		Plot plot = Plot.getPlot(location);
		if(plot == null) return false;
		if(plot.isDenied(player.getUniqueId())) return false;
		if(plot.isOwner(player.getUniqueId()) || plot.isAdded(player.getUniqueId())) return true;
		return false;
	}
}