package com.insipro.utils.client.managers.api.command;

import com.insipro.utils.client.managers.api.command.argparser.IArgParserManager;

public interface ICommandSystem {
    IArgParserManager getParserManager();
}
