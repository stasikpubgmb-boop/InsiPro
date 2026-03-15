package com.insipro.features.impl.misc;

import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.display.interfaces.QuickImports;
import com.insipro.utils.math.time.TimerUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

public class TapeMouse extends Module implements QuickImports {

    private final SelectSetting buttonSetting = new SelectSetting("Кнопка", "Выберите кнопку мыши")
            .value("Левая кнопка", "Правая кнопка")
            .selected("Левая кнопка");

    private final SliderSettings delaySetting = new SliderSettings("Задержка (мс)", "Интервал между кликами")
            .range(100, 30_000)
            .step(50)
            .setValue(450);

    private final TimerUtil timer = new TimerUtil();

    public TapeMouse() {
        super("TapeMouse", "Tape Mouse", ModuleCategory.MISC);
        setup(buttonSetting, delaySetting);
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null || mc.interactionManager == null) return;

        long delayMs = (long) delaySetting.getValue();
        if (!timer.hasTimeElapsed(delayMs)) return;

        if (buttonSetting.isSelected("Левая кнопка")) {
            if (mc.crosshairTarget instanceof EntityHitResult entityHit) {
                mc.interactionManager.attackEntity(mc.player, entityHit.getEntity());
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        } else {
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
        timer.resetCounter();
    }
}
