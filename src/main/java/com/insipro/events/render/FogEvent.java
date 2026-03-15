package com.insipro.events.render;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import com.insipro.utils.client.managers.event.events.callables.EventCancellable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FogEvent extends EventCancellable {
    float distance;
    int color;
}
