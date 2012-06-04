package com.imdeity.portals.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import com.imdeity.deityapi.Deity;
import com.imdeity.portals.events.PlayerPortalUseEvent;
import com.imdeity.portals.objects.Portal;

public class PortalListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPortalEnterEvent(PlayerPortalEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if ((player != null)) {
                Portal portal = Portal.getPortal(player.getLocation());
                if (portal != null) {
                    PlayerPortalUseEvent newEvent = new PlayerPortalUseEvent(player, portal);
                    Deity.server.getServer().getPluginManager().callEvent(newEvent);
                    if (!newEvent.isCancelled()) {
                        if (newEvent.getPortal().isFromConsole) {
                            Deity.server.getServer().dispatchCommand(Deity.server.getServer().getConsoleSender(), newEvent.getPortal().command.replaceAll("%player%", player.getName()));
                        } else {
                            newEvent.getPlayer().performCommand(newEvent.getPortal().command.replaceAll("%player%", player.getName()));
                        }
                    }
                } else {
                    Deity.chat.sendPlayerMessage(player, "DeityPortal", "This portal doesn't lead anywhere");
                }
            }
            event.setCancelled(true);
        }
    }
}
