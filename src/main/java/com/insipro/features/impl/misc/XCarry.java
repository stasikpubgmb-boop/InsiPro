package com.insipro.features.impl.misc;

import com.insipro.events.packet.PacketEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry", ModuleCategory.MISC);
    }
    @EventHandler
    public void onS(PacketEvent e){
        if (e.getPacket() instanceof CloseHandledScreenC2SPacket zalupa2) {
            if (zalupa2.getSyncId() == 0) {
                e.cancel();
            }
        }
    }
}
