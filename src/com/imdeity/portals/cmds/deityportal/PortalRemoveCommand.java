package com.imdeity.portals.cmds.deityportal;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.portals.DeityPortalMain;
import com.imdeity.portals.objects.Portal;
import com.imdeity.portals.objects.PortalManager;

public class PortalRemoveCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length < 1) { return false; }
        int portalId = 0;
        try {
            portalId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (portalId == 0) { return false; }
        Portal portal = PortalManager.getPortal(portalId);
        if (portal == null) {
            DeityPortalMain.plugin.chat.outWarn("Portal " + portalId + " does not exist");
            return true;
        }
        portal.remove();
        DeityPortalMain.plugin.chat.out("Portal " + portalId + " was removed");
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) { return false; }
        int portalId = 0;
        try {
            portalId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (portalId == 0) { return false; }
        Portal portal = PortalManager.getPortal(portalId);
        if (portal == null) {
            DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Portal " + portalId + " does &cnot &fexist");
            return true;
        }
        portal.remove();
        DeityPortalMain.plugin.chat.sendPlayerMessage(player, "Portal " + portalId + " was removed");
        return false;
    }
    
}
