package com.discordbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;


public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = Config.Token;
        builder.setToken(token);
        builder.addEventListener(new Data.Bot());
        builder.buildAsync();
       // new Data.Bot();
    }
}


