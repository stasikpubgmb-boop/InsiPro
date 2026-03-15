package com.insipro.common.discord.callbacks;

import com.sun.jna.Callback;
import com.insipro.common.discord.utils.DiscordUser;

public interface ReadyCallback extends Callback {
    void apply(DiscordUser var1);
}