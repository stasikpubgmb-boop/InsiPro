package com.insipro.commands;

import com.insipro.utils.client.managers.api.command.ICommandSystem;
import com.insipro.utils.client.managers.api.command.argparser.IArgParserManager;
import com.insipro.commands.argparser.ArgParserManager;

public enum CommandSystem implements ICommandSystem {
    INSTANCE;

    @Override
    public IArgParserManager getParserManager() {
        return ArgParserManager.INSTANCE;
    }
}
