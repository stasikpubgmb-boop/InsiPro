package com.insipro.features.impl.player;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.events.block.BlockBreakingEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FastBreak extends Module {

    public FastBreak() {super("FastBreak", "FastBreak", ModuleCategory.PLAYER);}

    @EventHandler
    
    public void onBlockBreaking(BlockBreakingEvent e) {
        BlockPos blockPos = e.blockPos();
        Direction direction = e.direction();
        if (mc.interactionManager.currentBreakingProgress >= 0.5) {
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, direction));
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, blockPos, direction));
        }
    }

}
