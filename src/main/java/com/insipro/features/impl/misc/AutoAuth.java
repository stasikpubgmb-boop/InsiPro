package com.insipro.features.impl.misc;

import com.insipro.events.packet.PacketEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.TextSetting;
import com.insipro.utils.client.managers.event.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import static com.insipro.utils.display.interfaces.QuickImports.mc;

public class AutoAuth extends Module {
    private final TextSetting password = new TextSetting("Пароль", "Пароль для автоматической регистрации/входа")
            .setText("bee1892");

    public AutoAuth() {
        super("AutoAuth", "AutoAuth", ModuleCategory.MISC);
        setup(password);
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (!state || mc.player == null || mc.world == null) return;
        
        if (e.getPacket() instanceof GameMessageS2CPacket chatPacket) {
            String chatMessage = chatPacket.content().getString();
            String pass = password.getText();
            
            if (chatMessage.contains("Войдите") || chatMessage.contains("/login")) {
                if (mc.player.networkHandler != null) {
                    mc.player.networkHandler.sendChatCommand("login " + pass);
                }
            }
            
            if (chatMessage.contains("Зарегистрируйтесь") || chatMessage.contains("/reg")) {
                if (pass != null && pass.length() >= 4 && mc.player.networkHandler != null) {
                    mc.player.networkHandler.sendChatCommand("reg " + pass + " " + pass);
                }
            }
        }
    }
}
