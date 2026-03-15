package com.insipro.display.screens.clickgui.components.implement.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.insipro.features.module.setting.Setting;
import com.insipro.features.module.Module;
import com.insipro.display.screens.clickgui.components.AbstractComponent;

@Getter
@RequiredArgsConstructor
public abstract class AbstractSettingComponent extends AbstractComponent {
    private final Setting setting;
    protected Module module;
    
    public void setModule(Module module) {
        this.module = module;
    }
}
