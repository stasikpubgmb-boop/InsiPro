package com.insipro.events.player;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import com.insipro.utils.client.managers.event.events.Event;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeathScreenEvent implements Event {
    int ticksSinceDeath;
}
