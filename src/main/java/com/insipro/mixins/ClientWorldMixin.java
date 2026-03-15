package com.insipro.mixins;

import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.insipro.utils.client.managers.event.EventManager;
import com.insipro.utils.display.interfaces.QuickImports;
import com.insipro.events.player.EntitySpawnEvent;
import com.insipro.events.render.WorldLoadEvent;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements QuickImports {

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initHook(CallbackInfo info) {
        EventManager.callEvent(new WorldLoadEvent());
    }

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    public void addEntityHook(Entity entity, CallbackInfo ci) {
        if (PlayerInteractionHelper.nullCheck()) return;
        EntitySpawnEvent event = new EntitySpawnEvent(entity);
        EventManager.callEvent(event);
        if (event.isCancelled()) ci.cancel();
    }
}