package com.rolechat.commands;

import com.rolechat.RoleChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class HeardYouCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    
    public HeardYouCommand(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rolechat.heardyou") && !sender.hasPermission("rolechat.*")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        // Toggle the heardyou setting
        FileConfiguration config = plugin.getConfig();
        boolean currentValue = config.getBoolean("heardyou", true);
        config.set("heardyou", !currentValue);
        plugin.saveConfig();
        
        // Reload the config manager to apply changes
        plugin.getConfigManager().reloadConfig();
        
        // Send feedback message
        if (currentValue) {
            sender.sendMessage("§7No one heard you: §cOff");
        } else {
            sender.sendMessage("§7No one heard you: §aOn");
        }
        
        return true;
    }
}