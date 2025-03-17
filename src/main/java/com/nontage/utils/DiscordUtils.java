package com.nontage.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class DiscordUtils {
    public static void reply(Message message, EmbedBuilder embedBuilder) {
        message.replyEmbeds(embedBuilder.build()).mentionRepliedUser(false).queue();
    }
}
