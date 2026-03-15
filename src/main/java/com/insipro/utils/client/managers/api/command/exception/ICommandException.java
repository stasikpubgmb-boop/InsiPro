package com.insipro.utils.client.managers.api.command.exception;

import net.minecraft.util.Formatting;
import com.insipro.utils.client.managers.api.command.ICommand;
import com.insipro.utils.client.managers.api.command.argument.ICommandArgument;
import com.insipro.utils.display.interfaces.QuickLogger;

import java.util.List;

public interface ICommandException extends QuickLogger {

    String getMessage();

    default void handle(ICommand command, List<ICommandArgument> args) {
        logDirect(
                this.getMessage(),
                Formatting.RED
        );
    }
}
