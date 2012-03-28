package com.imdeity.portals.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.imdeity.portals.objects.Portal;

public class PlayerPortalUseEvent extends Event implements Cancellable {

	private Player player;
	private Portal portal;
	private boolean isCancelled;
	private static final HandlerList HANDLERS = new HandlerList();

	public PlayerPortalUseEvent(Player player, Portal portal) {
		this.player = player;
		this.portal = portal;
	}

	@Override
	public HandlerList getHandlers() {
		return PlayerPortalUseEvent.HANDLERS;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Portal getPortal() {
		return this.portal;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}
}
