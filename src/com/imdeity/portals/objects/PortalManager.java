package com.imdeity.portals.objects;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.portals.DeityPortalMain;

public class PortalManager {
    private static ArrayList<Portal> portals = new ArrayList<Portal>();
    
    public static void loadPortals() {
        String sql = "SELECT * FROM " + DeityPortalMain.getPortalTableName() + ";";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql);
        if ((query != null) && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int id = query.getInteger(i, "id");
                    String command = query.getString(i, "command");
                    boolean executeFromConsole = query.getInteger(i, "execute_from_console") == 1;
                    Location minPoint = new Location(DeityPortalMain.plugin.getServer().getWorld(query.getString(i, "world")), query.getInteger(i, "min_x"), query.getInteger(i, "min_y"), query.getInteger(i, "min_z"));
                    Location maxPoint = new Location(DeityPortalMain.plugin.getServer().getWorld(query.getString(i, "world")), query.getInteger(i, "max_x"), query.getInteger(i, "max_y"), query.getInteger(i, "max_z"));
                    int cost = query.getInteger(i, "cost");
                    Portal portal = new Portal(id, command, minPoint, maxPoint, executeFromConsole, cost);
                    portals.add(portal);
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
        DeityPortalMain.plugin.chat.out(portals.size() + " portals loaded");
    }
    
    public static Portal addPortal(String commandToSend, boolean isConsole, Location minPoint, Location maxPoint) {
        int minX = minPoint.getBlockX();
        int minY = minPoint.getBlockY();
        int minZ = minPoint.getBlockZ();
        
        int maxX = maxPoint.getBlockX();
        int maxY = maxPoint.getBlockY();
        int maxZ = maxPoint.getBlockZ();
        
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
        
        String sql = "INSERT INTO " + DeityPortalMain.getPortalTableName() + " (command, execute_from_console, world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES (?,?,?,?,?,?,?,?,?);";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, commandToSend, (isConsole ? 1 : 0), minPoint.getWorld().getName(), minX, minY, minZ, maxX, maxY, maxZ);
        return getPortal(new Location(minPoint.getWorld(), minX, minY, minZ), new Location(maxPoint.getWorld(), maxX, maxY, maxZ));
    }
    
    public static Portal getPortal(Location minPoint, Location maxPoint) {
        int minX = minPoint.getBlockX();
        int minY = minPoint.getBlockY();
        int minZ = minPoint.getBlockZ();
        
        int maxX = maxPoint.getBlockX();
        int maxY = maxPoint.getBlockY();
        int maxZ = maxPoint.getBlockZ();
        
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
        
        for (Portal portal : portals) {
            if (portal.minPoint.getBlockX() == minX && portal.minPoint.getBlockY() == minY && portal.minPoint.getBlockZ() == minZ && portal.maxPoint.getBlockX() == maxX && portal.maxPoint.getBlockY() == maxY && portal.maxPoint.getBlockZ() == maxZ) { return portal; }
        }
        String sql = "SELECT * FROM " + DeityPortalMain.getPortalTableName() + " WHERE world = ? AND min_x = ? AND max_x = ? AND min_z = ? AND max_z = ?";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, minPoint.getWorld().getName(), minPoint.getBlockX(), maxPoint.getBlockX(), minPoint.getBlockZ(), maxPoint.getBlockZ());
        if (query != null && query.hasRows()) {
            int id = 0;
            String command = "";
            boolean executeFromConsole = false;
            int cost = 0;
            try {
                id = query.getInteger(0, "id");
                command = query.getString(0, "command");
                executeFromConsole = query.getInteger(0, "execute_from_console") == 1;
                cost = query.getInteger(0, "cost");
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
            Portal portal = new Portal(id, command, minPoint, maxPoint, executeFromConsole, cost);
            portals.add(portal);
            return portal;
        }
        return null;
    }
    
    public static Portal getPortal(Location location) {
        for (Portal portal : portals) {
            if (portal.minPoint.getBlockX() <= location.getBlockX() && portal.minPoint.getBlockY() <= location.getBlockY() && portal.minPoint.getBlockZ() <= location.getBlockZ() && portal.maxPoint.getBlockX() >= location.getBlockX() && portal.maxPoint.getBlockY() >= location.getBlockY()
                    && portal.maxPoint.getBlockZ() >= location.getBlockZ()) { return portal; }
        }
        return null;
    }
    
    public static Portal getPortal(int id) {
        for (Portal portal : portals) {
            if (portal.id == id) { return portal; }
        }
        String sql = "SELECT * FROM " + DeityPortalMain.getPortalTableName() + " WHERE id = ?;";
        DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, id);
        if (query != null && query.hasRows()) {
            String command = "";
            boolean executeFromConsole = false;
            Location minPoint = null;
            Location maxPoint = null;
            int cost = 0;
            try {
                id = query.getInteger(0, "id");
                command = query.getString(0, "command");
                executeFromConsole = query.getInteger(0, "execute_from_console") == 1;
                minPoint = new Location(DeityPortalMain.plugin.getServer().getWorld(query.getString(0, "world")), query.getInteger(0, "min_x"), query.getInteger(0, "min_y"), query.getInteger(0, "min_z"));
                maxPoint = new Location(DeityPortalMain.plugin.getServer().getWorld(query.getString(0, "world")), query.getInteger(0, "max_x"), query.getInteger(0, "max_y"), query.getInteger(0, "max_z"));
                cost = query.getInteger(0, "cost");
            } catch (SQLDataException e) {
                e.printStackTrace();
            }
            Portal portal = new Portal(id, command, minPoint, maxPoint, executeFromConsole, cost);
            portals.add(portal);
            return portal;
        }
        return null;
    }
    
    public static int reload() {
        portals.clear();
        loadPortals();
        return portals.size();
    }
    
    public static List<Portal> getPortals() {
        return portals;
    }
}
