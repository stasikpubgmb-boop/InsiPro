package com.insipro.features.impl.player;


import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.interactions.inv.InventoryFlowManager;
import com.insipro.utils.interactions.inv.InventoryTask;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.MultiSelectSetting;
import com.insipro.utils.interactions.item.ItemToolkit;
import com.insipro.utils.math.script.Script;
import com.insipro.events.player.TickEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoUse extends Module {
    Script script = new Script();

    MultiSelectSetting multiSetting = new MultiSelectSetting("Авто использование", "Выберите, что будет использоваться").value("Еды", "Невидимости");

    public AutoUse() {
        super("AutoUse", "AutoUse", ModuleCategory.PLAYER);
        setup(multiSetting);
    }

    @Override
    public void deactivate() {
        script.update();
    }

    @EventHandler
    
    public void onTick(TickEvent e) {
        for (String string : multiSetting.getSelected())
            switch (string) {
                case "Еды" -> {
                    Slot slot = InventoryTask.getFoodMaxSaturationSlot();
                    if (slot != null && mc.player.getHungerManager().isNotFull() && swapAndEat(slot)) {
                        return;
                    }
                }
                case "Невидимости" -> {
                    Slot slot = InventoryTask.getPotion(StatusEffects.INVISIBILITY);
                    if (slot != null && !PlayerInteractionHelper.isPotionActive(StatusEffects.INVISIBILITY) && swapAndEat(slot)) {
                        return;
                    }
                }
            }
        script.update();
    }

    public boolean swapAndEat(Slot slot) {
        ItemStack stack = slot.getStack();
        if (!mc.player.getItemCooldownManager().isCoolingDown(stack)) {
            if (!mc.player.getOffHandStack().equals(stack)) {
                if (InventoryFlowManager.script.isFinished()) {
                    InventoryTask.swapHand(slot, Hand.OFF_HAND, true, true);
                    script.cleanup().addTickStep(0, () -> InventoryTask.swapHand(slot, Hand.OFF_HAND, true, true));
                }
            } else {
                ItemToolkit.INSTANCE.useHand(Hand.OFF_HAND);
            }
            return true;
        }
        return false;
    }
}
