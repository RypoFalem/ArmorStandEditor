package io.github.rypofalem.armorstandeditor.protections;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
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
        LandPlayer landPlayer = landsAPI.getLandPlayer(playerUUID);

        //Get the world the play is in
        LandWorld landWorld = landsAPI.getWorld(player.getWorld());

        if(landWorld != null) {

            //Prep to do check for ClaimedArea
            Area landAreaOfAS = landsAPI.getArea(block.getLocation());
            Area landAreaOfPlayer = landsAPI.getArea(player.getLocation());

            if (landAreaOfAS != null) { //Block is in a Claimed Area
                if(landAreaOfPlayer == landAreaOfAS) {
                    if (landAreaOfAS.isTrusted(playerUUID) || landAreaOfAS.getOwnerUID() == landPlayer.getUID()) {
                        // Trusted Players and Owners can build
                        return true;
                    } else if (landAreaOfAS.hasRoleFlag(playerUUID, BLOCK_BREAK) ||
                            landAreaOfAS.hasRoleFlag(playerUUID, BLOCK_PLACE) ||
                            landAreaOfAS.hasRoleFlag(playerUUID, INTERACT_CONTAINER) ||
                            landAreaOfAS.hasRoleFlag(playerUUID, INTERACT_GENERAL)) {
                        //If Player can break Blocks, Place or Interact with in Claimed Area and add items to a container
                        return true;
                    } else{
                        return landAreaOfAS.hasRoleFlag(playerUUID, INTERACT_CONTAINER) || landAreaOfAS.hasRoleFlag(playerUUID, INTERACT_GENERAL);
                    }
                } else{
                    return false;
                }
            } else {
                return true;
            }
        }else{
            return true;
        }
    }
}
