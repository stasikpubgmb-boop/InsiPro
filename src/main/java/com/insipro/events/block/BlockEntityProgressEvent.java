package com.insipro.events.block;

import net.minecraft.block.entity.BlockEntity;
import com.insipro.utils.client.managers.event.events.Event;

public record BlockEntityProgressEvent(BlockEntity blockEntity, Type type) implements Event {
    public enum Type {
        ADD, REMOVE
    }
}
