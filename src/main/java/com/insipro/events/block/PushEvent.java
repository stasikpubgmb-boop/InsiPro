package com.insipro.events.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.insipro.utils.client.managers.event.events.callables.EventCancellable;

@Getter
@AllArgsConstructor
public class PushEvent extends EventCancellable {
    private Type type;

    public enum Type {
        COLLISION, BLOCK, WATER
    }
}
