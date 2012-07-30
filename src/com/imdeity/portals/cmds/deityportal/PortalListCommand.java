package com.imdeity.portals.cmds.deityportal;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.portals.DeityPortalMain;
import com.imdeity.portals.objects.Portal;
import com.imdeity.portals.objects.PortalManager;

public class PortalListCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        ArrayList<String> output = new ArrayList<String>();
        output.add("+-------[Loaded Portals]-------+");
        for (Portal p : PortalManager.getPortals()) {
            output.add(p.toConsole());
        }
        for (String s : output) {
            DeityPortalMain.plugin.chat.out(s);
        }
        return true;
    }
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        ArrayList<String> output = new ArrayList<String>();
        output.add("&3+-------&8[&bLoaded Portals&8]&3-------+");
        for (Portal p : PortalManager.getPortals()) {
            output.add(p.toPlayer());
        }
        for (String s : output) {
            DeityPortalMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
        }
        return true;
    }
}
