package com.rolechat.commands;

import com.rolechat.RoleChat;
import com.rolechat.models.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleOOCCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    
    public ToggleOOCCommand(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("rolechat.toggleooc")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
        plugin.getPlayerManager().toggleOOC(player);
        
        if (playerData.isOocEnabled()) {
            player.sendMessage(plugin.getConfigManager().getMessage("ooc-toggled-on"));
        } else {
            player.sendMessage(plugin.getConfigManager().getMessage("ooc-toggled-off"));
        }
        
        return true;
    }
}