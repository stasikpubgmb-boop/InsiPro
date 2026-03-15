package com.insipro.common.logger.implement;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import com.insipro.common.logger.Logger;
import com.insipro.utils.display.interfaces.QuickImports;

import java.util.Arrays;

public class MinecraftLogger implements Logger, QuickImports {
    @Override
    public void log(Object message) {

    }

    @Override
    
    public void minecraftLog(Text... components) {
        if (mc.player != null) {
            MutableText component = Text.literal("");
            Arrays.asList(components).forEach(component::append);
            mc.inGameHud.getChatHud().addMessage(component);
        }
    }
}
