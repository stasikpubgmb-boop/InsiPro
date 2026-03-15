package com.insipro.events.player;

import com.insipro.utils.client.managers.event.events.Event;
import com.insipro.utils.client.managers.event.events.callables.EventCancellable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class EventNoSlow extends EventCancellable {
    public boolean start;
}