package io.github.rypofalem.armorstandeditor.protections;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import static java.lang.Boolean.TRUE;

public class ResidenceProtection implements Protection {

    private final boolean resEnabled;
    private Plugin residencePlugin;
    private ResidenceApi residenceApi;
    private Residence resInstance;
    private boolean permChecker = TRUE;

    public ResidenceProtection() {
        resEnabled = Bukkit.getPluginManager().isPluginEnabled("Residence"); //Check if Plugin is Enabled

        if (!resEnabled) return;
        else residencePlugin = Bukkit.getPluginManager().getPlugin("Residence");

        if (residencePlugin == null) return;
        else {
            resInstance = Residence.getInstance();
            residenceApi = resInstance.getAPI();
        }

    }

    @Override
    public boolean checkPermission(Block block, Player player) {
        if(residencePlugin == null) return true;
        if(!resEnabled) return true;
        if(player.hasPermission("asedit.ignoreProtection.residence")) return true;

        //Get the Blocks Location
        Location loc = block.getLocation();

        //Get the Claimed Residence by Location
        ClaimedResidence resClaim = resInstance.getResidenceManager().getByLoc(loc);

        if(resClaim != null ){

            //Get the permissions for that Claimed Residence
            ResidencePermissions resPerms = resClaim.getPermissions();

            //Check: Is player has Admin Flag?
            boolean isPlayerAdmin = resPerms.playerHas(player, Flags.admin, permChecker);
            if(isPlayerAdmin) return true;

            //Check if Player can Build / Destory / Place ?
            boolean playerCanBuild = resPerms.playerHas(player, Flags.build, permChecker);
            boolean playerCanDestory = resPerms.playerHas(player, Flags.destroy, permChecker);
            boolean playerCanPlace = resPerms.playerHas(player, Flags.place, permChecker);

            if(playerCanBuild || playerCanDestory || playerCanPlace) return true;
            else return false;
        } else{
            return true;
        }
    }
}
