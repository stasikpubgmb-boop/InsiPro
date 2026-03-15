package com.insipro.features.impl.misc.newAutoMine;

import com.insipro.events.player.InputEvent;
import com.insipro.events.player.RotationUpdateEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;

/**
 * @author nikitavodolaz
 * @since 12.02.2026
 */

public class NewAutoMineModule extends Module {
    AutoMineController controller = new AutoMineController();

    public NewAutoMineModule() {
        super("Test Auto Mine", ModuleCategory.MISC);
    }

    @EventHandler
    public void onTick(TickEvent tickEvent) {
        controller.onTick();
    }

    @EventHandler
    public void onRotationUpdate(RotationUpdateEvent rotationUpdateEvent) {
        controller.onRotationUpdate(rotationUpdateEvent);
    }

    @EventHandler
    public void onInput(InputEvent inputEvent) {
        controller.onInput(inputEvent);
    }
}
