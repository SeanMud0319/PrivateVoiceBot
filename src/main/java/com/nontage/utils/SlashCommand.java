package com.nontage.utils;

import com.nontage.PrivateVoiceBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.HashMap;
import java.util.Map;

public abstract class SlashCommand {
    private static final Map<String, SlashCommand> cmds = new HashMap<>();

    public static void register(SlashCommand... commands) {
        CommandListUpdateAction action = PrivateVoiceBot.jda.updateCommands();
        for (SlashCommand command : commands) {
            cmds.put(command.getName(), command);
            action.addCommands(command.getData());
            System.out.println("Registered command: " + command.getName());
        }
        action.queue();
    }

    public static void handle(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        String subCommand = event.getSubcommandName();
        SlashCommand command = cmds.get(commandName);
        if (command != null) {
            command.execute(event.getMember(), event, subCommand);
        }
    }

    private final SlashCommandData data;

    public SlashCommand(SlashCommandData data) {
        this.data = data;
    }

    public String getName() {
        return data.getName();
    }

    public SlashCommandData getData() {
        return data;
    }

    public abstract void execute(Member sender, SlashCommandInteractionEvent event, String subCommand);
}
