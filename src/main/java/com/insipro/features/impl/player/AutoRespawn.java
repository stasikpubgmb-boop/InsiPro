package com.insipro.features.impl.player;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;

import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.packet.network.Network;
import com.insipro.events.packet.PacketEvent;
import com.insipro.events.player.DeathScreenEvent;

@SuppressWarnings("all")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoRespawn extends Module {

    SelectSetting modeSetting = new SelectSetting("Режим", "Выберите, что будет использоваться").value("ФанТайм возврат", "Обычный");

    public AutoRespawn() {
        super("AutoRespawn", "AutoRespawn", ModuleCategory.PLAYER);
        setup(modeSetting);
    }

    @EventHandler
    
    public void onPacket(PacketEvent e) {
        switch (e.getPacket()) {
            case DeathMessageS2CPacket message when Network.getWorldType().equals("lobby") && modeSetting.isSelected("ФанТайм возврат") -> {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(1448, 1337, 228, false, false));
                mc.player.requestRespawn();
                mc.player.closeScreen();
            }
            default -> {
            }
        }
    }

    
    @EventHandler
    public void onDeathScreen(DeathScreenEvent e) {
        if (modeSetting.isSelected("Обычный")) {
            mc.player.requestRespawn();
            mc.setScreen(null);
        }
    }
}
