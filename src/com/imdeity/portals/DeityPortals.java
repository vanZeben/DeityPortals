package com.imdeity.portals;

import java.sql.SQLDataException;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.imdeity.deityapi.Deity;
import com.imdeity.deityapi.records.DatabaseResults;
import com.imdeity.portals.listener.PortalListener;
import com.imdeity.portals.objects.Portal;

public class DeityPortals extends JavaPlugin implements CommandExecutor {

    public static DeityPortals plugin;

    public ArrayList<Portal> portals = new ArrayList<Portal>();
    private PortalListener portalListener = new PortalListener();

    public void createTables() {
        Deity.data.getDB().Write(
                "CREATE TABLE IF NOT EXISTS " + Deity.data.getDB().tableName("deity_", "portals") + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`command` VARCHAR( 128 ) NOT NULL ," + "`perform_from_console` INT( 1 ) NOT NULL ," + "`world` VARCHAR( 32 ) NOT NULL ,"
                        + "`min_x` INT( 16 ) NOT NULL ," + "`min_y` INT( 16 ) NOT NULL ," + "`min_z` INT( 16 ) NOT NULL ," + "`max_x` INT( 16 ) NOT NULL ," + "`max_y` INT( 16 ) NOT NULL ," + "`max_z` INT( 16 ) NOT NULL" + ") ENGINE = MYISAM ;");
    }

    public void loadPortals() {
        String sql = "SELECT * FROM " + Deity.data.getDB().tableName("deity_", "portals") + ";";
        DatabaseResults query = Deity.data.getDB().Read2(sql);
        if ((query != null) && query.hasRows()) {
            for (int i = 0; i < query.rowCount(); i++) {
                try {
                    int id = query.getInteger(i, "id");
                    String command = query.getString(i, "command");
                    boolean performFromConsole = (query.getInteger(i, "perform_from_console") == 0 ? false : true);
                    Location minPoint = new Location(Deity.server.getServer().getWorld(query.getString(i, "world")), query.getInteger(i, "min_x"), query.getInteger(i, "min_y"), query.getInteger(i, "min_z"));
                    Location maxPoint = new Location(Deity.server.getServer().getWorld(query.getString(i, "world")), query.getInteger(i, "max_x"), query.getInteger(i, "max_y"), query.getInteger(i, "max_z"));
                    Portal portal = new Portal(id, command, minPoint, maxPoint, performFromConsole);
                    this.portals.add(portal);
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.portals.clear();
        this.out("Disabled");
    }

    @Override
    public void onEnable() {
        this.createTables();
        this.loadPortals();
        this.getCommand("DeityPortals").setExecutor(this);
        this.getServer().getPluginManager().registerEvents(this.portalListener, this);
        DeityPortals.plugin = this;
        this.out("Enabled");
        this.out(this.portals.size() + " portals loaded");
    }

    public void out(String msg) {
        Deity.chat.out("[DeityPortals]", msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Deity.perm.isLeastSubAdmin(player)) { return false; }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("add")) {
                    Location minPoint = Deity.edit.getMinLocation(player);
                    Location maxPoint = Deity.edit.getMaxLocation(player);
                    String isConsole = args[1];
                    String commandToSend = Deity.utils.string.join(Deity.utils.string.remArgs(args, 2), " ");
                    String sql = "SELECT * FROM " + Deity.data.getDB().tableName("deity_", "portals") + " ORDER BY id DESC LIMIT 1";
                    DatabaseResults query = Deity.data.getDB().Read2(sql);
                    int id = 0;
                    if ((query != null) && query.hasRows()) {
                        try {
                            id = query.getInteger(0, "id");
                        } catch (SQLDataException e) {
                            e.printStackTrace();
                        }
                    }
                    sql = "INSERT INTO " + Deity.data.getDB().tableName("deity_", "portals") + " (command, perform_from_console, world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES (?,?,?,?,?,?,?,?,?);";
                    try {
                        Deity.data.getDB().Write(sql, commandToSend, Integer.parseInt(isConsole), minPoint.getWorld().getName(), (int) minPoint.getX(), (int) minPoint.getY(), (int) minPoint.getZ(), (int) maxPoint.getX(), (int) maxPoint.getY(), (int) maxPoint.getZ());
                        this.portals.add(new Portal(id, commandToSend, minPoint, maxPoint, (Integer.parseInt(isConsole) == 0 ? false : true)));
                        Deity.chat.sendPlayerMessage(player, "Portal Added");
                    } catch (NumberFormatException e) {
                        Deity.data.getDB().Write(sql, commandToSend, 0, minPoint.getWorld().getName(), (int) minPoint.getX(), (int) minPoint.getY(), (int) minPoint.getZ(), (int) maxPoint.getX(), (int) maxPoint.getY(), (int) maxPoint.getZ());
                        this.portals.add(new Portal(id, commandToSend, minPoint, maxPoint, false));
                        Deity.chat.sendPlayerMessage(player, "Portal Added");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    String sql = "DELETE FROM " + Deity.data.getDB().tableName("deity_", "portals") + " WHERE `id` = ?";
                    Deity.data.getDB().Write(sql, args[1]);
                    Deity.chat.sendPlayerMessage(player, "Portal removed");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    this.portals.clear();
                    this.loadPortals();
                    Deity.chat.sendPlayerMessage(player, "Portals reloaded");
                } else if (args[0].equalsIgnoreCase("list")) {
                    ArrayList<String> tmp = new ArrayList<String>();
                    for (Portal p : this.portals) {
                        tmp.add("&3[" + p.id + "]: &b/" + p.command);
                    }
                    for (String s : tmp) {
                        Deity.chat.sendPlayerMessageNoTitleNewLine(player, s);
                    }

                } else if (args[0].equalsIgnoreCase("edit")) {
                    if (args[1].equalsIgnoreCase("cmd")) {
                        for (Portal p : this.portals) {
                            if (p.id == Integer.parseInt(args[2])) {
                                p.command = Deity.utils.string.join(Deity.utils.string.remArgs(args, 3), " ");
                                p.update();
                                Deity.chat.sendPlayerMessage(player, "DeityPortal", "done");
                                return true;
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("console")) {
                        for (Portal p : this.portals) {
                            if (p.id == Integer.parseInt(args[2])) {
                                p.isFromConsole = args[3].equalsIgnoreCase("true");
                                p.update();
                                Deity.chat.sendPlayerMessage(player, "DeityPortal", "done");
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            this.out("Sorry you need to be in game to make a portal");
        }
        return false;
    }
}
