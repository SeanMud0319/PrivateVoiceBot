package com.nontage.listeners;

import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.nontage.PrivateVoiceBot.manager;

public class ChannelDelete extends ListenerAdapter {
    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        long channelId = event.getChannel().getIdLong();
        long guildId = event.getGuild().getIdLong();
        manager.getGuildChannels(guildId).stream()
                .filter(channel -> Long.valueOf(channel.getChannelId()).equals(channelId))
                .findFirst()
                .ifPresent(channel -> channel.disable(manager));
    }
}
