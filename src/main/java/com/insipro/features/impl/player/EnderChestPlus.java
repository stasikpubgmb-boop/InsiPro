package com.insipro.features.impl.player;


import com.insipro.utils.interactions.inv.InventoryTask;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.BindSetting;
import com.insipro.events.container.CloseScreenEvent;
import com.insipro.events.container.SetScreenEvent;
import com.insipro.events.keyboard.KeyEvent;
import com.insipro.events.packet.PacketEvent;
import com.insipro.display.hud.Notifications;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnderChestPlus extends Module {
    private HandledScreen<?> screen;
    private final BindSetting bindSetting = new BindSetting("Сложить", "Помещает все предметы в эндер-сундук");

    public EnderChestPlus() {
        super("EnderChestPlus", "Ender-ChestPlus", ModuleCategory.PLAYER);
        setup(bindSetting);
    }

    @Override
    public void deactivate() {
        if (screen != null) {
            screen = null;
            InventoryTask.closeScreen(true);
            Notifications.getInstance().addList("Ender Chest - " + Formatting.RED + "закрыт", 5000);
        }
    }

    @EventHandler
    
    public void onKey(KeyEvent e) {
        if (e.isKeyDown(bindSetting.getKey()) && screen != null) {
            List<Slot> slots = mc.player.currentScreenHandler.slots;
            slots.stream().filter(s -> s.id < slots.size() - 36 && s.getStack().isEmpty())
                    .findFirst().ifPresent(s -> InventoryTask.swapHand(s, Hand.OFF_HAND, false));
            slots.stream().filter(s -> s.id >= slots.size() - 36 && !s.getStack().isEmpty())
                    .forEach(s -> InventoryTask.clickSlot(s, 0, SlotActionType.QUICK_MOVE, false));
        }
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (screen != null && mc.player != null) switch (e.getPacket()) {
            case GameJoinS2CPacket join -> deactivate();
            case OpenScreenS2CPacket open -> deactivate();
            case CloseScreenS2CPacket close -> deactivate();
            case PlayerRespawnS2CPacket respawn -> deactivate();
            case CloseHandledScreenC2SPacket close -> e.cancel();
            case PlayerActionC2SPacket player when player.getAction().equals(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND) -> {
                InventoryTask.swapHand(InventoryTask.mainHandSlot(), Hand.OFF_HAND, false);
                e.cancel();
            }
            default -> {}
        }
    }

    @EventHandler
    public void onSetScreen(SetScreenEvent e) {
        if (e.getScreen() instanceof InventoryScreen && screen != null) {
            e.setScreen(screen);
        }
    }
    
    @EventHandler
    public void onCloseScreen(CloseScreenEvent e) {
        if (e.getScreen() instanceof GenericContainerScreen scr && scr.getTitle().getString().contains(Text.translatable("container.enderchest").getString())) {
            screen = scr;
        }
        if (screen != null) {
            mc.setScreen(null);
            e.cancel();
        }
    }
}
