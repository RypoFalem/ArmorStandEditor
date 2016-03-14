package io.github.rypofalem.armorstandeditor.protection;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.PlotSquared;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;

public class PlotSProtection implements Protection{

	private PlotSquared plotSPlugin;
	private PlotAPI plotAPI;

	public PlotSProtection(PlotSquared plotSPlugin) {
		this.plotSPlugin = plotSPlugin;
		plotAPI = new PlotAPI();
	}

	@Override
	public boolean canEdit(Player player, ArmorStand armorstand) {
		if(plotSPlugin != null && plotAPI != null){
			if(plotAPI.isPlotWorld(armorstand.getLocation().getWorld())){
				Plot plot = plotAPI.getPlot(armorstand.getLocation());
				if(plot == null) return false;
				if( plot.isDenied(player.getUniqueId()) || !((plot.isOwner(player.getUniqueId()) || plot.isAdded(player.getUniqueId()))) ){
					return false;
				}
			}	
		}
		return false;
	}
	
}
