package com.insipro.features.impl.player;


import com.insipro.events.player.InputEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.BlockPos;

public class SafeWalk extends Module {

    public SafeWalk() {
        super("SafeWalk", ModuleCategory.PLAYER);
        setup();
    }

    @EventHandler
    public void onInput(InputEvent e) {
        if (mc.player == null || mc.world == null) return;
        BlockPos pos = mc.player.getBlockPos().down();
        boolean atEdge = mc.world.getBlockState(pos).getBlock() == Blocks.AIR && mc.player.isOnGround();

        if (atEdge) {
            e.setSneak(true);
        }
    }
}


