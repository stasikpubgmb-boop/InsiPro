package com.insipro.features.impl.render;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.MultiSelectSetting;
import com.insipro.features.module.setting.implement.SelectSetting;
import com.insipro.utils.display.render.geometry.Render3D;
import com.insipro.events.block.BlockUpdateEvent;
import com.insipro.events.render.WorldLoadEvent;
import com.insipro.events.render.WorldRenderEvent;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class XRay extends Module {
    Map<BlockPos, BlockState> map = new HashMap<>();

    SelectSetting modeSetting = new SelectSetting("Режим", "Режим работы для XRay").value("Обновление блоков");
    MultiSelectSetting blockTypeSetting = new MultiSelectSetting("Блоки", "Блоки, которые будут отображаться")
            .value("Древние обломки", "Алмазы", "Изумруды", "Железо", "Золото");

    public XRay() {
        super("XRay", ModuleCategory.RENDER);
        setup(modeSetting, blockTypeSetting);
    }

    @EventHandler
    public void onWorldRender(WorldRenderEvent e) {
        map.forEach((key, value) -> {
            if (blockTypeSetting.getSelected().toString().toLowerCase().contains(getBlockName(value))) {
                Render3D.drawBox(new Box(key), getColorByBlock(value), 1);
            }
        });
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        map.clear();
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent e) {
        BlockState state = e.state();
        BlockPos pos = e.pos();
        switch (e.type()) {
            case BlockUpdateEvent.Type.UPDATE -> {
                if (getColorByBlock(state) != -1 && !map.containsKey(pos)) map.put(pos, state);
                if (map.containsKey(pos) && !map.get(pos).equals(state)) map.remove(pos);
            }
            case BlockUpdateEvent.Type.UNLOAD -> map.remove(pos);
        }
    }
    
    private int getColorByBlock(BlockState block) {
        return switch (getBlockName(block)) {
            case "Древние обломки" -> 0xFFA67554;
            case "Алмазы" -> 0xFF197B81;
            case "Изумруды" -> 0xFF41871B;
            case "Железо" -> 0xFF754C1F;
            case "Золото" -> 0xFFC5B938;
            default -> -1;
        };
    }

    private String getBlockName(BlockState state) {
        return state.getBlock().asItem().toString().replace("minecraft:", "").replace("_ore", "").replace("_", " ");
    }
}
