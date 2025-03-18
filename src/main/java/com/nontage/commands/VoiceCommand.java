package com.nontage.commands;

import com.nontage.utils.SlashCommand;
import com.nontage.utils.TextUtils;
import com.nontage.voice.VoiceUser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.nontage.PrivateVoiceBot.config;
import static com.nontage.PrivateVoiceBot.manager;

//TODO ALL METHODS NEED TO CHECK, EXPECT ADMIN COMMANDS
public class VoiceCommand extends SlashCommand {
    public VoiceCommand() {
        super(Commands.slash("voice", "語音指令")
                .addSubcommands(
                        new SubcommandData(VoiceCommands.SET_CREATE_CHANNEL.getName(), VoiceCommands.SET_CREATE_CHANNEL.getDescription())
                                .addOption(OptionType.CHANNEL, "channel", "指定頻道", true),
                        new SubcommandData(VoiceCommands.SET_CREATE_CATEGORY.getName(), VoiceCommands.SET_CREATE_CATEGORY.getDescription())
                                .addOption(OptionType.CHANNEL, "category", "指定類別", true),

                        new SubcommandData(VoiceCommands.INVITE.getName(), VoiceCommands.INVITE.getDescription())
                                .addOption(OptionType.USER, "user", "指定用戶", true),
                        new SubcommandData(VoiceCommands.KICK.getName(), VoiceCommands.KICK.getDescription())
                                .addOption(OptionType.USER, "user", "指定用戶", true),
                        new SubcommandData(VoiceCommands.RENAME.getName(), VoiceCommands.RENAME.getDescription())
                                .addOption(OptionType.STRING, "name", "新名稱", true),
                        new SubcommandData(VoiceCommands.TRANSFER.getName(), VoiceCommands.TRANSFER.getDescription())
                                .addOption(OptionType.USER, "user", "指定用戶", true),

                        new SubcommandData(VoiceCommands.INFO.getName(), VoiceCommands.INFO.getDescription()),
                        new SubcommandData(VoiceCommands.TOGGLE_VISIBILITY.getName(), VoiceCommands.TOGGLE_VISIBILITY.getDescription()),
                        new SubcommandData(VoiceCommands.CLOSE.getName(), VoiceCommands.CLOSE.getDescription())
                )
        );
    }

    @Override
    public void execute(Member sender, SlashCommandInteractionEvent event, String subCommand) {
        long guildId = Objects.requireNonNull(event.getGuild()).getIdLong();
        Optional<VoiceUser> senderUserOpt = manager.getUserById(guildId, sender.getIdLong());
        switch (VoiceCommands.fromName(subCommand)) {
            case SET_CREATE_CHANNEL -> {
                if (!sender.hasPermission(Permission.ADMINISTRATOR)) {
                    event.replyEmbeds(TextUtils.getNoPermissionEmbed().build()).setEphemeral(true).queue();
                    return;
                }
                try {
                    VoiceChannel channel = Objects.requireNonNull(event.getOption("channel")).getAsChannel().asVoiceChannel();
                    channel.getManager()
                            .setUserLimit(1)
                            .setName(config.getString("privateVoiceChannelName"))
                            .putPermissionOverride(event.getGuild().getPublicRole(), List.of(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT), new HashSet<>())
                            .queue();
                    long channelId = channel.getIdLong();
                    config.setMap("privateVoiceChannelId", Map.of(guildId, channelId));
                    config.save();
                    config.reload();
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("successColor")))
                            .setTitle(config.getString("successTitle"))
                            .setDescription(config.getString("commandSetCreateChannelSuccess"))
                            .build()).setEphemeral(true).queue();
                } catch (Exception e) {
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("errorColor")))
                            .setTitle(config.getString("errorTitle"))
                            .setDescription(config.getString("commandSetCreateChannelError"))
                            .build()).setEphemeral(true).queue();
                }
            }
            case SET_CREATE_CATEGORY -> {
                if (!sender.hasPermission(Permission.ADMINISTRATOR)) {
                    event.replyEmbeds(TextUtils.getNoPermissionEmbed().build()).setEphemeral(true).queue();
                    return;
                }
                try {
                    Category category = Objects.requireNonNull(event.getOption("category")).getAsChannel().asCategory();
                    PermissionOverride permissionOverride = category.getPermissionOverride(sender);
                    if (permissionOverride == null) {
                        permissionOverride = category.getPermissionContainer().upsertPermissionOverride(event.getGuild().getPublicRole()).complete();
                    }
                    permissionOverride.getManager().setDenied(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT).queue();
                    long categoryId = category.getIdLong();
                    config.setMap("privateVoiceCategoryId", Map.of(guildId, categoryId));
                    config.save();
                    config.reload();
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("successColor")))
                            .setTitle(config.getString("successTitle"))
                            .setDescription(config.getString("commandSetCreateCategorySuccess"))
                            .build()).setEphemeral(true).queue();
                } catch (Exception e) {
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("errorColor")))
                            .setTitle(config.getString("errorTitle"))
                            .setDescription(config.getString("commandSetCreateCategoryError"))
                            .build()).setEphemeral(true).queue();
                }
            }
            case INFO -> {
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    var discordChannel = manager.getDiscordVoiceChannelById(guildId, voiceUser.getSelfVoiceChannel().getChannelId());
                    int memberCount = discordChannel.getMembers().size();
                    //currently count +1 bcz owner is not in the list
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.CYAN)
                            .setTitle(sender.getEffectiveName() + "的頻道資訊")
                            .setDescription("頻道名稱: " + voiceUser.getSelfVoiceChannel().getName() + "\n" +
                                    "頻道所有者: " + voiceUser.getSelfVoiceChannel().getOwner().getMember().getAsMention() + "\n" +
                                    "頻道類型: " + (voiceUser.getSelfVoiceChannel().isPrivate() ? "私人" : "公開") + "\n" +
                                    "當前人數: " + (memberCount + "/" + (voiceUser.getSelfVoiceChannel().getInvitedUsers().size() + 1)) + "\n" +
                                    "邀請列表: " + voiceUser.getSelfVoiceChannel().getInvitedUsers().stream()
                                    .map(user -> user.getMember().getAsMention())
                                    .reduce((a, b) -> a + ", " + b).orElse("無"))
                            .build()).setEphemeral(true).queue();
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
            case INVITE -> {
                User target = Objects.requireNonNull(event.getOption("user")).getAsUser();
                if (isBot(target, event)) {
                    return;
                }
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    if (senderIsTarget(sender.getIdLong(), target.getIdLong(), event)) {
                        return;
                    }
                    Optional<VoiceUser> targetUserOpt = manager.getUserById(guildId, target.getIdLong());
                    if (targetUserOpt.isPresent() && voiceUser.getSelfVoiceChannel().getInvitedUsers().contains(targetUserOpt.get())) {
                        event.replyEmbeds(TextUtils.getGlobalEmbed()
                                .setColor(Color.decode(config.getString("errorColor")))
                                .setTitle(config.getString("errorTitle"))
                                .setDescription(config.getString("commandInviteAlreadyInvitedError"))
                                .build()).setEphemeral(true).queue();
                        return;
                    }
                    targetUserOpt.ifPresentOrElse(targetUser -> {
                                voiceUser.getSelfVoiceChannel().inviteUser(targetUser);
                                event.replyEmbeds(TextUtils.getGlobalEmbed()
                                        .setColor(Color.decode(config.getString("successColor")))
                                        .setTitle(config.getString("successTitle"))
                                        .setDescription(config.getString("commandInviteSuccess"))
                                        .build()).setEphemeral(true).queue();
                            }
                            , () -> {
                                voiceUser.getSelfVoiceChannel().inviteUser(new VoiceUser(Objects.requireNonNull(event.getGuild().getMember(target)), guildId, manager));
                                event.replyEmbeds(TextUtils.getGlobalEmbed()
                                        .setColor(Color.decode(config.getString("successColor")))
                                        .setTitle(config.getString("successTitle"))
                                        .setDescription(config.getString("commandInviteSuccess"))
                                        .build()).setEphemeral(true).queue();
                            });
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
            case KICK -> {
                User target = Objects.requireNonNull(event.getOption("user")).getAsUser();
                if (isBot(target, event)) {
                    return;
                }
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    if (!voiceUser.getSelfVoiceChannel().isPrivate()) {
                        event.replyEmbeds(TextUtils.getGlobalEmbed()
                                .setColor(Color.decode(config.getString("errorColor")))
                                .setTitle(config.getString("errorTitle"))
                                .setDescription(config.getString("commandKickPublicError"))
                                .build()).setEphemeral(true).queue();
                        return;
                    }
                    if (senderIsTarget(sender.getIdLong(), target.getIdLong(), event)) {
                        return;
                    }
                    Optional<VoiceUser> targetUserOpt = manager.getUserById(guildId, target.getIdLong());
                    if (targetUserOpt.isEmpty() || !voiceUser.getSelfVoiceChannel().getInvitedUsers().contains(targetUserOpt.get())) {
                        event.replyEmbeds(TextUtils.getGlobalEmbed()
                                .setColor(Color.decode(config.getString("errorColor")))
                                .setTitle(config.getString("errorTitle"))
                                .setDescription(config.getString("commandKickNotInvitedError"))
                                .build()).setEphemeral(true).queue();
                        return;
                    }
                    targetUserOpt.ifPresent(targetUser -> {
                        voiceUser.getSelfVoiceChannel().kickUser(targetUser);
                        event.replyEmbeds(TextUtils.getGlobalEmbed()
                                .setColor(Color.decode(config.getString("successColor")))
                                .setTitle(config.getString("successTitle"))
                                .setDescription(config.getString("commandKickSuccess"))
                                .build()).setEphemeral(true).queue();
                    });
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
            case RENAME -> {
                String newName = Objects.requireNonNull(event.getOption("name")).getAsString();
                if (newName.length() > 25) {
                    newName = newName.substring(0, 25);
                }
                String finalNewName = newName;
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    voiceUser.getSelfVoiceChannel().rename(finalNewName);
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("successColor")))
                            .setTitle(config.getString("successTitle"))
                            .setDescription(config.getString("commandRenameSuccess"))
                            .build()).setEphemeral(true).queue();
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
            case TRANSFER -> {
                User target = Objects.requireNonNull(event.getOption("user")).getAsUser();
                if (isBot(target, event)) {
                    return;
                }
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    if (senderIsTarget(sender.getIdLong(), target.getIdLong(), event)) {
                        return;
                    }
                    manager.getUserById(guildId, target.getIdLong()).ifPresentOrElse(targetUser -> {
                        if (targetUser.getSelfVoiceChannel() != null) {
                            event.replyEmbeds(TextUtils.getGlobalEmbed()
                                    .setColor(Color.decode(config.getString("errorColor")))
                                    .setTitle(config.getString("errorTitle"))
                                    .setDescription(config.getString("commandTransferTargetError"))
                                    .build()).setEphemeral(true).queue();
                            return;
                        }
                        voiceUser.getSelfVoiceChannel().transferOwner(targetUser);
                        event.replyEmbeds(TextUtils.getGlobalEmbed()
                                .setColor(Color.decode(config.getString("successColor")))
                                .setTitle(config.getString("successTitle"))
                                .setDescription(config.getString("commandTransferSuccess"))
                                .build()).setEphemeral(true).queue();
                    }, () -> {
                        voiceUser.getSelfVoiceChannel().transferOwner(new VoiceUser(Objects.requireNonNull(event.getGuild().getMember(target)), guildId, manager));
                        event.replyEmbeds(TextUtils.getGlobalEmbed()
                                .setColor(Color.decode(config.getString("successColor")))
                                .setTitle(config.getString("successTitle"))
                                .setDescription(config.getString("commandTransferSuccess"))
                                .build()).setEphemeral(true).queue();
                    });
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
            case TOGGLE_VISIBILITY -> {
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    voiceUser.getSelfVoiceChannel().toggleVisibility();
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("successColor")))
                            .setTitle(config.getString("successTitle"))
                            .setDescription(config.getString("commandToggleVisibilitySuccess"))
                            .build()).setEphemeral(true).queue();
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
            case CLOSE -> {
                senderUserOpt.ifPresentOrElse(voiceUser -> {
                    if (noChannel(voiceUser, event)) {
                        return;
                    }
                    voiceUser.getSelfVoiceChannel().disable(manager);
                    voiceUser.leave();
                    event.replyEmbeds(TextUtils.getGlobalEmbed()
                            .setColor(Color.decode(config.getString("successColor")))
                            .setTitle(config.getString("successTitle"))
                            .setDescription(config.getString("commandCloseSuccess"))
                            .build()).setEphemeral(true).queue();
                }, () -> {
                    event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
                });
            }
        }
    }

    private static boolean isBot(User user, SlashCommandInteractionEvent event) {
        if (user.isBot()) {
            event.replyEmbeds(TextUtils.getGlobalEmbed()
                    .setColor(Color.decode(config.getString("errorColor")))
                    .setTitle(config.getString("errorTitle"))
                    .setDescription(config.getString("commandToBotError"))
                    .build()).setEphemeral(true).queue();
            return true;
        }
        return false;
    }

    private static boolean noChannel(VoiceUser voiceUser, SlashCommandInteractionEvent event) {
        if (voiceUser.getSelfVoiceChannel() == null) {
            event.replyEmbeds(TextUtils.getNoVoiceChannelEmbed().build()).setEphemeral(true).queue();
            return true;
        }
        return false;
    }

    private static boolean senderIsTarget(long senderId, long targetId, SlashCommandInteractionEvent event) {
        if (senderId == targetId) {
            event.replyEmbeds(TextUtils.getGlobalEmbed()
                    .setColor(Color.decode(config.getString("errorColor")))
                    .setTitle(config.getString("errorTitle"))
                    .setDescription(config.getString("commandSelfError"))
                    .build()).setEphemeral(true).queue();
            return true;
        }
        return false;
    }
}

enum VoiceCommands {
    // Administrator commands
    SET_CREATE_CHANNEL("setcreatechannel", "將指定頻道設為創建頻道"),
    SET_CREATE_CATEGORY("setcreatecategory", "將指定類別設為創建類別"),
    // User commands
    INFO("info", "顯示頻道的資訊"),
    INVITE("invite", "邀請他人加入你的頻道"),
    KICK("kick", "踢出頻道中的成員"),
    RENAME("rename", "重新命名你的頻道"),
    TRANSFER("transfer", "轉移頻道的所有權"),
    TOGGLE_VISIBILITY("togglevisibility", "切換頻道的可見性"),
    CLOSE("close", "關閉你的頻道");

    private final String name;
    private final String description;

    VoiceCommands(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static VoiceCommands fromName(String name) {
        for (VoiceCommands command : values()) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + name);
    }
}
