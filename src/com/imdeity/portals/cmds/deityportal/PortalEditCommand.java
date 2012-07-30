package com.imdeity.portals.cmds.deityportal;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.portals.DeityPortalMain;
import com.imdeity.portals.objects.Portal;
import com.imdeity.portals.objects.PortalManager;

public class PortalEditCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length <= 2) { return false; }
        int portalId = 0;
        try {
            portalId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (portalId == 0) { return false; }
        Portal portal = PortalManager.getPortal(portalId);
        if (portal == null) {
            DeityPortalMain.plugin.chat.outWarn("Portal " + portalId + " does not exist");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("command")) {
            portal.command = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(DeityAPI.getAPI().getUtilAPI().getStringUtils().remArgs(args, 2), " ");
            portal.save();
            DeityPortalMain.plugin.chat.out("Updated portal " + portalId + ". Changed the command to: &a/" + portal.command);
        } else if (args[0].equalsIgnoreCase("source")) {
            portal.executeFromConsole = args[2].equalsIgnoreCase("console");
            portal.save();
            DeityPortalMain.plugin.chat.out("Updated portal " + portalId + ". Changed the source to &a" + (portal.executeFromConsole ? " Console" : " Player"));
        } else if (args[0].equalsIgnoreCase("cost")) {
            try {
                portal.cost = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                DeityPortalMain.plugin.chat.outWarn("That cost was invalid");
                return true;
            }
            portal.save();
            DeityPortalMain.plugin.chat.out("Updated portal " + portalId + ". Changed the cost to &a" + DeityAPI.getAPI().getEconAPI().getFormattedBalance(portal.cost));
        }
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length <= 2) { return false; }
        int portalId = 0;
        try {
            portalId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (portalId == 0) { return false; }
        Portal portal = PortalManager.getPortal(portalId);
        if (portal == null) {
            DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Portal " + portalId + " does not exist");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("command")) {
            portal.command = DeityAPI.getAPI().getUtilAPI().getStringUtils().join(DeityAPI.getAPI().getUtilAPI().getStringUtils().remArgs(args, 2), " ");
            portal.save();
            DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Updated portal " + portalId + ". Changed the command to: &a/" + portal.command);
        } else if (args[0].equalsIgnoreCase("source")) {
            portal.executeFromConsole = args[2].equalsIgnoreCase("console");
            portal.save();
            DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Updated portal " + portalId + ". Changed the source to &a" + (portal.executeFromConsole ? " Console" : " Player"));
        } else if (args[0].equalsIgnoreCase("cost")) {
            try {
                portal.cost = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                DeityPortalMain.plugin.chat.sendPlayerMessage(player, "That cost was invalid");
                return true;
            }
            portal.save();
            DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Updated portal " + portalId + ". Changed the cost to &a" + DeityAPI.getAPI().getEconAPI().getFormattedBalance(portal.cost));
        }
        return true;
    }
    
}
