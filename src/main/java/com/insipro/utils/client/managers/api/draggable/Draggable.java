package com.insipro.utils.client.managers.api.draggable;

import net.minecraft.client.gui.DrawContext;
import com.insipro.events.container.SetScreenEvent;
import com.insipro.events.packet.PacketEvent;

public interface Draggable {
    boolean visible();

    void tick();

    void render(DrawContext context, int mouseX, int mouseY, float delta);

    void packet(PacketEvent e);

    void setScreen(SetScreenEvent screen);

    boolean mouseClicked(double mouseX, double mouseY, int button);

    boolean mouseReleased(double mouseX, double mouseY, int button);

    boolean mouseScrolled(double mouseX, double mouseY, double amount);
}
