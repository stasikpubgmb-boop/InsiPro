package com.insipro.utils.client.managers.api.command.datatypes;

import com.insipro.utils.client.managers.api.command.exception.CommandException;
import com.insipro.utils.client.managers.api.command.helpers.TabCompleteHelper;
import com.insipro.common.repository.way.Way;
import com.insipro.Essence;

import java.util.List;
import java.util.stream.Stream;

public enum WayDataType implements IDatatypeFor<Way> {
    INSTANCE;

    @Override
    public Stream<String> tabComplete(IDatatypeContext datatypeContext) throws CommandException {
        Stream<String> ways = getWay().stream().map(Way::name);
        String context = datatypeContext.getConsumer().getString();
        return new TabCompleteHelper().append(ways).filterPrefix(context).sortAlphabetically().stream();
    }

    @Override
    public Way get(IDatatypeContext datatypeContext) throws CommandException {
        String text = datatypeContext.getConsumer().getString();
        return getWay().stream().filter(s -> s.name().equalsIgnoreCase(text)).findFirst().orElse(null);
    }

    private List<? extends Way> getWay() {
        return Essence.getInstance().getWayRepository().wayList;
    }
}
