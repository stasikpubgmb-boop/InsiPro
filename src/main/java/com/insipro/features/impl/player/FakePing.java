package com.insipro.features.impl.player;

import com.insipro.events.packet.PacketEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.packet.network.Network;
import com.insipro.utils.math.time.TimerUtil;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;

public class FakePing extends Module {
    public FakePing() {
        super("FakePing", ModuleCategory.PLAYER);
        setup(slid);
    }
    SliderSettings slid = new SliderSettings("Значение пинга","Устанавливает задержку на сервер").setValue(1000).range(0F, 10000);
    long id = -1;
    TimerUtil time = new TimerUtil();

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (!event.isSend()) {
            if (event.getPacket() instanceof KeepAliveS2CPacket p) {
                id = p.getId();
                time.resetCounter();
                event.cancel();
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (time.hasTimeElapsed((long) slid.getValue()) && id != -1) {
            mc.player.networkHandler.getConnection().send(new KeepAliveC2SPacket(id));
            id = -1;
        }
    }
}
