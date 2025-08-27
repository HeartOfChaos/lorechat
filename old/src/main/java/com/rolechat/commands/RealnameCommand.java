package com.rolechat.commands;

import com.rolechat.RoleChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RealnameCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    
    public RealnameCommand(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rolechat.realname")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /realname <nickname>");
            return true;
        }
        
        // Combine all arguments into a single nickname
        StringBuilder nicknameBuilder = new StringBuilder();
        for (String arg : args) {
            nicknameBuilder.append(arg).append(" ");
        }
        String nickname = nicknameBuilder.toString().trim();
        
        // Find the player with this nickname
        Player targetPlayer = plugin.getPlayerManager().getPlayerByNickname(nickname);
        
        if (targetPlayer == null) {
            sender.sendMessage(plugin.getConfigManager().getMessage("realname-not-found"));
            return true;
        }
        
        sender.sendMessage(plugin.getConfigManager().getMessage("realname-result", 
                "nickname", nickname, 
                "realname", targetPlayer.getName()));
        
        return true;
    }
}