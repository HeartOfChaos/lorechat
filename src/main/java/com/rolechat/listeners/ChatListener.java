package com.rolechat.listeners;

import com.rolechat.RoleChat;
import com.rolechat.models.PlayerData;
import com.rolechat.utils.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final RoleChat plugin;
    private final Pattern dialoguePattern = Pattern.compile("\"([^\"]*)\"");

    public ChatListener(RoleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
        String message = event.getMessage();
        String channel = playerData.getChannel();

        processMessage(player, message, channel);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerManager().loadPlayerData(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerManager().savePlayerData(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String command = event.getMessage();

        String spyFormat = plugin.getConfigManager().getSpyFormat()
                .replace("{format}", "§7[Command] §f" + sender.getName() + ": §7" + command);
        spyFormat = ChatUtils.colorize(spyFormat);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.equals(sender)) continue;

            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
            if (playerData.isSpyActive() && (player.hasPermission("rolechat.spy") || player.hasPermission("rolechat.*"))) {
                player.sendMessage(spyFormat);
            }
        }
    }

    public void processMessage(Player sender, String message, String channel) {
        PlayerData senderData = plugin.getPlayerManager().getPlayerData(sender);
        String nickname = ChatUtils.colorize(senderData.getNickname());

        String format = plugin.getConfigManager().getFormat(channel);

        format = format.replace("{displayname}", ChatUtils.colorize(sender.getDisplayName()));
        format = format.replace("{nickname}", nickname);

        List<Player> recipients = new ArrayList<>();

        switch (channel) {
            case "ooc":
                sendOOCMessage(sender, message, format, recipients);
                break;
            case "shout":
                sendRangedMessage(sender, message, format, plugin.getConfigManager().getRange("shout"), recipients);
                break;
            case "talk":
                sendRangedMessage(sender, message, format, plugin.getConfigManager().getRange("talk"), recipients);
                break;
            case "whisper":
                sendRangedMessage(sender, message, format, plugin.getConfigManager().getRange("whisper"), recipients);
                break;
            default:
                sendRangedMessage(sender, message, format, plugin.getConfigManager().getRange("talk"), recipients);
                break;
        }

        sendSpyMessage(sender, message, format, channel, recipients);

        if ((recipients.isEmpty() || (recipients.size() == 1 && recipients.contains(sender)))
                && plugin.getConfigManager().isHeardYouEnabled()) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-one-heard"));
        }
    }

    private void sendOOCMessage(Player sender, String message, String format, List<Player> recipients) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);

            if (playerData.isOocEnabled()) {
                String formattedMessage = ChatColor.GRAY + message;
                String finalFormat = format.replace("{message}", formattedMessage);
                finalFormat = ChatUtils.colorize(finalFormat);

                BaseComponent[] messageComponent = TextComponent.fromLegacyText(finalFormat);
                player.spigot().sendMessage(messageComponent);

                if (!recipients.contains(player)) {
                    recipients.add(player);
                }
            }
        }
    }

    private void sendRangedMessage(Player sender, String message, String format, int range, List<Player> recipients) {
        List<String> hearers = new ArrayList<>();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getWorld() != sender.getWorld() || player.getLocation().distance(sender.getLocation()) > range) {
                continue;
            }

            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
            String formattedMessage = formatMessageForRecipient(message, playerData);
            String finalFormat = format.replace("{message}", formattedMessage);
            finalFormat = ChatUtils.colorize(finalFormat);

            BaseComponent[] messageComponent = TextComponent.fromLegacyText(finalFormat);

            hearers.add(player.getName());
            //String hoverText = "Heard by: " + String.join(", ", hearers);
            //messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            //        new ComponentBuilder(hoverText).create()));

            player.spigot().sendMessage(messageComponent);
            if (!recipients.contains(player)) {
                recipients.add(player);
            }
        }
    }

    private void sendSpyMessage(Player sender, String message, String format, String channel, List<Player> recipients) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (recipients.contains(player)) continue;

            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
            if (playerData.isSpyActive() && (player.hasPermission("rolechat.spy") || player.hasPermission("rolechat.*"))) {
                String formattedMessage = channel.equals("ooc")
                        ? ChatColor.GRAY + message
                        : formatMessageForRecipient(message, playerData);

                String finalFormat = format.replace("{message}", formattedMessage);
                finalFormat = ChatUtils.colorize(finalFormat);

                String spyFormat = plugin.getConfigManager().getSpyFormat().replace("{format}", finalFormat);
                spyFormat = ChatUtils.colorize(spyFormat);

                player.spigot().sendMessage(TextComponent.fromLegacyText(spyFormat));
            }
        }
    }

    private String formatMessageForRecipient(String message, PlayerData recipientData) {
        String dialogueColor = ChatUtils.colorize(recipientData.getDialogueColor());
        String emoteColor = ChatUtils.colorize(recipientData.getEmoteColor());

        Matcher matcher = dialoguePattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String quoted = matcher.group(0);
            matcher.appendReplacement(buffer, dialogueColor + quoted + ChatColor.RESET + emoteColor);
        }
        matcher.appendTail(buffer);

        return ChatUtils.colorize(emoteColor + buffer.toString());
    }
}
