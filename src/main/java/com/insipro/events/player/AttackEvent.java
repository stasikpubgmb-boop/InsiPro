package com.insipro.events.player;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.Entity;
import com.insipro.utils.client.managers.event.events.callables.EventCancellable;

@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttackEvent extends EventCancellable {
    Entity entity;
}
