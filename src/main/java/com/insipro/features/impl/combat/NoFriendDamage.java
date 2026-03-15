package com.insipro.features.impl.combat;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import com.insipro.common.repository.friend.FriendUtils;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.events.player.AttackEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoFriendDamage extends Module {
    public NoFriendDamage() {
        super("NoFriendDamage", "NoFriendDamage", ModuleCategory.COMBAT);
    }

    @EventHandler
    public void onAttack(AttackEvent e) {
        e.setCancelled(FriendUtils.isFriend(e.getEntity()));
    }
}

