package com.nontage.listeners;

import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.nontage.PrivateVoiceBot.manager;

public class ChannelUpdateName extends ListenerAdapter {
    @Override
    public void onChannelUpdateName(ChannelUpdateNameEvent event) {
        String newName = event.getNewValue();
        long channelId = event.getChannel().getIdLong();
        long guildId = event.getGuild().getIdLong();
        manager.getGuildChannels(guildId).stream()
                .filter(channel -> Long.valueOf(channel.getChannelId()).equals(channelId))
                .findFirst()
                .ifPresent(channel -> channel.setName(newName));
    }
}
