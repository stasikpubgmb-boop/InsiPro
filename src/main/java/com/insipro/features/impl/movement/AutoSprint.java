package com.insipro.features.impl.movement;


import com.insipro.main.listener.impl.EventListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.effect.StatusEffects;
import com.insipro.features.module.setting.implement.MultiSelectSetting;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.Instance;
import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoSprint extends Module {
    public static AutoSprint getInstance() {
        return Instance.get(AutoSprint.class);
    }

    public static int tickStop;

    MultiSelectSetting settings = new MultiSelectSetting("Игнорировать эффект", "Игнорировать эффекты игрока")
            .value("Замедление", "Слепота");

    public AutoSprint() {
        super("AutoSprint", "AutoSprint", ModuleCategory.MOVEMENT);
    }

    @EventHandler
    
    public void onTick(TickEvent e) {
        boolean horizontal = mc.player.horizontalCollision && !mc.player.collidedSoftly;
        boolean sneaking = mc.player.isSneaking() && !mc.player.isSwimming();
        if (!(settings.isSelected("Слепота") && mc.player.hasStatusEffect(StatusEffects.BLINDNESS))
                && !(settings.isSelected("Замедление") && mc.player.hasStatusEffect(StatusEffects.SLOWNESS))
                && tickStop > 0 || sneaking) {
            mc.player.setSprinting(false);
        } else if (!horizontal && mc.player.forwardSpeed > 0) {
            mc.player.setSprinting(true);
        }

        tickStop--;
    }

}