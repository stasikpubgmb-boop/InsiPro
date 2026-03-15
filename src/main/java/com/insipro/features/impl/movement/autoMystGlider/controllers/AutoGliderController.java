package com.insipro.features.impl.movement.autoMystGlider.controllers;

import com.insipro.Essence;
import com.insipro.events.packet.PacketEvent;
import com.insipro.events.player.InputEvent;
import com.insipro.events.player.MoveEvent;
import com.insipro.events.player.RotationUpdateEvent;
import com.insipro.features.impl.misc.ElytraHelper;
import com.insipro.features.impl.movement.autoMystGlider.handers.StealHandler;
import com.insipro.features.impl.movement.autoMystGlider.handers.ConnectionHandler;
import com.insipro.features.impl.movement.autoMystGlider.handers.NoClipHandler;
import com.insipro.features.impl.movement.autoMystGlider.receivers.AutoMystGliderModule;
import com.insipro.features.impl.movement.autoMystGlider.states.ConnectionState;
import com.insipro.features.impl.movement.autoMystGlider.states.GlidePhase;
import com.insipro.features.impl.movement.autoMystGlider.states.GlideState;
import com.insipro.utils.client.chat.ChatMessage;
import com.insipro.utils.display.interfaces.QuickImports;
import com.insipro.utils.features.aura.warp.TurnsConnection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoGliderController implements QuickImports {
    ConnectionHandler connectionHandler;
    StealHandler stealHandler;
    NoClipHandler noClipHandler;
    GlideState state;

    public AutoGliderController(Vec3d vec3d, AutoMystGliderModule module) {
        state = new GlideState(vec3d);
        stealHandler = new StealHandler(module);
        connectionHandler = new ConnectionHandler(state, stealHandler, module);
        noClipHandler = new NoClipHandler(state, connectionHandler.getConnection());
    }

    public void onRotationUpdate(RotationUpdateEvent rotationUpdateEvent) {
        GlidePhase phase = getPhase();
        if (phase == GlidePhase.LOOTING) {
            stealHandler.onRotationUpdate(rotationUpdateEvent);
        }
    }

    public void onInput(InputEvent inputEvent) {
        if (mc.player == null || state.vec3d() == null || !connectionHandler.getConnection().isConnected()) return;

        GlidePhase phase = getPhase();
        if (phase == GlidePhase.FLYING) {
            handleFlyingInput(inputEvent);
        }
    }

    private void handleFlyingInput(InputEvent inputEvent) {
        if (state.done()) {
            if (state.shouldLand()) {
                inputEvent.setSneak(true);
                return;
            }
        }

        if (state.verticalGlide()) {
            inputEvent.setJumping(true);
        }

        if (state.horizontalGlide()) {
            PlayerInput playerInput = state.selectInput();
            inputEvent.setDirectional(playerInput.forward(), playerInput.backward(), playerInput.left(), playerInput.right());
        }
    }

    public void onTick() {
        if (mc.player == null) return;

        GlidePhase phase = getPhase();
        ConnectionState connection = connectionHandler.getConnection();

        // Свап элитры при LOOTING
        if (phase == GlidePhase.LOOTING && !connectionHandler.isElytraSwaped()
                && connection.getAnarchy() != -1
                && mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA)
                && System.currentTimeMillis() - connectionHandler.getElytraChangeTime() > 2000) {
            ElytraHelper elytraHelper = Essence.getInstance().getModuleProvider().get(ElytraHelper.class);
            elytraHelper.changeChestPlate();
            connectionHandler.setElytraSwaped(true);
            connectionHandler.setElytraChangeTime(System.currentTimeMillis());
        }

        switch (phase) {
            case FLYING -> {
                connectionHandler.onTick();
                if (connection.isConnected() && canEnableNoClip()) {
                    noClipHandler.onTick();
                } else {
                    noClipHandler.end();
                }
            }
            case LOOTING -> stealHandler.onTick();
            default -> connectionHandler.onTick(); // WAITING_REOPEN, HUB_TO_HOME, AT_HOME, HUB_TO_NEXT, WAITING_COORDS
        }
    }

    public void onMove(MoveEvent moveEvent) {
        if (mc.player == null || !connectionHandler.getConnection().isConnected()) return;

        GlidePhase phase = getPhase();
        if (phase == GlidePhase.FLYING) {
            if (canEnableNoClip()) {
                noClipHandler.onMove(moveEvent);
            } else if (state.done() && !isAlignedToTarget()) {
                // Подлетаем к координатам — ограничиваем скорость 0.1 для выравнивания по центру
                setMoveMotion(moveEvent, 0.1);
            }
        }
    }

    /** Проверяет, выровнен ли игрок по центру целевого блока (±0.3 по XZ) */
    private boolean isAlignedToTarget() {
        Vec3d target = state.vec3d();
        Vec3d pos = mc.player.getPos();
        double dx = Math.abs(target.x - pos.x);
        double dz = Math.abs(target.z - pos.z);
        return dx < 0.3 && dz < 0.3;
    }

    /** Устанавливает скорость движения по XZ в MoveEvent */
    private void setMoveMotion(MoveEvent move, double motion) {
        if (mc.player == null) return;
        ChatMessage.brandmessage("замедляюсь");

        Vec3d target = state.vec3d();
        Vec3d pos = mc.player.getPos();
        double dx = target.x - pos.x;
        double dz = target.z - pos.z;
        double dist = Math.sqrt(dx * dx + dz * dz);

        if (dist < 0.05) {
            // Уже на месте — обнуляем горизонтальную скорость
            move.setMovement(new Vec3d(0, move.getMovement().y, 0));
        } else {
            // Двигаемся к цели с ограниченной скоростью
            double nx = (dx / dist) * motion;
            double nz = (dz / dist) * motion;
            move.setMovement(new Vec3d(nx, move.getMovement().y, nz));
        }
    }

    public void onPacket(PacketEvent packetEvent) {
        if (mc.player == null || state.vec3d() == null || !connectionHandler.getConnection().isConnected())
            return;

        GlidePhase phase = getPhase();
        if (phase == GlidePhase.FLYING && canEnableNoClip()) {
            noClipHandler.onPacket(packetEvent);
        }
    }

    public void reload() {
        connectionHandler.reload();
        noClipHandler.end();
        TurnsConnection.INSTANCE.setRotation(null);
    }

    public boolean canEnableNoClip() {
        return state.done() && state.solidBlockInRadius(2);
    }

    private GlidePhase getPhase() {
        return connectionHandler.getConnection().getPhase();
    }
}
