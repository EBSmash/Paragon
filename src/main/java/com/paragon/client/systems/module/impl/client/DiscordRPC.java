package com.paragon.client.systems.module.impl.client;

import com.paragon.Paragon;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.ModuleCategory;

public class DiscordRPC extends Module {

    public DiscordRPC() {
        super("DiscordRPC", ModuleCategory.CLIENT, "Changes your Discord presence to reflect the client's current state");
    }

    @Override
    public void onEnable() {
        Paragon.INSTANCE.getPresenceManager().startRPC();
    }

    @Override
    public void onDisable() {
        Paragon.INSTANCE.getPresenceManager().stopRPC();
    }

}