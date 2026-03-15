package com.insipro.features.impl.player;

import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.mixins.IMinecraftClient;
import com.insipro.utils.client.Instance;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.managers.event.impl.EventUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoFall extends Module {
    public static NoFall getInstance() {
        return Instance.get(NoFall.class);
    }

    public NoFall() {
        super("NoFall", ModuleCategory.PLAYER);
    }


    @EventHandler
    public void onTick(TickEvent e) {
        if (mc.player.fallDistance > 2.4) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1e-6, mc.player.getZ(), mc.player.getYaw(), ( mc.player).getPitch(), false,false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1e-6, mc.player.getZ(), mc.player.getYaw(), (mc.player).getPitch(), false,false));
            mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.OFF_HAND, 0,mc.player.getYaw(),mc.player.getPitch()));
            mc.player.fallDistance = 0;
        }
    }
}
