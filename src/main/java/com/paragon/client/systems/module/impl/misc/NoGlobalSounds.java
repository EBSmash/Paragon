package com.paragon.client.systems.module.impl.misc;

import com.paragon.api.event.network.PacketEvent;
import com.paragon.client.systems.module.Category;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.setting.Setting;
import me.wolfsurge.cerauno.listener.Listener;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;

/**
 * @author Wolfsurge
 * @since 14/05/22
 */
public class NoGlobalSounds extends Module {

    // Sounds to cancel
    private final Setting<Boolean> endPortal = new Setting<>("End Portal", true)
            .setDescription("Disables the end portal spawn sound");

    private final Setting<Boolean> witherSpawn = new Setting<>("Wither Spawn", true)
            .setDescription("Disables the wither spawn sound");

    private final Setting<Boolean> dragonDeath = new Setting<>("Dragon Death", true)
            .setDescription("Disables the dragon death sound");

    private final Setting<Boolean> lightning = new Setting<>("Lightning", true)
            .setDescription("Disables the lightning sound");

    public NoGlobalSounds() {
        super("NoGlobalSounds", Category.MISC, "Prevents global sounds from playing");
        this.addSettings(endPortal, witherSpawn, dragonDeath, lightning);
    }

    @Listener
    public void onPacketReceive(PacketEvent.PreReceive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            if (packet.getCategory().equals(SoundCategory.WEATHER) && packet.getSound().equals(SoundEvents.ENTITY_LIGHTNING_THUNDER) && lightning.getValue()) {
                event.cancel();
            }
        }

        if (event.getPacket() instanceof SPacketEffect) {
            SPacketEffect packet = (SPacketEffect) event.getPacket();

            if (packet.getSoundType() == 1038 && endPortal.getValue() || packet.getSoundType() == 1023 && witherSpawn.getValue() || packet.getSoundType() == 1028 && dragonDeath.getValue()) {
                event.cancel();
            }
        }
    }

}