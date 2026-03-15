package com.insipro.utils.features.aura.warp;

import com.insipro.features.impl.player.AutoPilot;
import com.insipro.utils.features.aura.utils.MathAngle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import com.insipro.utils.display.interfaces.QuickImports;
import com.insipro.utils.features.aura.rotations.constructor.RotateConstructor;

@Setter
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TurnsConstructor implements QuickImports {
    Turns angle;
    Vec3d vec3d;
    Entity entity;
    RotateConstructor angleSmooth;
    int ticksUntilReset;
    float resetThreshold;
    public boolean moveCorrection;
    @Getter(AccessLevel.PUBLIC)
    public boolean freeCorrection;
    public boolean changeLook = (AutoPilot.getInstance() != null && AutoPilot.getInstance().isState() && AutoPilot.getInstance().target != null);

    public Turns nextRotation(Turns fromAngle, boolean isResetting) {
        if (isResetting) {
            return angleSmooth.limitAngleChange(fromAngle, MathAngle.fromVec2f(mc.player.getRotationClient()));
        }
        return angleSmooth.limitAngleChange(fromAngle, angle, vec3d, entity);
    }
}