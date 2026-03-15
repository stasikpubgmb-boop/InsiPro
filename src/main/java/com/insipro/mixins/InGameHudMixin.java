package com.insipro.mixins;

import com.insipro.utils.display.render.post.KawaseBlur;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.insipro.utils.display.interfaces.QuickImports;
import com.insipro.utils.display.render.geometry.Render2D;
import com.insipro.Essence;
import com.insipro.utils.client.managers.event.EventManager;
import com.insipro.events.render.DrawEvent;
import com.insipro.utils.math.calc.Calculate;
import com.insipro.features.impl.render.CrossHair;
import com.insipro.features.impl.render.Hud;

import java.util.ConcurrentModificationException;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements QuickImports {
    @Unique private Hud getHud() {
        return Hud.getInstance();
    }

    @Final @Shadow private MinecraftClient client;

    @Shadow protected abstract void renderStatusBars(DrawContext context);

    @Shadow protected abstract void renderMountHealth(DrawContext context);

    @Inject(method = "render", at = @At("RETURN"))
    public void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (context == null || drawEngine == null || tickCounter == null || mc.player == null || mc.world == null) return;
        blur.setup();
        KawaseBlur.INSTANCE.render(context.getMatrices(),6,8);
        DrawEvent event = new DrawEvent(context, drawEngine, tickCounter.getTickDelta(false));
        EventManager.callEvent(event);
        Render2D.onRender(context);
        if (!client.options.hudHidden) {
            Hud hud = getHud();
            if (hud != null) {
                Essence.getInstance().getDraggableRepository().draggable().forEach(draggable -> {
                    if (draggable.getName().equals("Target Hud")) {
                        boolean isVariant1 = hud.targetHudMode.isSelected("1");
                        boolean isVariant2 = hud.targetHudMode.isSelected("2");
                        boolean isTargetHud1 = draggable instanceof com.insipro.display.hud.TargetHud && !(draggable instanceof com.insipro.display.hud.TargetHud2);
                        boolean isTargetHud2 = draggable instanceof com.insipro.display.hud.TargetHud2;

                        
                        if ((isTargetHud1 && !isVariant1) || (isTargetHud2 && !isVariant2)) {
                            draggable.stopAnimation();
                            return;
                        }
                    }
                    if (draggable.canDraw(hud, draggable)) draggable.startAnimation();
                    else draggable.stopAnimation();

                float alpha = draggable.getScaleAnimation().getOutput().floatValue();
                if (!draggable.isCloseAnimationFinished()) {
                    try {
                        float centerX = draggable.getX() + draggable.getWidth() / 2f;
                        float centerY = draggable.getY() + draggable.getHeight() / 2f;

                        Calculate.setAlpha(alpha, () -> {
                            float scale = draggable.getScaleFactor();
                            context.getMatrices().push();
                            context.getMatrices().translate(centerX, centerY, 0);
                            context.getMatrices().scale(scale, scale, 1);
                            context.getMatrices().translate(-centerX, -centerY, 0);
                                draggable.drawDraggable(context);
                            context.getMatrices().pop();
                        });
                    } catch (ConcurrentModificationException ignored) {}
                }
                });
            }
        }
    }

    @Inject(method = "renderCrosshair", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;CROSSHAIR_TEXTURE:Lnet/minecraft/util/Identifier;"), cancellable = true)
    public void renderCrosshairHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        CrossHair crossHair = CrossHair.getInstance();
        if (crossHair.isState()) {
            crossHair.onRenderCrossHair();
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    public void renderStatusEffectOverlayHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Hud hud = getHud();
        if (hud != null && hud.isState() && hud.interfaceSettings.isSelected("Potions")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "HEAD"), cancellable = true)
    private void renderScoreboardSidebarHook(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        com.insipro.features.impl.render.NoRender noRender = com.insipro.features.impl.render.NoRender.getInstance();
        if (noRender != null && noRender.shouldHideScoreboard()) {
            ci.cancel();
            return;
        }
        Hud hud = getHud();
        if (hud != null && hud.isState() && hud.interfaceSettings.isSelected("Score Board")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderVignette(CallbackInfo ci) {
        com.insipro.features.impl.render.NoRender noRender = com.insipro.features.impl.render.NoRender.getInstance();
        if (noRender != null && noRender.shouldHideVignette()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderOverlayMessage", at = @At(value = "HEAD"), cancellable = true)
    private void renderOverlayMessage(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Hud hud = getHud();
        if (hud != null && hud.isState() && hud.interfaceSettings.isSelected("HotBar")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At(value = "HEAD"), cancellable = true)
    private void renderExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Hud hud = getHud();
        if (hud != null && hud.isState() && hud.interfaceSettings.isSelected("HotBar")) {
            ci.cancel();
        }
    }

    @Inject(method = "renderMainHud", at = @At(value = "HEAD"), cancellable = true)
    private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Hud hud = getHud();
        if (hud != null && hud.isState() && hud.interfaceSettings.isSelected("HotBar")) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, InGameHud.HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, 0, 0, 1, 1);
            if (client.interactionManager != null && client.interactionManager.hasStatusBars()) {
                renderStatusBars(context);
            }
            this.renderMountHealth(context);
            ci.cancel();
        }
    }

}
