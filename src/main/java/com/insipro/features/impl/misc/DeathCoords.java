package com.insipro.features.impl.misc;

import com.insipro.events.packet.PacketEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.chat.ChatMessage;
import com.insipro.utils.client.managers.event.EventHandler;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;

public class DeathCoords extends Module {
    public DeathCoords() {
        super("DeathCoords", ModuleCategory.MISC);
    }

    @EventHandler
    public void onPacket(PacketEvent e){
        if (e.getPacket() instanceof DeathMessageS2CPacket) {
            String text = String.format("%.0f %.0f %.0f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
            ChatMessage.send( Text.literal(text).styled(style->style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text))));
        }
    }
}
