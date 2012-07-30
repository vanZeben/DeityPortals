package com.imdeity.portals;

import org.bukkit.command.CommandExecutor;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;
import com.imdeity.portals.cmds.DeityPortalCommandHandler;
import com.imdeity.portals.listener.PortalListener;
import com.imdeity.portals.objects.PortalManager;

public class DeityPortalMain extends DeityPlugin implements CommandExecutor {
    
    public static DeityPortalMain plugin;
    
    @Override
    protected void initCmds() {
       this.registerCommand(new DeityPortalCommandHandler("DeityPortals"));
    }
    
    @Override
    protected void initConfig() {
        // None
    }
    
    @Override
    protected void initDatabase() {
        DeityAPI.getAPI()
                .getDataAPI()
                .getMySQL()
                .write("CREATE TABLE IF NOT EXISTS " + getPortalTableName() + " (" + "`id` INT( 16 ) NOT NULL AUTO_INCREMENT PRIMARY KEY ," + "`command` VARCHAR( 128 ) NOT NULL ," + "`execute_from_console` INT( 1 ) NOT NULL ," + "`world` VARCHAR( 32 ) NOT NULL ," + "`min_x` INT( 16 ) NOT NULL ,"
                        + "`min_y` INT( 16 ) NOT NULL ," + "`min_z` INT( 16 ) NOT NULL ," + "`max_x` INT( 16 ) NOT NULL ," + "`max_y` INT( 16 ) NOT NULL ," + "`max_z` INT( 16 ) NOT NULL ," + "`cost` INT(16) NOT NULL DEFAULT '0'"+") ENGINE = MYISAM ;");
    }
    
    @Override
    protected void initInternalDatamembers() {
        PortalManager.loadPortals();
    }
    
    @Override
    protected void initLanguage() {
        // None
    }
    
    @Override
    protected void initListeners() {
        this.registerListener(new PortalListener());
    }
    
    @Override
    protected void initPlugin() {
        plugin = this;
    }
    
    @Override
    protected void initTasks() {
        // None
    }
    
    public static String getPortalTableName() {
        return DeityAPI.getAPI().getDataAPI().getMySQL().tableName("deity_", "portals");
    }
}
