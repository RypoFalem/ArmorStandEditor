package io.github.rypofalem.armorstandeditor.protections;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.angeschossen.lands.api.flags.type.Flags.*;

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

        //Get the players UUID
        UUID playerUUID = player.getUniqueId();

        //Get the world the play is in
        LandWorld landWorld = landsAPI.getWorld(player.getWorld());

        if(landWorld != null) {

            //Prep to do check for ClaimedArea
            Area landArea = landsAPI.getArea(block.getLocation());

            if (landArea != null) {
                if (landArea.isTrusted(playerUUID)) {
                    return true;
                } else if (landArea.hasRoleFlag(playerUUID, BLOCK_BREAK) ||
                        landArea.hasRoleFlag(playerUUID, BLOCK_PLACE) ||
                        landArea.hasRoleFlag(playerUUID, INTERACT_GENERAL)) {
                    return true;
                } else {
                    return false;
                }
            }else {
                return true;
            }
        }else{
            return true;
        }
    }
}
