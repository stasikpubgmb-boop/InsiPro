package com.insipro.features.impl.player;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.setting.implement.MultiSelectSetting;
import com.insipro.utils.client.Instance;
import com.insipro.events.player.TickEvent;
import net.minecraft.item.Items;

import static com.insipro.utils.display.interfaces.QuickImports.mc;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoDelay extends Module {
    public static NoDelay getInstance() {
        return Instance.get(NoDelay.class);
    }

    public MultiSelectSetting ignoreSetting = new MultiSelectSetting("Убирать заддержку у", "Разрешает выбранные вами действия")
            .value("Прыжка", "ПКМ", "Ломания блока", "Пузырек опыта");

    public NoDelay() {
        super("NoDelay", "NoDelay", ModuleCategory.PLAYER);
        setup(ignoreSetting);
    }

    
    @EventHandler
    
    public void onTick(TickEvent e) {
        if (ignoreSetting.isSelected("Ломания блока")) mc.interactionManager.blockBreakingCooldown = 0;
        if (ignoreSetting.isSelected("Прыжка")) mc.player.jumpingCooldown = 0;
        if (ignoreSetting.isSelected("ПКМ")) mc.itemUseCooldown = 0;
        if (ignoreSetting.isSelected("Пузырек опыта") && mc.player != null) {
            if (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE) {
                mc.itemUseCooldown = 0;
            }
        }
    }
}