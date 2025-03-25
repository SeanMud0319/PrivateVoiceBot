package com.nontage.voice;

import com.nontage.PrivateVoiceBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.nontage.PrivateVoiceBot.config;

/**
 * Represents a user in a Discord-like server.
 * Each user has a unique ID, a username, and belongs to a specific guild (server).
 */
public class VoiceUser {
    private final Member member;
    private final long userId;
    private final long guildId;
    private final VoiceGuildManager manager;

    /**
     * Creates a new user and registers them with the {@link VoiceGuildManager}.
     *
     * @param member  The {@link Member} object representing the user.
     * @param guildId The ID of the guild the user belongs to.
     * @param manager The {@link VoiceGuildManager} managing the user.
     */
    public VoiceUser(Member member, long guildId, VoiceGuildManager manager) {
        this.member = member;
        this.userId = member.getIdLong();
        this.guildId = guildId;
        this.manager = manager;
        manager.addUser(guildId, this);
    }

    /**
     * @return The {@link Member} object representing this user.
     */
    public Member getMember() {
        return member;
    }

    /**
     * @return The unique ID of this user.
     */
    public long getUserId() {
        return userId;
    }


    /**
     * @return The ID of the guild (server) this user belongs to.
     */
    public long getGuildId() {
        return guildId;
    }

    /**
     * Creates a new voice channel owned by this user.
     *
     * @param channelName The name of the new channel.
     */
    public void createChannel(String channelName) {
        Long categoryId = config.getMap("privateVoiceCategoryId", Long.class, Long.class).get(this.guildId);
        if (categoryId != null) {
            String fullChannelName = config.getString("privateVoiceChannelPrefix") + channelName + config.getString("privateVoiceChannelSuffix");
            if (fullChannelName.length() > 25) {
                fullChannelName = fullChannelName.substring(0, 25);
            }
            Objects.requireNonNull(Objects.requireNonNull(PrivateVoiceBot.jda.getGuildById(guildId))
                            .getCategoryById(categoryId))
                    .createVoiceChannel(fullChannelName)
                    .queue(voiceChannel -> {
                        VoiceChannel newChannel = new VoiceChannel(voiceChannel.getIdLong(), this, channelName, voiceChannel);
                        manager.addChannel(guildId, newChannel);
                        if (this.member.getVoiceState() != null && this.member.getVoiceState().inAudioChannel()) {
                            voiceChannel.getGuild().moveVoiceMember(this.member, voiceChannel).queue();
                        }
                        voiceChannel.getGuild().moveVoiceMember(this.member, voiceChannel).queue();
                        voiceChannel.getManager()
                                .putMemberPermissionOverride(member.getIdLong(),
                                        List.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT),
                                        new HashSet<>()).queue();
                    });
        }

    }

    /**
     * Retrieves a channel owned by this user, if it exists.
     *
     * @return The voice channel if it exists, otherwise `null`.
     */
    public VoiceChannel getSelfVoiceChannel() {
        for (VoiceChannel channel : manager.getGuildChannels(guildId)) {
            if (channel.getOwner().getUserId() == userId) {
                return channel;
            }
        }
        return null;
    }

    /**
     * Removes the user from the guild manager.
     */
    public void leave() {
        manager.removeUser(guildId, userId);
    }
}
