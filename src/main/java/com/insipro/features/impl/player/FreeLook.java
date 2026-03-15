package com.insipro.features.impl.player;

import com.insipro.utils.features.aura.warp.Turns;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.BindSetting;
import com.insipro.events.keyboard.KeyEvent;
import com.insipro.events.keyboard.MouseRotationEvent;
import com.insipro.events.render.CameraEvent;
import com.insipro.events.render.FovEvent;
import com.insipro.utils.features.aura.utils.MathAngle;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FreeLook extends Module {

    Perspective perspective;
    Turns angle;
    public static BindSetting freeLookSetting = new BindSetting("Свободный обзор", "Клавиша для свободного обзора");

    public FreeLook() {
        super("FreeLook", "FreeLook", ModuleCategory.RENDER);
        setup(freeLookSetting);
        angle = null;
    }

    @EventHandler
    public void onKey(KeyEvent e) {
        if (e.isKeyDown(freeLookSetting.getKey())) {
            perspective = mc.options.getPerspective();
            if (angle == null) {
                angle = MathAngle.cameraAngle();
            }
        }
    }

    @EventHandler
    public void onFov(FovEvent e) {
        if (PlayerInteractionHelper.isKey(freeLookSetting)) {
            if (mc.options.getPerspective().isFirstPerson()) mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            if (angle == null) {
                angle = MathAngle.cameraAngle();
            }
        } else if (perspective != null) {
            mc.options.setPerspective(perspective);
            perspective = null;
            angle = null;
        }
    }

    @EventHandler
    public void onMouseRotation(MouseRotationEvent e) {
        if (PlayerInteractionHelper.isKey(freeLookSetting)) {
            if (angle == null) {
                angle = MathAngle.cameraAngle();
            }
            angle.setYaw(angle.getYaw() + e.getCursorDeltaX() * 0.15F);
            angle.setPitch(MathHelper.clamp(angle.getPitch() + e.getCursorDeltaY() * 0.15F, -90F, 90F));
            e.cancel();
        } else {
            angle = null;
        }
    }

    @EventHandler
    public void onCamera(CameraEvent e) {
        if (PlayerInteractionHelper.isKey(freeLookSetting) && angle != null) {
            e.setAngle(angle);
            e.cancel();
        }
    }
}