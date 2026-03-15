package com.insipro.features.impl.player;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.BooleanSetting;
import com.insipro.features.module.setting.implement.TextSetting;
import com.insipro.common.repository.friend.FriendUtils;
import com.insipro.events.render.TextFactoryEvent;

public class NameProtect extends Module {
    TextSetting nameSetting = new TextSetting("Имя", "").setText("Protected").setMax(16);
    BooleanSetting friendsSetting = new BooleanSetting("Скрывать друзей", "Скрывает никнеймы друзей").setValue(true);

    public NameProtect() {
        super("NameProtect","NameProtect", ModuleCategory.PLAYER);
        setup(nameSetting,friendsSetting);
    }

    @EventHandler
    public void onTextFactory(TextFactoryEvent e) {
        e.replaceText(mc.getSession().getUsername(), nameSetting.getText());
        if (friendsSetting.isValue()) FriendUtils.getFriends().forEach(friend -> e.replaceText(friend.getName(), nameSetting.getText()));
    }
}
