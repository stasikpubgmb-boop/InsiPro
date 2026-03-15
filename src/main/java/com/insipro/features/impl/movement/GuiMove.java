package com.insipro.features.impl.movement;

import com.insipro.events.item.ClickSlotEvent;
import com.insipro.features.impl.render.TargetESP;
import com.insipro.utils.client.Instance;
import com.insipro.utils.client.packet.network.Network;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.interactions.inv.InventoryFlowManager;
import lombok.Getter;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.interactions.simulate.Simulations;

import com.insipro.utils.interactions.inv.InventoryTask;
import com.insipro.events.container.CloseScreenEvent;
import com.insipro.events.packet.PacketEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import net.minecraft.screen.slot.SlotActionType;

import java.util.*;

@Getter
public class GuiMove extends Module {
    public static GuiMove getInstance() {
        return Instance.get(GuiMove.class);
    }
    private final List<Packet<?>> packets = new ArrayList<>();
    public static final SelectSetting mode = new SelectSetting("Режим", "Выберите режим передвижения в инвентаре")
            .value("Обычный", "ФанТайм", "СпукиТайм", "ХолиВорлд","РиллиВорлд","КопиТайм")
            .selected("СпукиТайм");

    public GuiMove() {
        super("GuiMove", "GuiMove", ModuleCategory.MOVEMENT);
        setup(mode);
    }



    @EventHandler
    public void onPacket(PacketEvent e) {
        if (!mode.isSelected("Обычный")) {
            switch (e.getPacket()) {
                case ClickSlotC2SPacket slot when (!packets.isEmpty() || Simulations.hasPlayerMovement()) && InventoryFlowManager.shouldSkipExecution() -> {
                    packets.add(slot);
                    e.cancel();
                }
                case CloseScreenS2CPacket screen when screen.getSyncId() == 0 -> e.cancel();
                default -> {
                }
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent e) {
        if (!InventoryTask.isServerScreen() && InventoryFlowManager.shouldSkipExecution() && (mode.isSelected("Обычный") || !packets.isEmpty() || mc.player.currentScreenHandler.getCursorStack().isEmpty())) {
            InventoryFlowManager.updateMoveKeys();
        }
    }

    @EventHandler
    public void onClickSlot(ClickSlotEvent e) {
        if (!mode.isSelected("Обычный")) {
            SlotActionType actionType = e.getActionType();
            if ((!packets.isEmpty() || Simulations.hasPlayerMovement()) && ((e.getButton() == 1 && !actionType.equals(SlotActionType.SWAP) && !actionType.equals(SlotActionType.THROW)) || actionType.equals(SlotActionType.PICKUP_ALL))) {
                e.cancel();
            }

        }
    }

    @EventHandler
    public void onCloseScreen(CloseScreenEvent e) {
        if (!mode.isSelected("Обычный") && !packets.isEmpty()) {
            InventoryFlowManager.addTask(() -> {
                packets.forEach(PlayerInteractionHelper::sendPacketWithOutEvent);
                packets.clear();
                InventoryTask.updateSlots();
            });
        }
    }
}