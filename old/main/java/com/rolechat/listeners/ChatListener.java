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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    
    private final RoleChat plugin;
    private final Pattern dialoguePattern = Pattern.compile("&quot;([^&quot;]*)&quot;");
    
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
        
        // Process the message based on the player's current channel
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
    
    public void processMessage(Player sender, String message, String channel) {
        PlayerData senderData = plugin.getPlayerManager().getPlayerData(sender);
        String nickname = ChatColor.translateAlternateColorCodes('&', senderData.getNickname());
        
        // Format the message with dialogue and emote colors
        String formattedMessage = formatMessage(message, senderData);
        
        // Get the base format for this channel
        String format = plugin.getConfigManager().getFormat(channel);
        
        // Replace placeholders in the format
        format = format.replace("{displayname}", sender.getDisplayName());
        format = format.replace("{nickname}", nickname);
        format = format.replace("{message}", formattedMessage);
        format = ChatColor.translateAlternateColorCodes('&', format);
        
        // Create the message component with hover text
        TextComponent messageComponent = new TextComponent(format);
        
        // Send the message based on the channel
        List<Player> recipients = new ArrayList<>();
        
        switch (channel) {
            case "ooc":
                sendOOCMessage(sender, messageComponent, recipients);
                break;
            case "shout":
                sendRangedMessage(sender, messageComponent, plugin.getConfigManager().getRange("shout"), recipients);
                break;
            case "talk":
                sendRangedMessage(sender, messageComponent, plugin.getConfigManager().getRange("talk"), recipients);
                break;
            case "whisper":
                sendRangedMessage(sender, messageComponent, plugin.getConfigManager().getRange("whisper"), recipients);
                break;
            default:
                // Default to talk if channel is unknown
                sendRangedMessage(sender, messageComponent, plugin.getConfigManager().getRange("talk"), recipients);
                break;
        }
        
        // Send spy messages
        sendSpyMessage(sender, format, channel, recipients);
        
        // If no one heard the message and heardyou is enabled, inform the sender
        if ((recipients.isEmpty() || (recipients.size() == 1 && recipients.contains(sender))) 
                && plugin.getConfigManager().isHeardYouEnabled()) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-one-heard"));
        }
    }
    
    private String formatMessage(String message, PlayerData playerData) {
        String dialogueColor = ChatUtils.colorize(playerData.getDialogueColor());
        String emoteColor = ChatUtils.colorize(playerData.getEmoteColor());
        
        // Replace dialogue with colored text
        StringBuffer result = new StringBuffer();
        Matcher matcher = dialoguePattern.matcher(message);
        
        while (matcher.find()) {
            String dialogue = matcher.group(1);
            // Color the quotation marks and the dialogue text with dialogue color
            matcher.appendReplacement(result, dialogueColor + "&quot;" + dialogue + "&quot;" + emoteColor);
        }
        matcher.appendTail(result);
        
        // If no dialogue was found, apply emote color to the whole message
        if (result.length() == 0) {
            return emoteColor + message;
        }
        
        // Apply emote color to the beginning of the message
        return emoteColor + result.toString();
    }
    
    private void sendOOCMessage(Player sender, TextComponent messageComponent, List<Player> recipients) {
        // OOC is global, so send to all players who haven't disabled OOC
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
            
            if (playerData.isOocEnabled()) {
                player.spigot().sendMessage(messageComponent);
                if (!recipients.contains(player)) {
                    recipients.add(player);
                }
            }
        }
    }
    
    private void sendRangedMessage(Player sender, TextComponent messageComponent, int range, List<Player> recipients) {
        // Add hover text showing who heard the message
        List<String> hearers = new ArrayList<>();
        
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            // Skip players who are too far away
            if (player.getWorld() != sender.getWorld() || player.getLocation().distance(sender.getLocation()) > range) {
                continue;
            }
            
            player.spigot().sendMessage(messageComponent);
            hearers.add(player.getName());
            if (!recipients.contains(player)) {
                recipients.add(player);
            }
        }
        
        // Add hover text
        String hoverText = "Heard by: " + String.join(", ", hearers);
        messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder(hoverText).create()));
    }
    
    private void sendSpyMessage(Player sender, String format, String channel, List<Player> recipients) {
        // Send spy messages to players with spy mode enabled who didn't already receive the message
        String spyFormat = plugin.getConfigManager().getSpyFormat().replace("{format}", format);
        spyFormat = ChatColor.translateAlternateColorCodes('&', spyFormat);
        
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            // Skip the original recipients
            if (recipients.contains(player)) {
                continue;
            }
            
            PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
            if (playerData.isSpyActive() && (player.hasPermission("rolechat.spy") || player.hasPermission("rolechat.*"))) {
                player.sendMessage(spyFormat);
            }
        }
    }
}