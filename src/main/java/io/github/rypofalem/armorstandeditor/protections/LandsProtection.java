package io.github.rypofalem.armorstandeditor.protections;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LandsProtection implements Protection {
    private final boolean landsEnabled;
    private @NotNull LandsIntegration lands;

    public LandsProtection() {
        landsEnabled = Bukkit.getPluginManager().isPluginEnabled("Lands");

        if (landsEnabled)
            lands = LandsIntegration.of(ArmorStandEditorPlugin.instance());
        else
            return;
    }

    @Override
    public boolean checkPermission(Block block, Player player) {
        if (!landsEnabled || player.hasPermission("asedit.ignoreProtection.lands")) return true;

        //Get the land of the current block
        Land land = lands.getLandByChunk(block.getWorld(), block.getX(), block.getZ());

        //Get the Area that the block is in
        Area area = land.getArea(block.getLocation());

        if(area != null)
            //Return if Trusted in the area or not either based on BukkitPlayer
            return area.isTrusted(player.getUniqueId());
        else
            return false;

    }
}
