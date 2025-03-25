package com.nontage.commands;

import com.nontage.PrivateVoiceBot;
import com.nontage.utils.SlashCommand;
import com.nontage.utils.TextUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;

/**
 * I hope you won't delete this,
 * but if you do, I won't stop you.
 */

public class AboutCommand extends SlashCommand {
    public AboutCommand() {
        super(Commands.slash("about", "Get information about the bot."));
    }

    @Override
    public void execute(Member sender, SlashCommandInteractionEvent event, String subCommand) {
        event.replyEmbeds(TextUtils.embed()
                .setColor(Color.decode("#7AD3FF"))
                .setTitle("PrivateVoiceBot Information")
                .setDescription("PrivateVoiceBot is a Discord bot that lets users create private voice channels in their server, offering easy control through simple commands.")
                .addField("Author", "Nontage", true)
                .addField("Source Code", "[GitHub](https://github.com/SeanMud0319)", true)
                .addField("Version", PrivateVoiceBot.config.getString("version"), true)
                .setFooter("PrivateVoiceBot ● Made by Nontage", "https://cdn.discordapp.com/avatars/810170073239126066/98c4e35237d5ab7ff452e7dcd71e4a75.png?size=2048&quality=lossless")
                .build()).queue();
    }
}
