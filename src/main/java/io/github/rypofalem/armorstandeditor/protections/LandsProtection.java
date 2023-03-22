package io.github.rypofalem.armorstandeditor.protections;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.land.LandWorld;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LandsProtection implements Protection {
    private final boolean landsEnabled;
    private LandsIntegration landsAPI;

    public LandsProtection() {
        landsEnabled = Bukkit.getPluginManager().isPluginEnabled("Lands");

        if (landsEnabled)
            landsAPI = LandsIntegration.of(ArmorStandEditorPlugin.instance());
    }

    @Override
    public boolean checkPermission(Block block, Player player) {
        if (!landsEnabled || player.hasPermission("asedit.ignoreProtection.lands")) return true;

        LandWorld landWorld = landsAPI.getWorld(block.getWorld());

        if (landWorld != null){

            //Get the area that the player is in
            Area landArea = landWorld.getArea(block.getLocation());

            if(landArea != null){
                return landArea.isTrusted(player.getUniqueId());
            } else {
                return false;
            }

        } else {
            return true;
        }
    }
}
