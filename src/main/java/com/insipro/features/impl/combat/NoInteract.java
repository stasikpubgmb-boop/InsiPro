package com.insipro.features.impl.combat;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import com.insipro.features.module.Module;
import com.insipro.features.module.ModuleCategory;
import com.insipro.utils.client.Instance;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoInteract extends Module {
    public static NoInteract getInstance() {
        return Instance.get(NoInteract.class);
    }

    public NoInteract() {
        super("NoInteract", "NoInteract", ModuleCategory.COMBAT);
    }
}
