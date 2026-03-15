

package com.insipro.commands;

import net.minecraft.util.Pair;
import com.insipro.Essence;
import com.insipro.utils.client.managers.api.command.argument.ICommandArgument;
import com.insipro.utils.client.managers.api.command.exception.CommandNotEnoughArgumentsException;
import com.insipro.utils.client.managers.api.command.exception.CommandNotFoundException;
import com.insipro.utils.client.managers.api.command.helpers.TabCompleteHelper;
import com.insipro.utils.client.managers.api.command.manager.ICommandManager;
import com.insipro.utils.client.managers.event.EventManager;
import com.insipro.utils.client.managers.event.EventHandler;
import com.insipro.events.chat.ChatEvent;
import com.insipro.events.chat.TabCompleteEvent;
import com.insipro.utils.display.interfaces.QuickLogger;
import com.insipro.commands.argument.ArgConsumer;
import com.insipro.commands.argument.CommandArguments;
import com.insipro.commands.manager.CommandRepository;
import java.util.List;
import java.util.stream.Stream;

import static com.insipro.utils.client.managers.api.command.IBaritoneChatControl.FORCE_COMMAND_PREFIX;

public class CommandDispatcher implements QuickLogger {
    private final ICommandManager manager;
    public static String prefix = ".";

    public CommandDispatcher(EventManager eventManager) {
        this.manager = Essence.getInstance().getCommandRepository();
        eventManager.register(this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        String msg = event.getMessage();

        boolean forceRun = msg.startsWith(FORCE_COMMAND_PREFIX);
        if ((msg.startsWith(prefix)) || forceRun) {
            event.cancel();
            String commandStr = msg.substring(forceRun ? FORCE_COMMAND_PREFIX.length() : prefix.length());
            if (!runCommand(commandStr) && !commandStr.trim().isEmpty()) {
                new CommandNotFoundException(CommandRepository.expand(commandStr).getLeft()).handle(null, null);
            }
        } else if (runCommand(msg)) {
            event.cancel();
        }
    }

    public boolean runCommand(String msg) {
        if (msg.isEmpty()) {
            return this.runCommand("help");
        }
        Pair<String, List<ICommandArgument>> pair = CommandRepository.expand(msg);
        String command = pair.getLeft();
        String rest = msg.substring(pair.getLeft().length());
        ArgConsumer argc = new ArgConsumer(this.manager, pair.getRight());
       

        return this.manager.execute(pair);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String eventPrefix = event.prefix;
        if (!eventPrefix.startsWith(prefix)) {
            return;
        }

        String msg = eventPrefix.substring(prefix.length());
        List<ICommandArgument> args = CommandArguments.from(msg, true);
        Stream<String> stream = tabComplete(msg);
        if (args.size() == 1) {
            stream = stream.map(x -> prefix + x);
        }
        event.completions = stream.toArray(String[]::new);
    }

    public Stream<String> tabComplete(String msg) {
        try {
            List<ICommandArgument> args = CommandArguments.from(msg, true);
            ArgConsumer argc = new ArgConsumer(this.manager, args);
            if (argc.hasAtMost(2)) {
                if (argc.hasExactly(1)) {
                    return new TabCompleteHelper()
                            .addCommands(this.manager)
                            .filterPrefix(argc.getString())
                            .stream();
                }
          
            }
            return this.manager.tabComplete(msg);
        } catch (CommandNotEnoughArgumentsException ignored) { 
            return Stream.empty();
        }
    }
}
