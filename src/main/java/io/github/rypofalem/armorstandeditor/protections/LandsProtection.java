package io.github.rypofalem.armorstandeditor.protections;

import io.github.rypofalem.armorstandeditor.ArmorStandEditorPlugin;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.role.Role;
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

                    //Get Visitor Role for the Area of the AS
                    Role visitorRole = landAreaOfAS.getVisitorRole();

                    //If Player is a Visitor - Dont allow Edits
                    if(landAreaOfAS.getRole(playerUUID) == visitorRole) return false;

                    // If Player is Trusted OR Player is Owner of the Area/Claim, Allow Edits
                    if (landAreaOfAS.isTrusted(playerUUID) || landAreaOfAS.getOwnerUID() == landPlayer.getUID())return true;

                    // If in the Claim a Player can:
                        // break Blocks,
                        // Place
                        // Interact with in Claimed Area
                        // add items to a container
                    // Allow Edits
                    else if (landAreaOfAS.hasRoleFlag(playerUUID, BLOCK_BREAK) ||
                            landAreaOfAS.hasRoleFlag(playerUUID, BLOCK_PLACE) ||
                            landAreaOfAS.hasRoleFlag(playerUUID, INTERACT_CONTAINER) ||
                            landAreaOfAS.hasRoleFlag(playerUUID, INTERACT_GENERAL)) {

                        return true;
                    } else{ // Any other case, dont allow edits
                        return false;
                    }
                } else return false; //If the land areas are different
            } else { //If the AS is in the Wilderness
                return true;
            }
        }else { //if the ArmorStand is in a world
            return true;
        }
    }
}
