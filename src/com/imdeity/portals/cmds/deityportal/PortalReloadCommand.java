package com.imdeity.portals.cmds.deityportal;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.portals.DeityPortalMain;
import com.imdeity.portals.objects.PortalManager;

public class PortalReloadCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        DeityPortalMain.plugin.chat.out(PortalManager.reload() + " portals reloaded");
        return false;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        DeityPortalMain.plugin.chat.sendPlayerMessage(player, PortalManager.reload() + " portals reloaded");
        return false;
    }
    
}
