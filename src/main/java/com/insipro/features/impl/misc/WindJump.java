package com.insipro.features.impl.misc;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.BindSetting;
import com.insipro.events.keyboard.HotBarScrollEvent;
import com.insipro.events.keyboard.KeyEvent;
import com.insipro.events.player.HotBarUpdateEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.events.render.WorldRenderEvent;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.interactions.inv.InventoryTask;
import com.insipro.utils.math.time.StopWatch;
import com.insipro.utils.math.script.Script;
import com.insipro.utils.features.aura.utils.MathAngle;
import com.insipro.features.impl.render.Prediction;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WindJump extends Module {

    BindSetting windChargeBind = new BindSetting("Заряд ветра", "Бросить заряд ветра");
    StopWatch stopWatch = new StopWatch();
    Script script = new Script();

    public WindJump() {
        super("WindJump", "WindJump", ModuleCategory.MISC);
        setup(windChargeBind);
    }

    @EventHandler
    public void onHotBarUpdate(HotBarUpdateEvent e) {
        if (!script.isFinished()) e.cancel();
    }

    @EventHandler
    public void onHotBarScroll(HotBarScrollEvent e) {
        if (!script.isFinished()) e.cancel();
    }

    @EventHandler
    public void onKey(KeyEvent e) {
        if (e.isKeyReleased(windChargeBind.getKey())) {
            InventoryTask.swapAndUse(Items.WIND_CHARGE,false);
        }
    }

    @EventHandler
    public void onWorldRender(WorldRenderEvent e) {
        if (PlayerInteractionHelper.isKey(windChargeBind)) {
            ItemStack stack = Items.WIND_CHARGE.getDefaultStack();
            Prediction.getInstance().drawPredictionInHand(e.getStack(), List.of(stack), MathAngle.cameraAngle());
        }
    }

    @EventHandler
    
    public void onTick(TickEvent e) {
        if (!script.isFinished() && stopWatch.every(250)) {
            script.update();
        }
    }
}