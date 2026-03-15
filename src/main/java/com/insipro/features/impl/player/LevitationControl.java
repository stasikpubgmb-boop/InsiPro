package com.insipro.features.impl.player;

import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.BooleanSetting;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.utils.client.managers.event.EventHandler;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

public class LevitationControl extends Module{

    SliderSettings up = new SliderSettings("Скорость вверх","").setValue(0.5f).range(0,2);
    SliderSettings down = new SliderSettings("Скорость вниз","").setValue(0.5f).range(0,2);
    BooleanSetting stuck=new BooleanSetting("Зависать","Стопит вас на месте");

    public LevitationControl() {
        super("LevitationControl", ModuleCategory.PLAYER);
        setup(up, down, stuck);
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (!mc.player.hasStatusEffect(StatusEffects.LEVITATION)) return;

        double test1214 = switch(0){
            default -> {
                if (mc.options.jumpKey.isPressed())   yield  up.getValue();
                if (mc.options.sneakKey.isPressed())  yield -down.getValue();
                if (stuck.isValue())                  yield  0.0;
                yield mc.player.getVelocity().y;
            }
        };

        if (Math.abs(mc.player.getVelocity().y - test1214) > 0.001) {
            mc.player.setVelocity(mc.player.getVelocity().x, test1214, mc.player.getVelocity().z);
        }
    }
}
