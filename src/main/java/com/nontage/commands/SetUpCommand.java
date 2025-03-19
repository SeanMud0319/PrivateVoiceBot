package com.nontage.commands;

import com.nontage.utils.SlashCommand;
import com.nontage.utils.TextUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;

import static com.nontage.PrivateVoiceBot.config;

public class SetUpCommand extends SlashCommand {
    public SetUpCommand() {
        super(Commands.slash("setup", "設定教學"));
    }
    @Override
    public void execute(Member sender, SlashCommandInteractionEvent event, String subCommand) {
        if (!sender.hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(TextUtils.getNoPermissionEmbed().build()).setEphemeral(config.getBoolean("commandMessageEphemeral")).queue();
            return;
        }
        event.replyEmbeds(TextUtils.embed()
                .setColor(Color.CYAN)
                .setTitle("PrivateVoiceBot 設定教學")
                .setDescription("步驟：")
                .addField("1. 設定個人語音建立頻道", "輸入 `/voice setcreatechannel` 來設定私人語音頻道。", false)
                .addField("2. 設定個人語音建立類別", "輸入 `/voice setcreatecategory` 來設定私人語音頻道類別。", false)
                .addField("3. 注意事項", "注意！ 上述設定指令會更改權限，強烈建議創建新的頻道及類別。", false)
                .setFooter("PrivateVoiceBot ● Made by Nontage", "https://cdn.discordapp.com/avatars/810170073239126066/98c4e35237d5ab7ff452e7dcd71e4a75.png?size=2048&quality=lossless")
                .build()).setEphemeral(true).queue();
    }
}
