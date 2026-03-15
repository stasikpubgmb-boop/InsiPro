package com.insipro.utils.client.managers.api.command;

import com.insipro.utils.client.managers.api.command.argument.IArgConsumer;
import com.insipro.utils.client.managers.api.command.exception.CommandException;
import com.insipro.utils.display.interfaces.QuickLogger;

import java.util.List;
import java.util.stream.Stream;

public interface ICommand extends QuickLogger {
    void execute(String label, IArgConsumer args) throws CommandException;

    Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException;

    String getShortDesc();

    List<String> getLongDesc();

    List<String> getNames();

    default boolean hiddenFromHelp() {
        return false;
    }
}
