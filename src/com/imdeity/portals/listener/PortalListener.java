package com.imdeity.portals.listener;

import java.util.regex.Matcher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityListener;
import com.imdeity.deityapi.exception.NegativeMoneyException;
import com.imdeity.portals.DeityPortalMain;
import com.imdeity.portals.events.PlayerPortalUseEvent;
import com.imdeity.portals.objects.Portal;
import com.imdeity.portals.objects.PortalManager;

public class PortalListener extends DeityListener {
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPortalEnterEvent(PlayerPortalEvent event) {
        if (event.getCause() == TeleportCause.END_PORTAL) { return; }
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if ((player != null) && (player.hasPermission("deityportal.use"))) {
                Portal portal = PortalManager.getPortal(player.getLocation());
                if (portal == null) {
                    if (DeityPortalMain.plugin.getServer().getAllowNether()) {
                        return;
                    } else {
                        DeityPortalMain.plugin.chat.sendPlayerMessage(player, "This portal doesn't lead anywhere");
                        event.setCancelled(true);
                        return;
                    }
                }
                if (!DeityAPI.getAPI().getEconAPI().canPay(player.getName(), portal.cost)) {
                    DeityPortalMain.plugin.chat.sendPlayerMessage(player, "You cannot afford to use this portal");
                    return;
                }
                PlayerPortalUseEvent newEvent = new PlayerPortalUseEvent(player, portal);
                DeityPortalMain.plugin.getServer().getPluginManager().callEvent(newEvent);
                if (!newEvent.isCancelled()) {
                    if (newEvent.getPortal().executeFromConsole) {
                        try {
                            DeityAPI.getAPI().getEconAPI().send(player.getName(), portal.cost, "Portal Use");
                        } catch (NegativeMoneyException e) {
                        }
                        DeityPortalMain.plugin.getServer().dispatchCommand(DeityPortalMain.plugin.getServer().getConsoleSender(), newEvent.getPortal().command.replaceAll("%player%", Matcher.quoteReplacement(player.getName())));
                        event.setCancelled(true);
                        return;
                    } else {
                        try {
                            DeityAPI.getAPI().getEconAPI().send(player.getName(), portal.cost, "Portal Use");
                        } catch (NegativeMoneyException e) {
                        }
                        newEvent.getPlayer().performCommand(newEvent.getPortal().command.replaceAll("%player%", Matcher.quoteReplacement(player.getName())));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
