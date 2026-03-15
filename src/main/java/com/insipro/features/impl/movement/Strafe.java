package com.insipro.features.impl.movement;

import com.google.common.eventbus.Subscribe;
import com.insipro.events.player.TickEvent;
import com.insipro.features.impl.combat.Aura;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.Instance;
import com.insipro.utils.features.aura.warp.Turns;
import com.insipro.utils.features.aura.warp.TurnsConnection;
import com.insipro.utils.features.aura.warp.TurnsConfig;
import com.insipro.utils.features.aura.warp.TurnsConstructor;
import com.insipro.utils.interactions.simulate.Simulations;
import com.insipro.utils.math.task.TaskPriority;
import net.minecraft.client.MinecraftClient;

public class Strafe extends Module {

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    public SelectSetting mode = new SelectSetting("Режим", "Выберите тип стрейфов")
            .value("Матрикс", "Грим")
            .selected("Матрикс");
    SliderSettings speed = new SliderSettings("Скорость", "Выберите скорость для стрейфа")
            .setValue(0.42F).range(0F, 1F);

    private float lastYaw, lastPitch;
    private final Turns rot = new Turns(0, 0);

    public Strafe() {
        super("Strafe", "Strafe", ModuleCategory.MOVEMENT);
        setup(mode, speed);
    }

    public static Strafe getInstance() {
        return Instance.get(Strafe.class);
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (mc.player == null || mc.world == null) return;

        boolean moving = Simulations.hasPlayerMovement();

        float yaw = mc.player.getYaw();

        String modeVal = mode.getSelected();
        if (modeVal.equals("Матрикс")) {
            if (moving) {
                yaw = Simulations.moveYaw(mc.player.getYaw());
                double motion = speed.getValue() * 1.5f;
                Simulations.setVelocity(motion);
            } else {
                Simulations.setVelocity(0);
            }
            mc.player.setVelocity(mc.player.getVelocity().x, mc.player.getVelocity().y, mc.player.getVelocity().z);
        } else if (modeVal.equals("Грим")) {
            if (moving) {
                TurnsConfig.freeCorrection = true;
                yaw = Simulations.moveYaw(mc.player.getYaw());
                rot.setYaw(yaw);
                rot.setPitch(mc.player.getPitch());
                TurnsConnection.INSTANCE.rotateTo(rot, TurnsConfig.DEFAULT, TaskPriority.LOW_PRIORITY, this);
            }
        }

        lastYaw = yaw;
        lastPitch = 0;
    }


    @Override
    public void activate() {
        super.activate();
        lastYaw = mc.player != null ? mc.player.getYaw() : 0;
        lastPitch = mc.player != null ? mc.player.getPitch() : 0;
    }
}
