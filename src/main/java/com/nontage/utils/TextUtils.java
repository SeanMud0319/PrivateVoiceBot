package com.nontage.utils;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

import static com.nontage.PrivateVoiceBot.config;

public class TextUtils {
    public static EmbedBuilder embed() {
        return new EmbedBuilder();
    }

    public static EmbedBuilder getNoPermissionEmbed() {
        return embed()
                .setColor(Color.decode(config.getString("noPermissionColor")))
                .setTitle(config.getString("noPermissionTitle"))
                .setDescription(config.getString("noPermissionDescription"))
                .setFooter(config.getString("footer"), config.getString("footerIcon"));
    }

    public static EmbedBuilder getNoVoiceChannelEmbed() {
        return embed()
                .setColor(Color.decode(config.getString("noVoiceChannelColor")))
                .setTitle(config.getString("noVoiceChannelTitle"))
                .setDescription(config.getString("noVoiceChannelDescription"))
                .setFooter(config.getString("footer"), config.getString("footerIcon"));
    }

    public static EmbedBuilder getGlobalEmbed() {
        return embed().setFooter(config.getString("footer"), config.getString("footerIcon"));
    }
}