package com.insipro.features.impl.player;

import com.insipro.features.impl.combat.Aura;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.Instance;
import net.minecraft.client.Keyboard;
import net.minecraft.world.GameRules;

public class SaturationViewer extends Module {

    public static SaturationViewer getInstance() {
        return Instance.get(SaturationViewer.class);
    }

    public SaturationViewer() {
        super("SaturationViewer", ModuleCategory.PLAYER);
    }
}
