package com.insipro.events.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.DrawContext;
import com.insipro.utils.client.managers.event.events.Event;
import com.insipro.utils.display.render.draw.DrawEngine;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DrawEvent implements Event {
    DrawContext drawContext;
    DrawEngine drawEngine;
    float partialTicks;
}
