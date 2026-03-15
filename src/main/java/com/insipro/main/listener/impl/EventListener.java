package com.insipro.main.listener.impl;

import com.insipro.utils.interactions.inv.InventoryFlowManager;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.managers.api.draggable.AbstractDraggable;
import com.insipro.utils.client.packet.network.Network;
import com.insipro.Essence;
import com.insipro.main.listener.Listener;
import com.insipro.events.item.UsingItemEvent;
import com.insipro.events.packet.PacketEvent;
import com.insipro.events.player.TickEvent;

public class EventListener implements Listener {
    public static boolean serverSprint;
    public static int selectedSlot;

    @EventHandler
    public void onTick(TickEvent e) {
        Network.tick();
        Essence.getInstance().getAttackPerpetrator().tick();
        InventoryFlowManager.tick();
        Essence.getInstance().getDraggableRepository().draggable().forEach(AbstractDraggable::tick);
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        switch (e.getPacket()) {
            case ClientCommandC2SPacket command -> serverSprint = switch (command.getMode()) {
                case ClientCommandC2SPacket.Mode.START_SPRINTING -> true;
                case ClientCommandC2SPacket.Mode.STOP_SPRINTING -> false;
                default -> serverSprint;
            };
            case UpdateSelectedSlotC2SPacket slot -> selectedSlot = slot.getSelectedSlot();
            default -> {}
        }
        Network.packet(e);
        Essence.getInstance().getAttackPerpetrator().onPacket(e);
        Essence.getInstance().getDraggableRepository().draggable().forEach(drag -> drag.packet(e));
    }

    @EventHandler
    public void onUsingItemEvent(UsingItemEvent e) {
        Essence.getInstance().getAttackPerpetrator().onUsingItem(e);
    }
}
