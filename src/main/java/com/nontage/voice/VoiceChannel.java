package com.nontage.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nontage.PrivateVoiceBot.jda;
import static com.nontage.PrivateVoiceBot.manager;
import static com.nontage.PrivateVoiceBot.voiceTimer;
import static com.nontage.PrivateVoiceBot.config;

/**
 * Represents a voice channel in a Discord-like server.
 * Each channel has a unique ID, a name, an owner, and visibility settings (public or private).
 * This class does not manage channels globally; instead, channels are managed through {@link VoiceGuildManager}.
 */
public class VoiceChannel {
    private final long channelId;
    private final long guildId;
    private final Set<VoiceUser> invitedUsers;
    private final net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel discordChannel;
    private boolean isPrivate;
    private String name;
    private VoiceUser owner;

    /**
     * Creates a new public voice channel.
     *
     * @param channelId      The unique ID of the channel.
     * @param owner          The owner of the channel.
     * @param name           The name of the channel.
     * @param discordChannel The Discord {@link net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel} object representing this channel.
     */
    public VoiceChannel(long channelId, VoiceUser owner, String name, net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel discordChannel) {
        this.channelId = channelId;
        this.guildId = owner.getGuildId();
        this.owner = owner;
        this.name = name;
        this.discordChannel = discordChannel;
        this.isPrivate = config.getBoolean("defaultChannelVisibility");
        this.invitedUsers = new HashSet<>();
        voiceTimer.addChannel(guildId, this);
    }

    /**
     * @return The unique ID of this channel.
     */
    public long getChannelId() {
        return channelId;
    }

    /**
     * @return The owner of this channel.
     */
    public VoiceUser getOwner() {
        return owner;
    }

    /**
     * Transfers channel ownership to a new user, but only if the user is in the same guild.
     *
     * @param newOwner The new owner of the channel.
     */
    public void transferOwner(VoiceUser newOwner) {
        if (newOwner.getGuildId() == this.guildId) {
            VoiceChannelManager voiceChannelManager = manager.getDiscordVoiceChannelById(guildId, channelId).getManager();
            inviteUser(owner);
            voiceChannelManager.putMemberPermissionOverride(newOwner.getUserId(), List.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT), new HashSet<>()).queue();
            this.owner = newOwner;
        }
    }

    /**
     * @return The name of this channel.
     */
    public String getName() {
        return name;
    }

    /**
     * Renames the channel.
     */
    public void rename(String newName) {
        this.name = newName;
        var discordChannel = manager.getDiscordVoiceChannelById(guildId, channelId);
        if (discordChannel != null) {
            discordChannel.getManager().setName(newName).queue();
        } else {
            System.err.println("Channel rename failed: Channel not found for ID " + channelId);
        }
    }

    /**
     * Sets the name of this channel class.
     *
     * @param name The new name of the channel.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return Whether this channel is private.
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Toggles the channel visibility (public/private).
     */
    public void toggleVisibility() {
        isPrivate = !isPrivate;
        var discordChannel = manager.getDiscordVoiceChannelById(guildId, channelId);
        if (isPrivate) {
            discordChannel.getManager().putPermissionOverride(jda.getGuildById(guildId).getPublicRole(), new HashSet<>(), List.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT)).queue();
        } else {
            discordChannel.getManager().putPermissionOverride(jda.getGuildById(guildId).getPublicRole(), List.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT), new HashSet<>()).queue();
        }
    }

    /**
     * Invites a user to the channel. Only users from the same guild can be invited.
     *
     * @param user The user to invite.
     */
    public void inviteUser(VoiceUser user) {
        if (user.getGuildId() == this.guildId) {
            invitedUsers.add(user);
            VoiceChannelManager voiceChannelManager = manager.getDiscordVoiceChannelById(guildId, channelId).getManager();
            List<Permission> permissions = List.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT);
            voiceChannelManager.putMemberPermissionOverride(user.getUserId(), permissions, new HashSet<>()).queue();
        }
    }

    /**
     * Removes a user from the channel.
     *
     * @param user The user to remove.
     */
    public void kickUser(VoiceUser user) {
        if (user.getGuildId() != this.guildId) return;
        invitedUsers.remove(user);
        var discordChannel = manager.getDiscordVoiceChannelById(guildId, channelId);
        if (discordChannel == null) return;
        discordChannel.getManager().removePermissionOverride(user.getUserId()).queue();
        Member member = user.getMember();
        if (member != null && member.getVoiceState() != null) {
            var voiceState = member.getVoiceState();
            if (voiceState.getChannel() != null && voiceState.getChannel().getIdLong() == channelId) {
                member.getGuild().kickVoiceMember(member).queue();
            }
        }
    }


    /**
     * @return The list of invited users for this channel.
     */
    public Set<VoiceUser> getInvitedUsers() {
        return invitedUsers;
    }

    /**
     * @return The Discord {@link net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel} object representing this channel.
     */
    public net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel getDiscordChannel() {
        return discordChannel;
    }

    /**
     * Disables this channel and removes it from the manager.
     *
     * @param manager The {@link VoiceGuildManager} managing this channel.
     */
    public void disable(VoiceGuildManager manager) {
        try {
            manager.getDiscordVoiceChannelById(guildId, channelId).delete().queue();
        } catch (Exception e) {
            System.err.println("Failed to delete channel: " + e.getMessage());
            System.out.println("If you manually delete the channel, please ignore this message.");
        }
        voiceTimer.removeChannel(guildId, channelId);
        manager.removeChannel(guildId, channelId);
    }
}
