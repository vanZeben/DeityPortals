package com.imdeity.portals.objects;

import org.bukkit.Location;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.portals.DeityPortalMain;

public class Portal {
    
    public int id;
    public String command;
    public Location minPoint;
    public Location maxPoint;
    public boolean executeFromConsole;
    public int cost;
    
    public Portal(int id, String command, Location minPoint, Location maxPoint, boolean executeFromConsole, int cost) {
        this.id = id;
        this.command = command;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.executeFromConsole = executeFromConsole;
        this.cost = cost;
    }
    
    public void save() {
        String sql = "UPDATE " + DeityPortalMain.getPortalTableName() + " SET command = ?, execute_from_console = ?, cost = ? WHERE id = ?;";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.command, (this.executeFromConsole ? 1 : 0), cost, this.id);
    }
    
    public void remove() {
        String sql = "DELETE FROM " + DeityPortalMain.getPortalTableName() + " WHERE `id` = ?;";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, id);
    }
    
    public String toPlayer() {
        return "&8[&3" + id + "&8] &b/" + command + (executeFromConsole ? " &8[&7Console&8]" : " &8[&7Player&8]") + " &8[&7" + minPoint.getBlockX() + "&8,&7" + minPoint.getBlockY() + "&8,&7" + minPoint.getBlockZ() + " " + maxPoint.getBlockX() + "&8,&7" + maxPoint.getBlockY() + "&8,&7"
                + maxPoint.getBlockZ() + "&8] " + (cost != 0 ? " &7[&6" + DeityAPI.getAPI().getEconAPI().getFormattedBalance(cost) + "]" : "");
    }
    
    public String toConsole() {
        return "[" + id + "]: /" + command + (executeFromConsole ? " [Console]" : " [Player]") + " [" + minPoint.getBlockX() + "," + minPoint.getBlockY() + "," + minPoint.getBlockZ() + " " + maxPoint.getBlockX() + "," + maxPoint.getBlockY() + "," + maxPoint.getBlockZ() + "]"
                + (cost != 0 ? " [" + DeityAPI.getAPI().getEconAPI().getFormattedBalance(cost) + "]" : "");
    }
}
