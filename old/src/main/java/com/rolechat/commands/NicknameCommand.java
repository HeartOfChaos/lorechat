package com.rolechat.commands;

import com.rolechat.RoleChat;
import com.rolechat.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NicknameCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    
    public NicknameCommand(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("rolechat.nickname")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage("§cUsage: /nickname <nickname>");
            return true;
        }
        
        // Combine all arguments into a single nickname
        StringBuilder nicknameBuilder = new StringBuilder();
        for (String arg : args) {
            nicknameBuilder.append(arg).append(" ");
        }
        String nickname = nicknameBuilder.toString().trim();
        
        // Check for restricted color codes
        if (containsRestrictedColors(nickname) && !player.hasPermission("rolechat.allcolors")) {
            player.sendMessage(plugin.getConfigManager().getMessage("nickname-restricted"));
            return true;
        }
        
        // Set the nickname
        plugin.getPlayerManager().setNickname(player, nickname);
        player.sendMessage(plugin.getConfigManager().getMessage("nickname-set", "nickname", ChatUtils.colorize(nickname)));
        
        return true;
    }
    
    private boolean containsRestrictedColors(String text) {
        for (String restrictedColor : plugin.getConfigManager().getRestrictedColors()) {
            if (text.contains(restrictedColor)) {
                return true;
            }
        }
        return false;
    }
}