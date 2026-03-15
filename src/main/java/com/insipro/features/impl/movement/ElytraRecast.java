package com.insipro.features.impl.movement;

import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.events.player.TickEvent;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElytraRecast extends Module {

    public ElytraRecast() {
        super("ElytraRecast", "ElytraRecast", ModuleCategory.MOVEMENT);
        setup();
    }

    
    private boolean shouldRecast() {
        if (mc.player == null) return false;

        ItemStack chestItem = mc.player.getEquippedStack(EquipmentSlot.CHEST);

        
        boolean elytraNotBroken = chestItem.isOf(Items.ELYTRA) 
                && (!chestItem.isDamageable() || (chestItem.getMaxDamage() - chestItem.getDamage() > 1));

        return !mc.player.getAbilities().flying 
                && !mc.player.hasVehicle() 
                && !mc.player.isClimbing() 
                && !mc.player.isTouchingWater() 
                && !mc.player.hasStatusEffect(StatusEffects.LEVITATION)
                && elytraNotBroken
                && mc.options.jumpKey.isPressed();
    }

    
    public boolean recastElytra() {
        if (shouldRecast()) {
            PlayerInteractionHelper.startFallFlying();
            return true;
        }
        return false;
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (PlayerInteractionHelper.nullCheck()) return;
        recastElytra();
    }
}
