package com.insipro.events.keyboard;


import com.insipro.utils.client.managers.event.events.callables.EventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuiScrollEvent extends EventCancellable {
    private double mouseX;
    private double mouseY;
    private double horizontal;
    private double vertical;
}

