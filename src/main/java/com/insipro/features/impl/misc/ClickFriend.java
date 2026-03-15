package com.insipro.features.impl.misc;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.BindSetting;
import com.insipro.common.repository.friend.FriendUtils;
import com.insipro.events.keyboard.KeyEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClickFriend extends Module {

    BindSetting friendBind = new BindSetting("Добавить друга", "Добавить/удалить друга");

    public ClickFriend() {
        super("ClickFriend", "Click Friend", ModuleCategory.MISC);
        setup(friendBind);
    }

    @EventHandler
    public void onKey(KeyEvent e) {
        if (e.isKeyDown(friendBind.getKey()) && mc.crosshairTarget instanceof EntityHitResult result && result.getEntity() instanceof PlayerEntity player) {
            if (FriendUtils.isFriend(player)) FriendUtils.removeFriend(player);
            else FriendUtils.addFriend(player);
        }
    }
}