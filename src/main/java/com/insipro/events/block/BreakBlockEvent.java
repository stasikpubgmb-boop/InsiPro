package com.insipro.events.block;

import net.minecraft.util.math.BlockPos;
import com.insipro.utils.client.managers.event.events.Event;

public record BreakBlockEvent(BlockPos blockPos) implements Event {}
