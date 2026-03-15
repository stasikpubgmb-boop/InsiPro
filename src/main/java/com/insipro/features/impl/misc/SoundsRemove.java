package com.insipro.features.impl.misc;

import com.insipro.events.packet.PacketEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.Instance;
import com.insipro.utils.client.managers.event.EventHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;

public class SoundsRemove extends Module {
    public static SoundsRemove getInstance() {
        return Instance.get(SoundsRemove.class);
    }

    public SoundsRemove() {
        super("SoundsRemove", "SoundsRemove", ModuleCategory.MISC);
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (!isState() || e.isSend()) return;
        if (!(e.getPacket() instanceof PlaySoundS2CPacket packet)) return;

        String soundId = packet.getSound().getIdAsString();
        if (soundId != null && soundId.contains("entity.wither")) {
            e.setCancelled(true);
        }
    }
}
