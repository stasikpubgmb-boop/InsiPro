package com.insipro.events.player;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.insipro.utils.client.managers.event.events.callables.EventCancellable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
 public class MotionEvent extends EventCancellable {
    double x, y, z;
    float yaw, pitch;
    boolean onGround;
}
