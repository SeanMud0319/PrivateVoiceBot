package com.nontage;

import com.nontage.commands.AboutCommand;
import com.nontage.commands.VoiceCommand;
import com.nontage.listeners.ChannelDelete;
import com.nontage.listeners.ChannelUpdateName;
import com.nontage.listeners.GuildVoiceUpdate;
import com.nontage.listeners.SlashCommandInteraction;
import com.nontage.utils.SimpleYaml;
import com.nontage.utils.SlashCommand;
import com.nontage.utils.VoiceTimer;
import com.nontage.voice.VoiceGuildManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class PrivateVoiceBot {
    public static SimpleYaml config;
    public static VoiceGuildManager manager;
    public static JDA jda;
    public static VoiceTimer voiceTimer;

    public static void main(String[] args) {
        config = new SimpleYaml("PrivateVoiceBot", "config.yml");
        try {
            jda = JDABuilder.createDefault(config.getString("token"))
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .addEventListeners(new GuildVoiceUpdate(), new SlashCommandInteraction(), new ChannelDelete(), new ChannelUpdateName())
                    .build();
            System.out.println("Initiation JDA...");
            manager = new VoiceGuildManager();
            jda.awaitReady();
            System.out.println("JDA Loaded!");
            SlashCommand.register(new VoiceCommand(), new AboutCommand());
            jda.getGuilds().forEach(guild -> {
                manager.removeExceptionChannel(guild.getIdLong());
            });
            voiceTimer = new VoiceTimer();
            voiceTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("If the exception is not associated with config, please ignore this message.");
            System.err.println("Error: Config file not found or invalid token or invalid values, please check your config.yml\nPath: " + config.getFilePath());
        }
    }
}