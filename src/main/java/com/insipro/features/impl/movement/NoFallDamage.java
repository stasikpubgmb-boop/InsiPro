package com.insipro.features.impl.movement;

import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.events.packet.PacketEvent;
import com.insipro.utils.math.projection.Projection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoFallDamage extends Module {

    SelectSetting mode = new SelectSetting("Режим", "Выберите тип")
            .value("SpookyTime")
            .selected("SpookyTime");

    public NoFallDamage() {
        super("NoFallDamage", "NoFallDamage", ModuleCategory.MOVEMENT);
        setup(mode);
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.fallDistance > 0 && Projection.getDistanceToGround() >4) {
            mc.player.setVelocity(0, 0, 0);
        }
    }
}
