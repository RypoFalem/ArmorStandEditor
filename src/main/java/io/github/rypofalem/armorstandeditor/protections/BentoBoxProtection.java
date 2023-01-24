package io.github.rypofalem.armorstandeditor.protections;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BentoBoxProtection implements Protection  {

    private final boolean bentoEnabled;

    public BentoBoxProtection() {
        bentoEnabled = Bukkit.getPluginManager().isPluginEnabled("BentoBox");
        if(!bentoEnabled) return;
    }

    @Override
    public boolean checkPermission(Block block, Player player) {
        if(!bentoEnabled || player.isOp() || player.hasPermission("asedit.ignoreProtection.bentobox")) return true;

        //TODO: Add API Items here

        return true;

    }
}
