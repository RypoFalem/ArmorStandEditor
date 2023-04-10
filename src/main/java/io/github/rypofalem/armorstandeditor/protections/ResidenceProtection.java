package io.github.rypofalem.armorstandeditor.protections;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import static java.lang.Boolean.TRUE;

public class ResidenceProtection implements Protection {

    private final boolean resEnabled;
    private Plugin residencePlugin;
    ResidenceApi residenceApi;
    private Residence resInstance;
    private static final boolean PERMCHECKER = TRUE;

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
        if(residenceApi == null || residencePlugin == null || !resEnabled || player.hasPermission("asedit.ignoreProtection.residence") ) return true;

        //Get the Claimed Residence by Location
        ClaimedResidence resClaim = resInstance.getResidenceManager().getByLoc(block.getLocation());

        if(resClaim != null ){

            //Get the permissions for that Claimed Residence
            ResidencePermissions resPerms = resClaim.getPermissions();

            //Check: Is player has Admin Flag?
            boolean isPlayerAdmin = resPerms.playerHas(player, Flags.admin, PERMCHECKER);
            if(isPlayerAdmin) return true;

            //Check if Player can Build / Destroy / Place ?
            boolean playerCanBuild = resPerms.playerHas(player, Flags.build, PERMCHECKER);
            boolean playerCanDestroy = resPerms.playerHas(player, Flags.destroy, PERMCHECKER);
            boolean playerCanPlace = resPerms.playerHas(player, Flags.place, PERMCHECKER);
            boolean playerCanInteractWContainer = resPerms.playerHas(player, Flags.container, PERMCHECKER);


            if(playerCanBuild || playerCanDestroy || playerCanPlace){
                return playerCanInteractWContainer;
            } else return false;
        } else{
            return true;
        }
    }
}
