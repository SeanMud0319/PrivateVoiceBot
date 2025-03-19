package com.nontage.utils;

import com.nontage.voice.VoiceChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.nontage.PrivateVoiceBot.manager;
import static com.nontage.PrivateVoiceBot.config;

public class VoiceTimer {
    private final Timer timer;
    private static final Map<Long, Map<Long, Integer>> voiceChannelTime = new ConcurrentHashMap<>();

    public VoiceTimer() {
        timer = new Timer(true);
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onTick();
            }
        }, 0, 1000);
    }

    public void onTick() {
        voiceChannelTime.forEach((guildId, channels) -> {
            channels.forEach((channelId, time) -> {
                manager.getGuildChannels(guildId).stream()
                        .filter(voiceChannel -> voiceChannel.getChannelId() == channelId)
                        .findFirst()
                        .ifPresent(voiceChannel -> {
                            if (voiceChannel.getDiscordChannel().getMembers().isEmpty()) {
                                channels.put(channelId, time + 1);
                            } else {
                                channels.put(channelId, 0);
                            }
                            if (time >= config.getInt("privateVoiceChannelTimeout")) {
                                voiceChannel.disable(manager);
                                channels.remove(channelId);
                            }
                        });
            });
        });
    }

    public void addChannel(long guildId, VoiceChannel channel) {
        voiceChannelTime.computeIfAbsent(guildId, k -> new ConcurrentHashMap<>())
                .put(channel.getChannelId(), 0);
    }

    public void removeChannel(long guildId, long channelId) {
        Map<Long, Integer> channels = voiceChannelTime.get(guildId);
        if (channels != null) {
            channels.remove(channelId);
            if (channels.isEmpty()) {
                voiceChannelTime.remove(guildId);
            }
        }
    }
}
