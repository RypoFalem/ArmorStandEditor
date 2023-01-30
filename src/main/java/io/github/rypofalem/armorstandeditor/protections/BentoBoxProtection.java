package io.github.rypofalem.armorstandeditor.protections;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.bentobox.managers.AddonsManager;
import world.bentobox.bentobox.managers.IslandsManager;

import java.util.Optional;

public class BentoBoxProtection implements Protection  {

    private final boolean bentoEnabled;


    public BentoBoxProtection() {
        bentoEnabled = Bukkit.getPluginManager().isPluginEnabled("BentoBox");
    }

    @Override
    public boolean checkPermission(Block block, Player player) {
        BentoBox myBento = BentoBox.getInstance();

        if(!bentoEnabled || player.isOp() ||
                player.hasPermission("asedit.ignoreProtection.bentobox") ||
                player.hasPermission("bentobox.admin") ||
                myBento == null ) return true;

        IslandsManager islandsManager = myBento.getIslandsManager();
        AddonsManager addonsManager = myBento.getAddonsManager();

        //Check first if BSkyblock is enabled or if the Player is Owner of that Island
        if(!addonsManager.getAddonByName("BSkyblock").isPresent()) return true;

        //Get the Location of the ArmorStand
        Optional<Island> islandOptional = islandsManager.getIslandAt(block.getLocation());

        //If there are no Islands Present
        if(!islandOptional.isPresent()) return false;

        //Get the Island from the Island Optional
        Island theIsland = islandOptional.get();

        return theIsland.isAllowed(User.getInstance(player), Flags.BREAK_BLOCKS);
    }
}
