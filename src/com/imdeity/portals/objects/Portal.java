package com.imdeity.portals.objects;

import org.bukkit.Location;

import com.imdeity.deityapi.Deity;
import com.imdeity.portals.DeityPortals;

public class Portal {

    public static Portal getPortal(Location location) {
        String world = location.getWorld().getName();
        int origX = location.getBlockX();
        int origY = location.getBlockY();
        int origZ = location.getBlockZ();
        for (Portal portal : DeityPortals.plugin.portals) {
            if (!portal.minPoint.getWorld().getName().equalsIgnoreCase(world)) {
                continue;
            }
            int minX = portal.minPoint.getBlockX();
            int minY = portal.minPoint.getBlockY();
            int minZ = portal.minPoint.getBlockZ();

            int maxX = portal.maxPoint.getBlockX();
            int maxY = portal.maxPoint.getBlockY();
            int maxZ = portal.maxPoint.getBlockZ();

            if (minX > maxX) {
                int tmp = minX;
                minX = maxX;
                maxX = tmp;
            }
            if (minY > maxY) {
                int tmp = minY;
                minY = maxY;
                maxY = tmp;
            }
            if (minZ > maxZ) {
                int tmp = minZ;
                minZ = maxZ;
                maxZ = tmp;
            }
            if ((minX <= origX && origX <= maxX) && (minY <= origY && origY <= maxY) && (minZ <= origZ && origZ <= maxZ)) { return portal; }
            continue;
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
