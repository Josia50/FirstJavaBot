package com.discordbot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Data {
    public static class Bot extends ListenerAdapter {
        Integer erroramount;
        byte lvl;
        String currencyname = "JosiDollar";
        boolean muted = false;
        DateTimeFormatter forma = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        int msgcount;
        int currency = 0;
        public ArrayList<CharSequence> bannedwords = new ArrayList();
        ArrayList<Long> warnedplayers = new ArrayList<Long>();
        public ArrayList<String> FileDataUser = new ArrayList<>();
        boolean channelmute = false;
        String ChannelSave;
        String lastmsg = now.format(forma);
        String file;


        public void loadchannel() {
            try {
                new File("channels/").mkdir();
                FileInputStream saveFile = new FileInputStream("channels/" + ChannelSave);
                ObjectInputStream save = new ObjectInputStream(saveFile);
                channelmute = (Boolean) save.readObject();
                save.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        public void savechannel() {
            try {
                new File("channels/").mkdir();
                FileOutputStream saveFile = new FileOutputStream("channels/" + ChannelSave);
                ObjectOutputStream save = new ObjectOutputStream(saveFile);
                save.writeObject(channelmute);
                save.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        public void loaddserver() {
            try {
                FileInputStream saveFile = new FileInputStream("server.set");
                ObjectInputStream save = new ObjectInputStream(saveFile);
                currencyname = (String) save.readObject();
                bannedwords = (ArrayList) save.readObject();
                warnedplayers = (ArrayList) save.readObject();
                erroramount = (Integer) save.readObject();
                save.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        public void resetvar() {
            currency = 0;
            msgcount = 0;
            lvl = 0;
            muted = false;
            lastmsg = "";
        }

        public void saveserver() {
            try {
                FileOutputStream saveFile = new FileOutputStream("server.set");
                ObjectOutputStream save = new ObjectOutputStream(saveFile);
                save.writeObject(currencyname);
                save.writeObject(bannedwords);
                save.writeObject(warnedplayers);
                save.writeObject(erroramount);
                save.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        public void load() {
            loaddserver();
            loadchannel();
            try {
                FileInputStream saveFile = new FileInputStream("users/" + file);
                ObjectInputStream save = new ObjectInputStream(saveFile);
                currency = (Integer) save.readObject();
                muted = (Boolean) save.readObject();
                lastmsg = (String) save.readObject();
                msgcount = (Integer) save.readObject();
                lvl = (Byte) save.readObject();
                FileDataUser = (ArrayList<String>) save.readObject();
                save.close();
            } catch (Exception exc) {
                exc.printStackTrace();
                resetvar();
                save();
            }
        }

        public void save() {
            try {
                new File("users/").mkdir();
                FileOutputStream saveFile = new FileOutputStream("users/" + file);
                ObjectOutputStream save = new ObjectOutputStream(saveFile);
                save.writeObject(currency);
                save.writeObject(muted);
                save.writeObject(lastmsg);
                save.writeObject(msgcount);
                save.writeObject(lvl);
                save.writeObject(FileDataUser);
                save.close();
                resetvar();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            saveserver();
            savechannel();
        }

        public void onMessageReceived(MessageReceivedEvent event) {
            file = String.valueOf(event.getMessage().getAuthor().getIdLong());
            String[] args = event.getMessage().getContentRaw().split(" ");

            if (event.getAuthor().isBot()) {
                return;
            }

            if (event.getMessage().getContentRaw().contains("")) {
                ChannelSave = String.valueOf(event.getMessage().getChannel().getIdLong());
                load();
                DateTimeFormatter forma = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                lastmsg = now.format(forma);
                msgcount++;
                save();
            }

            /*if (event.getMessage().getContentRaw().contains("!hah")) {
                file = String.valueOf(event.getAuthor().getIdLong());
                load();
                lvl++;
                event.getChannel().sendMessage(String.valueOf(lvl)).queue();
                event.getChannel().sendMessage(String.valueOf(msgcount)).queue();
                save();
            }*/
            if (event.getMessage().getContentRaw().equalsIgnoreCase(Config.Prefix + "userinfo")) {
                EmbedBuilder me = new EmbedBuilder();
                file = String.valueOf(event.getAuthor().getIdLong());

                load();

                me.setTitle(event.getAuthor().getName() + "'s information.")
                        .addField("User", event.getAuthor().getAsMention(), true)
                        .addField("Bot", String.valueOf(event.getAuthor().isBot()), true)
                        .addField("Creation Date", String.valueOf(event.getAuthor().getCreationTime()), true)
                        .addField("Discriminator", event.getAuthor().getDiscriminator(), true)
                        .addField("ID", String.valueOf(event.getAuthor().getIdLong()), true)
                        .addField("MessageCount", String.valueOf(msgcount), true)
                        .addField("Data File", String.valueOf(event.getAuthor().getIdLong()), true)
                        .setFooter(Config.Botname, event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(me.build()).queue();
            }
            if (event.getMessage().getContentRaw().equalsIgnoreCase(Config.Prefix + "data")) {
                EmbedBuilder d = new EmbedBuilder();

                    file = String.valueOf(event.getAuthor().getIdLong());
                    load();
                    d.setTitle(event.getAuthor().getName() + "'s data.")
                            .addField("Money", String.valueOf(currency), false)
                            .addField("Muted", String.valueOf(muted), false)
                            .addField("Level", String.valueOf(lvl), false)
                            .addField("MessageCount", String.valueOf(msgcount), false)
                            .addField("Last Message", String.valueOf(lastmsg), false)
                            .setFooter(Config.Botname, event.getAuthor().getAvatarUrl());
                    event.getChannel().sendMessage(d.build()).queue();

            }
            }
        }

    }


