package com.nontage.commands;

import com.nontage.utils.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SetUpCommand extends SlashCommand {
    public SetUpCommand() {
        super(Commands.slash("setup", "設定教學"));
    }
    @Override
    public void execute(Member sender, SlashCommandInteractionEvent event, String subCommand) {

    }
}
