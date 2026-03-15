package com.insipro.events.keyboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.insipro.utils.client.managers.event.events.callables.EventCancellable;

@Getter
@Setter
@AllArgsConstructor
public class MouseRotationEvent extends EventCancellable {
    float cursorDeltaX, cursorDeltaY;
}
