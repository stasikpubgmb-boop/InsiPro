package com.insipro.events.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.insipro.utils.client.managers.event.events.Event;

@Getter
@AllArgsConstructor
public class RotationUpdateEvent implements Event {
    byte type;
}
