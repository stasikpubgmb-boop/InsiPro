package com.insipro.features.impl.render;

import com.insipro.features.impl.player.FreeLook;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.math.MathHelper;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.*;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.math.calc.Calculate;
import com.insipro.utils.client.Instance;
import com.insipro.events.keyboard.HotBarScrollEvent;
import com.insipro.events.keyboard.KeyEvent;
import com.insipro.events.render.CameraEvent;
import com.insipro.events.render.FovEvent;
import com.insipro.utils.features.aura.utils.MathAngle;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CameraSettings extends Module {

    public static CameraSettings getInstance() {
        return Instance.get(CameraSettings.class);
    }

    float fov = 110;
    float smoothFov = 30;
    float lastChangedFov = 30;

   static BooleanSetting clipSetting = new BooleanSetting("Сквозь блоки", "Камера проходит сквозь блоки").setValue(true);
    SliderSettings distanceSetting = new SliderSettings("Дистанция камеры", "Настройка расстояния камеры")
            .setValue(3.0F).range(2.0F, 15.0F);
    BindSetting zoomSetting = new BindSetting("Зум", "Клавиша для увеличения камеры");

    public CameraSettings() {
        super("CameraSettings", "CameraSettings", ModuleCategory.RENDER);
        setup(clipSetting, zoomSetting, distanceSetting);
    }

    @EventHandler
    public void onKey(KeyEvent e) {
        if (e.isKeyDown(zoomSetting.getKey())) {
            fov = Math.min(lastChangedFov, mc.options.getFov().getValue() - 20);
        }
        if (e.isKeyReleased(zoomSetting.getKey(), true)) {
            lastChangedFov = fov;
            fov = mc.options.getFov().getValue();
        }
    }

    @EventHandler
    public void onHotBarScroll(HotBarScrollEvent e) {
        if (PlayerInteractionHelper.isKey(zoomSetting)) {
            fov = (int) MathHelper.clamp(fov - e.getVertical() * 10, 10, mc.options.getFov().getValue());
            e.cancel();
        }
    }

    @EventHandler
    public void onFov(FovEvent e) {
        e.setFov((int) MathHelper.clamp((smoothFov = Calculate.interpolateSmooth(1.6, smoothFov, fov)) + 1, 10, mc.options.getFov().getValue()));
        e.cancel();
    }

    @EventHandler
    public void onCamera(CameraEvent e) {
        e.setCameraClip(clipSetting.isValue());
        e.setDistance(distanceSetting.getValue());
        FreeLook freeLook = Instance.get(FreeLook.class);
        if (!freeLook.isState() || !PlayerInteractionHelper.isKey(freeLook.freeLookSetting)) {
            e.setAngle(MathAngle.cameraAngle());
        }
        e.cancel();
    }
}