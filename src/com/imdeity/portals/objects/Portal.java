package com.imdeity.portals.objects;

import org.bukkit.Location;

import com.imdeity.deityapi.Deity;
import com.imdeity.portals.DeityPortals;

public class Portal {

    public static Portal getPortal(Location location) {
        String world = location.getWorld().getName();
        int origX = (int) location.getX();
        int origY = (int) location.getY();
        int origZ = (int) location.getZ();
        for (Portal portal : DeityPortals.plugin.portals) {
            if (!portal.minPoint.getWorld().getName().equalsIgnoreCase(world)) {
                continue;
            }
            int minX = (int) portal.minPoint.getX();
            int minY = (int) portal.minPoint.getY();
            int minZ = (int) portal.minPoint.getZ();
            if ((minX > origX) || (minY > origY) || (minZ > origZ)) {
                continue;
            }
            int maxX = (int) portal.maxPoint.getX();
            int maxY = (int) portal.maxPoint.getY();
            int maxZ = (int) portal.maxPoint.getZ();
            if ((maxX < origX) || (maxY < origY) || (maxZ < origZ)) {
                continue;
            }
            return portal;
        }
        return null;
    }

    public int id;
    public String command;
    public Location minPoint;
    public Location maxPoint;

    public boolean isFromConsole;

    public Portal(int id, String command, Location minPoint, Location maxPoint, boolean isFromConsole) {
        this.id = id;
        this.command = command;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.isFromConsole = isFromConsole;
    }

    public void update() {
        String sql = "UPDATE " + Deity.data.getDB().tableName("deity_", "portals") + " SET command = ?, perform_from_console = ? WHERE id = ?;";
        Deity.data.getDB().Write(sql, this.command, (this.isFromConsole ? 1 : 0), this.id);
    }
}
