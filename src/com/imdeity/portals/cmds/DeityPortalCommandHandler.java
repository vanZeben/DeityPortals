package com.imdeity.portals.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import com.imdeity.portals.cmds.deityportal.PortalAddCommand;
import com.imdeity.portals.cmds.deityportal.PortalEditCommand;
import com.imdeity.portals.cmds.deityportal.PortalListCommand;
import com.imdeity.portals.cmds.deityportal.PortalReloadCommand;
import com.imdeity.portals.cmds.deityportal.PortalRemoveCommand;

public class DeityPortalCommandHandler extends DeityCommandHandler {
    
    public DeityPortalCommandHandler(String pluginName) {
        super(pluginName, "DeityPortal");
    }
    
    @Override
    protected void initRegisteredCommands() {
        String[] editArgs = { "command [portal-id] [new-command]", "source [portal-id] [CONSOLE/PLAYER]", "cost [portal-id] [price]" };
        this.registerCommand("add", "[CONSOLE/PLAYER] [command]", "Creates a portal at the specified Coords", new PortalAddCommand(), "deityportal.add");
        this.registerCommand("remove", "[portal-id]", "Deletes a portal", new PortalRemoveCommand(), "deityportal.remove");
        this.registerCommand("list", "", "Lists all portals", new PortalListCommand(), "deityportal.list");
        this.registerCommand("edit", editArgs, "Edits a portal", new PortalEditCommand(), "deityportal.edit");
        this.registerCommand("reload", "", "Reloads all portals", new PortalReloadCommand(), "deityportal.reload");
    }
}
