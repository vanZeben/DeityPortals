package com.imdeity.portals.listener;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import com.imdeity.deityapi.Deity;
import com.imdeity.portals.events.PlayerPortalUseEvent;
import com.imdeity.portals.objects.Portal;

public class PortalListener implements Listener {

    private HashMap<String, Date> portalDelay = new HashMap<String, Date>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if ((player != null)) {
                if (this.portalDelay.containsKey(player.getName())) {
                    if (new Date().getTime() - this.portalDelay.get(player.getName()).getTime() >= 2000) {
                        Portal portal = Portal.getPortal(event.getLocation());
                        if (portal != null) {
                            PlayerPortalUseEvent newEvent = new PlayerPortalUseEvent(player, portal);
                            Deity.server.getServer().getPluginManager().callEvent(newEvent);
                            if (!newEvent.isCancelled()) {
                                if (newEvent.getPortal().isFromConsole) {
                                    Deity.server.getServer().dispatchCommand(Deity.server.getServer().getConsoleSender(), newEvent.getPortal().command);
                                } else {
                                    newEvent.getPlayer().performCommand(newEvent.getPortal().command);
                                }
                            }
                        } else {
                            Deity.chat.sendPlayerMessage(player, "DeityPortal", "This portal doesn't lead anywhere");
                        }
                        this.portalDelay.remove(player.getName());
                    }
                } else {
                    this.portalDelay.put(player.getName(), new Date());
                }
            }
        }
    }
}
