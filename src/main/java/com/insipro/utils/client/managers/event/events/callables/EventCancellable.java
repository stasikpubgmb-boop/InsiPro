package com.insipro.utils.client.managers.event.events.callables;

import lombok.Setter;
import com.insipro.utils.client.managers.event.events.Cancellable;
import com.insipro.utils.client.managers.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    @Setter
    private boolean cancelled;

    protected EventCancellable() {
    }

    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    

    @Override
    public void cancel() {
        cancelled = true;
    }
}