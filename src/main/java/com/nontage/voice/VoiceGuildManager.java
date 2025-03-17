package com.nontage.voice;

import com.nontage.PrivateVoiceBot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.nontage.PrivateVoiceBot.config;

/**
 * Manages voice channels and users for multiple Discord-like servers (guilds).
 * Each guild's channels and users are kept separate to prevent cross-server interference.
 */
public class VoiceGuildManager {
    private final Map<Long, Set<VoiceChannel>> guildChannels = new HashMap<>();
    private final Map<Long, Set<VoiceUser>> guildUsers = new HashMap<>();

    /**
     * Retrieves the set of voice channels for a specific guild.
     *
     * @param guildId The ID of the guild.
     * @return A set of voice channels in the specified guild.
     */
    public Set<VoiceChannel> getGuildChannels(long guildId) {
        return guildChannels.computeIfAbsent(guildId, k -> new HashSet<>());
    }

    /**
     * Retrieves the set of users for a specific guild.
     *
     * @param guildId The ID of the guild.
     * @return A set of users in the specified guild.
     */
    public Set<VoiceUser> getGuildUsers(long guildId) {
        return guildUsers.computeIfAbsent(guildId, k -> new HashSet<>());
    }

    /**
     * Adds a voice channel to the specified guild.
     *
     * @param guildId The ID of the guild.
     * @param channel The voice channel to add.
     */
    protected void addChannel(long guildId, VoiceChannel channel) {
        getGuildChannels(guildId).add(channel);
    }

    /**
     * Removes a voice channel from the specified guild.
     *
     * @param guildId   The ID of the guild.
     * @param channelId The unique ID of the channel to remove.
     */
    protected void removeChannel(long guildId, long channelId) {
        getGuildChannels(guildId).removeIf(channel -> channel.getChannelId() == channelId);
    }

    /**
     * Retrieves a voice channel by its ID in a specific guild.
     *
     * @param guildId   The ID of the guild.
     * @param channelId The unique ID of the channel.
     * @return The voice channel if found, otherwise `null`.
     */
    public VoiceChannel getChannelById(long guildId, long channelId) {
        return getGuildChannels(guildId).stream()
                .filter(channel -> channel.getChannelId() == channelId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a user to the specified guild.
     *
     * @param guildId The ID of the guild.
     * @param user    The user to add.
     */
    protected void addUser(long guildId, VoiceUser user) {
        getGuildUsers(guildId).add(user);
    }

    /**
     * Removes a user from the specified guild.
     *
     * @param guildId The ID of the guild.
     * @param userId  The unique ID of the user to remove.
     */
    protected void removeUser(long guildId, long userId) {
        getGuildUsers(guildId).removeIf(user -> user.getUserId() == userId);
    }

    /**
     * Retrieves a user by their ID in a specific guild.
     *
     * @param guildId The ID of the guild.
     * @param userId  The unique ID of the user.
     * @return The user if found, otherwise `null`.
     */
    public Optional<VoiceUser> getUserById(long guildId, long userId) {
        return getGuildUsers(guildId).stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();
    }

    /**
     * Retrieves a voice channel by its ID in a specific guild.
     *
     * @param guildId   The ID of the guild.
     * @param channelId The unique ID of the channel.
     * @return The voice channel if found, otherwise `null`.
     */

    public net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel getDiscordVoiceChannelById(long guildId, long channelId) {
        return Objects.requireNonNull(PrivateVoiceBot.jda.getGuildById(guildId)).getVoiceChannelById(channelId);
    }

    public void removeExceptionChannel(long guildId) {
        Long categoryId = config.getMap("privateVoiceCategoryId", Long.class, Long.class).get(guildId);
        if (categoryId != null) {
            Objects.requireNonNull(Objects.requireNonNull(PrivateVoiceBot.jda.getGuildById(guildId))
                            .getCategoryById(categoryId))
                    .getChannels()
                    .forEach(channel -> {
                        if (!config.getMap("privateVoiceChannelId", Long.class, Long.class).containsValue(channel.getIdLong())) {
                            channel.delete().queue();
                            System.out.println("Removed exception channel: " + channel.getName());
                        }
                    });
        }
    }

}
