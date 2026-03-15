package com.insipro.features.impl.player.autoMine.receivers;

import com.insipro.events.packet.PacketEvent;
import com.insipro.events.player.InputEvent;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import com.insipro.events.player.RotationUpdateEvent;
import com.insipro.events.player.TickEvent;
import com.insipro.events.render.DrawEvent;
import com.insipro.events.render.WorldLoadEvent;
import com.insipro.events.render.WorldRenderEvent;
import com.insipro.features.impl.movement.autoMystGlider.parsers.SpookyParser;
import com.insipro.features.impl.player.autoMine.controllers.AutoMineController;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.TextSetting;
import com.insipro.utils.client.managers.event.EventHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author nikitavodolaz
 * @since 08.02.2026
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutoMineModule extends Module {
    AutoMineController controller = new AutoMineController();

    TextSetting anarchyNumber = new TextSetting("Anarchy", "")
            .setText("Anarchy")
            .setMin(3)
            .setMax(4);

    TextSetting homeName = new TextSetting("Home", "")
            .setText("Home")
            .setMax(16)
            .setMin(0);

    public AutoMineModule() {
        super("AutoMine", ModuleCategory.PLAYER);
        setup(anarchyNumber, homeName);
    }

    @EventHandler
    public void tickEventReceive(TickEvent tickEvent) {
        if (SpookyParser.isAnarchy(-1)) return;
        controller.onTick();
    }

    @EventHandler
    public void rotationUpdateEventReceive(RotationUpdateEvent rotationUpdateEvent) {
        if (SpookyParser.isAnarchy(-1)) return;
        controller.onRotationUpdate(rotationUpdateEvent);
    }

    @EventHandler
    public void worldChangeEventReceive(WorldLoadEvent worldLoadEvent) {
        if (SpookyParser.isAnarchy(-1)) return;
        if (controller != null) controller.onWorldChange();
    }

    @EventHandler
    public void packetEventReceive(PacketEvent packetEvent) {
        if (SpookyParser.isAnarchy(-1)) return;
        if (packetEvent.getType() != PacketEvent.Type.RECEIVE) return;
        if (mc.player == null || controller == null) return;

        if (packetEvent.getPacket() instanceof GameMessageS2CPacket gameMessage) {
            var content = gameMessage.content();
            if (content == null) return;
            String message = content.getString();
            if (message != null && (message.contains("Вы уже подключены на этот сервер!") || message.contains("Вы уже подключены к этому серверу!"))) {
                controller.notifyAlreadyConnected();
            }
        }
    }

    @EventHandler
    public void drawEventReceive(DrawEvent drawEvent) {
        if (SpookyParser.isAnarchy(-1)) return;

        controller.onDraw(drawEvent);
    }

    @EventHandler
    public void inputEventReceive(InputEvent inputEvent) {
        if (SpookyParser.isAnarchy(-1)) return;

        controller.onInput(inputEvent);
    }

    @EventHandler
    public void worldRenderEventReceive(WorldRenderEvent worldRenderEvent) {
        if (SpookyParser.isAnarchy(-1)) return;

        controller.onWorldRender(worldRenderEvent);
    }

}
