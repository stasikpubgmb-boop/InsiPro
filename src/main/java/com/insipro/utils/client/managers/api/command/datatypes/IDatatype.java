package com.insipro.utils.client.managers.api.command.datatypes;

import com.insipro.utils.client.managers.api.command.exception.CommandException;
import com.insipro.utils.display.interfaces.QuickImports;

import java.util.stream.Stream;

public interface IDatatype extends QuickImports {
    Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException;
}
