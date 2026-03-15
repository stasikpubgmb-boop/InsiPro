package com.insipro.features.impl.misc;

import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.features.module.setting.implement.SliderSettings;
import com.insipro.utils.client.Instance;

public class ClientSounds extends Module {
    public static ClientSounds getInstance() {
        return Instance.get(ClientSounds.class);
    }

    public SliderSettings volumeSetting = new SliderSettings("Громкость", "Громкость звуков клиента")
            .range(0.0f, 1.0f)
            .setValue(1.0f);
    public SliderSettings pitchSetting = new SliderSettings("Тон", "Тональность (pitch) звуков клиента")
            .range(0.5f, 2.0f)
            .setValue(1.0f);

    public ClientSounds() {
        super("ClientSounds", "ClientSounds", ModuleCategory.MISC);
        setup(volumeSetting, pitchSetting);
    }

    public float getVolume() {
        return volumeSetting.getValue();
    }

    public float getPitch() {
        return pitchSetting.getValue();
    }
}