package com.insipro.features.impl.movement;


import com.insipro.events.player.EventNoSlow;
import com.insipro.events.player.TickEvent;
import com.insipro.features.module.setting.implement.BooleanSetting;
import com.insipro.utils.client.packet.network.Network;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.interactions.inv.InventoryTask;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;

import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.utils.client.managers.event.types.EventType;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.utils.client.Instance;
import com.insipro.utils.math.time.StopWatch;
import com.insipro.utils.math.script.Script;
import com.insipro.events.item.UsingItemEvent;
import net.minecraft.util.math.BlockPos;

import static com.insipro.utils.display.interfaces.QuickImports.mc;

public class NoSlow extends Module {
    public static NoSlow getInstance() {
        return Instance.get(NoSlow.class);
    }

    private final StopWatch notifWatch = new StopWatch();
    private final Script script = new Script();
    private boolean finish;

    public final SelectSetting itemMode = new SelectSetting("Режим", "Выберите режим обхода").value("Новый Грим", "Старый Грим", "Грим", "Грим тики");
    public final BooleanSetting NoGepl = new BooleanSetting("Не работать на геплы и чарки","").visible(() -> itemMode.isSelected("Грим"));
    public NoSlow() {
        super("NoSlow", "NoSlow", ModuleCategory.MOVEMENT);
        setup(itemMode,NoGepl);
    }

    private int ticks = 0;

    @EventHandler
    public void onUpdate(TickEvent event) {

        if (itemMode.isSelected("Грим")) {
            handleGrimLast();
        }

        if (mc.player.getActiveHand() == Hand.MAIN_HAND || mc.player.getActiveHand() == Hand.OFF_HAND) {
            ticks++;
        } else {
            ticks = 0;
        }
    }


    @EventHandler
    public void onNoslow(EventNoSlow e) {
        if (mc.player == null || !mc.player.isUsingItem()) return;
        Hand first = mc.player.getActiveHand();
        Hand second = first.equals(Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND;



            String mode = itemMode.getSelected();
            ItemStack activeStack = mc.player.getActiveItem();
            switch (mode) {
                case "Старый Грим" -> {
                    if (mc.player.getOffHandStack().getUseAction().equals(UseAction.NONE) || mc.player.getMainHandStack().getUseAction().equals(UseAction.NONE)) {
                        PlayerInteractionHelper.interactItem(first);
                        PlayerInteractionHelper.interactItem(second);
                        e.cancel();
                    }
                }
                case "Новый Грим" -> {
                    if (mc.player.getItemUseTime() < 7) {
                        InventoryTask.updateSlots();
                        InventoryTask.closeScreen(true);
                    } else e.cancel();
                }
                case "Грим тики" -> {

                    if (NoGepl.isValue() && (activeStack.getItem() == Items.GOLDEN_APPLE || activeStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE))
                        return;
                    if (ticks > 1F && mc.player.getItemUseTime() > 1) {
                        e.cancel();
                        ticks = 0;
                    }
                }
                case "Грим" -> {
                    if (NoGepl.isValue() && (activeStack.getItem() == Items.GOLDEN_APPLE || activeStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE))
                        return;
                    if (handleGrimLastNoslow()) {
                        e.cancel();
                    }
                }
            }
        }




    private boolean handleGrimLastNoslow() {
        if (mc.player == null) return false;

        if (mc.player.getActiveHand() == Hand.OFF_HAND) return false;

        int useTime = mc.player.getItemUseTime();

        if (useTime < 4) return false;
        if (useTime > 4) return true;

        return false;
    }

    private void handleGrimLast() {
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        int useTime = mc.player.getItemUseTime();

        if (useTime == 4) {
            mc.getNetworkHandler().sendPacket(
                    new PlayerActionC2SPacket(
                            PlayerActionC2SPacket.Action.DROP_ALL_ITEMS,
                            BlockPos.ORIGIN,
                            mc.player.getHorizontalFacing()
                    )
            );
        }
    }
    }
