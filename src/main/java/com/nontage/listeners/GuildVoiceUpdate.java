package com.nontage.listeners;

import com.nontage.PrivateVoiceBot;
import com.nontage.voice.VoiceUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.nontage.PrivateVoiceBot.manager;

public class GuildVoiceUpdate extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (member.getUser().isBot()) {
            return;
        }
        VoiceUser voiceUser;
        var optionalVoiceUser = manager.getUserById(guild.getIdLong(), member.getIdLong());
        voiceUser = optionalVoiceUser.orElseGet(() -> new VoiceUser(member, guild.getIdLong(), manager));
        var privateVoiceChannelMap = PrivateVoiceBot.config.getMap("privateVoiceChannelId", Long.class, Long.class);
        Long privateVoiceChannelId = privateVoiceChannelMap.get(guild.getIdLong());
        if (event.getChannelJoined() != null && event.getChannelJoined().getIdLong() == privateVoiceChannelId) {
            if (voiceUser.getSelfVoiceChannel() != null) {
                VoiceChannel channel = manager.getDiscordVoiceChannelById(guild.getIdLong(), voiceUser.getSelfVoiceChannel().getChannelId());
                guild.moveVoiceMember(member, channel).queue();
                return;
            }
            voiceUser.createChannel(member.getEffectiveName());
        }
    }
}
