package com.insipro.features.impl.combat;

import com.insipro.events.player.AttackEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.utils.client.Instance;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.features.aura.warp.TurnsConnection;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.math.calc.Calculate;
import io.netty.util.internal.MathUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Criticals extends Module {
    public static Criticals getInstance() {
        return Instance.get(Criticals.class);
    }

    SelectSetting mode = new SelectSetting("Mode", "Select bypass mode").value("ReallyWorld");

    public Criticals() {
        super("Criticals", ModuleCategory.COMBAT);
        setup(mode);
    }


    @EventHandler
    public void onAttack(AttackEvent e) {

        if (!PlayerInteractionHelper.isPlayerInBlock(Blocks.COBWEB)) {
            return;
        }

        if (mc.player.isTouchingWater()) return;

        if (mode.isSelected("ReallyWorld")) {
            if (!mc.player.isOnGround() && mc.player.fallDistance == 0) {
                PlayerInteractionHelper.grimSuperBypass$$$(-(mc.player.fallDistance = Calculate.getRandom(1e-5F, 1e-4F)), TurnsConnection.INSTANCE.getRotation().random(1e-3F));
            }
        }
    }
}