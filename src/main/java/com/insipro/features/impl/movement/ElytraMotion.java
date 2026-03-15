package com.insipro.features.impl.movement;


import com.insipro.features.impl.combat.Aura;
import com.insipro.events.packet.PacketEvent;
import com.insipro.features.module.setting.implement.BooleanSetting;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.utils.client.chat.ChatMessage;
import com.insipro.utils.interactions.inv.InventoryTask;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.events.player.TickEvent;
import com.insipro.utils.client.Instance;
import com.insipro.utils.math.time.StopWatch;

import java.util.Random;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElytraMotion extends Module {
    public static Fly getInstance() {
        return Instance.get(Fly.class);
    }
    @NonFinal StopWatch timer = new StopWatch();
    @NonFinal Vec3d targetPosition = null;
    @NonFinal
    Random random = new Random();
    @NonFinal double rotationAngle = 0.0;
    public ElytraMotion() {
        super("ElytraMotion", "ElytraMotion", ModuleCategory.MOVEMENT);
        setup(auto,timer2);
    }

    BooleanSetting auto= new BooleanSetting("Авто-фейр","Автоматически использует фейрверк");
    SliderSettings timer2 = new SliderSettings("Скорость исп.", "").setValue(500).range(0F, 10000).step(10).visible(()->auto.isValue());

    @EventHandler
    
    public void onTick(TickEvent e) {
        if (!state || mc.player == null || mc.world == null || !mc.player.isGliding()) return;

        Aura aura = Instance.get(Aura.class);

        if (auto.isValue() && timer.every(timer2.getValue())) {
            InventoryTask.swapAndUse(Items.FIREWORK_ROCKET,false);
            timer.reset();
        }

        if (aura.isState()) {
            if (aura.isState() && aura.getTarget() !=null && mc.player.distanceTo(aura.getTarget()) < aura.attackRange.getValue() - 0.425F) {
                mc.player.setVelocity(0, 0.02, 0);
            }
        }
    }


    @EventHandler
    public void onPacket(PacketEvent e) {
        Aura aura = Instance.get(Aura.class);
        if (aura.isState() && aura.getTarget() != null && mc.player.distanceTo(aura.getTarget()) < aura.attackRange.getValue() - 0.15F) {
            switch (e.getPacket()) {
                default -> {
                }
            }
        }
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }
}
