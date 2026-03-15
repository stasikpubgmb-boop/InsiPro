package com.insipro.utils.client.managers.api.command.exception;

import com.insipro.utils.client.managers.api.command.ICommand;
import com.insipro.utils.client.managers.api.command.argument.ICommandArgument;
import com.insipro.utils.display.interfaces.QuickLogger;

import java.util.List;

public class CommandUnhandledException extends RuntimeException implements ICommandException, QuickLogger {

    public CommandUnhandledException(String message) {
        super(message);
    }

    public CommandUnhandledException(Throwable cause) {
        super(cause);
    }

    @Override
    public void handle(ICommand command, List<ICommandArgument> args) {
    }
}
