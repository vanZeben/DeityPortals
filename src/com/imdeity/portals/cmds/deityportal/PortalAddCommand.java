package com.imdeity.portals.cmds.deityportal;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.portals.DeityPortalMain;
import com.imdeity.portals.objects.Portal;
import com.imdeity.portals.objects.PortalManager;

public class PortalAddCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        DeityPortalMain.plugin.chat.outWarn("You need to be in game to make a portal");
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 2) { return false; }
        String commandToSend = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(DeityAPI.getAPI().getUtilAPI().getStringUtils().remArgs(args, 1), " ");
        boolean isConsole = args[0].equalsIgnoreCase("console");
        Location minPoint = DeityAPI.getAPI().getWorldEditAPI().getMinLocation(player);
        Location maxPoint = DeityAPI.getAPI().getWorldEditAPI().getMaxLocation(player);
        
        Portal portal = PortalManager.addPortal(commandToSend, isConsole, minPoint, maxPoint);
        if (portal != null) {
            DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Portal Added at [" + minPoint.getBlockX() + "," + minPoint.getBlockY() + "," + minPoint.getBlockZ() + "] [" + maxPoint.getBlockX() + "," + maxPoint.getBlockY() + "," + maxPoint.getBlockZ() + "]");
        }
        return true;
    }
}
