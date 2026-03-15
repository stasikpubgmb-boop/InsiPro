/*
package com.insipro.features.impl.movement;

import com.insipro.features.impl.misc.ElytraHelper;
import com.insipro.utils.client.text.TextHelper;
import com.insipro.utils.interactions.inv.InventoryTask;
import com.insipro.utils.math.calc.Calculate;
import com.insipro.utils.math.time.TimerUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.client.chat.ChatMessage;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.events.player.MoveEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.utils.client.Instance;
import com.insipro.utils.math.time.StopWatch;
import org.jetbrains.annotations.Nullable;

import static com.insipro.features.impl.misc.ElytraHelper.getChestPlateSlot;
import static com.insipro.utils.interactions.inv.InventoryToolkit.getItemSlot;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class testfly extends Module {
    public static Fly getInstance() {
        return Instance.get(Fly.class);
    }

    SelectSetting mode = new SelectSetting("Режим", "Выберите режим полета")
            .value("Ванилла", "TP")
            .selected("Ванилла");

    SliderSettings cooldownSetting = new SliderSettings("Кулдаун", "Тиков между телепортами (режим Тест)")
            .setValue(5F).range(1F, 20F);

    @NonFinal
    StopWatch timer = new StopWatch();
    @NonFinal
    int teleportCooldown = 0;
    @NonFinal
    Vec3d LastTeleportPos = Vec3d.ZERO;

    static int ticks;
    private static int oldSlot;

    public testfly() {
        super("testfly", ModuleCategory.MOVEMENT);
        setup(cooldownSetting);
    }

    TimerUtil t = new TimerUtil();

    private void sendTPPacket(Vec3d pos) {
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                pos.x, pos.y, pos.z, true, true));
    }
    @Override
    public void activate() {
        if (mode.isSelected("Ванилла")) {
            mc.player.getAbilities().allowFlying = true;
        }
        super.activate();
    }


   @Override
    public void deadctivate() {
       teleportCooldown = 0;
       lastTeleportPos = Vec3d.ZERO;
       if (mode.isSelected("Ванилла")) {
           mc.player.getAbilities().setFlySpeed(0.05f);
           if (mc.player.getAbilities().flying) {
               mc.player.getAbilities().allowFlying = true;
               mc.player.getAbilities().flying = true;
           } else {
               mc.player.getAbilities().allowFlying = false;
               mc.player.getAbilities().flying = false;
           }
       }
        super.deactivate();
   }
}
*/
