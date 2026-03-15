package com.insipro.features.impl.misc.creeperFarm;

import com.insipro.events.player.RotationUpdateEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author nikitavodolaz
 * @since 07.02.2026
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreeperFarmModule extends Module {
    CreeperFarmController controller = new CreeperFarmController();

    public CreeperFarmModule() {
        super("Creeper Farm", ModuleCategory.MISC);
    }

    @EventHandler
    public void tickEvent(TickEvent tickEvent) {
        controller.onTick();
    }

    @EventHandler
    public void rotationUpdateEvent(RotationUpdateEvent rotationUpdateEvent) {
        controller.onRotationUpdate();
    }
}
