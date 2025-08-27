package com.rolechat.commands;

import com.rolechat.RoleChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChannelCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    private final String channelName;
    
    public ChannelCommand(RoleChat plugin, String channelName) {
        this.plugin = plugin;
        this.channelName = channelName;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("rolechat.channel." + channelName) && !player.hasPermission("rolechat.channel.*")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        // If there are arguments, send a message in this channel without changing the current channel
        if (args.length > 0) {
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(" ");
            }
            String message = messageBuilder.toString().trim();
            
            // Process the message in this channel without changing the current channel
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                // Create a new instance of ChatListener to process the message
                new com.rolechat.listeners.ChatListener(plugin).processMessage(player, message, channelName);
            });
            
            return true;
        }
        
        // Otherwise, set this as the player's current channel
        plugin.getPlayerManager().setChannel(player, channelName);
        player.sendMessage(plugin.getConfigManager().getMessage("channel-set", "channel", channelName));
        
        return true;
    }
}