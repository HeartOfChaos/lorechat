package com.rolechat.commands;

import com.rolechat.RoleChat;
import com.rolechat.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DialoguePrefCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    
    public DialoguePrefCommand(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("rolechat.dialoguepref")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage("§cUsage: /dialoguepref <color code>");
            return true;
        }
        
        // Combine all arguments into a single color code
        StringBuilder colorBuilder = new StringBuilder();
        for (String arg : args) {
            colorBuilder.append(arg).append(" ");
        }
        String colorCode = colorBuilder.toString().trim();
        
        // Check for restricted color codes
        if (containsRestrictedColors(colorCode) && !player.hasPermission("rolechat.allcolors")) {
            player.sendMessage(plugin.getConfigManager().getMessage("color-restricted"));
            return true;
        }
        
        // Set the dialogue color preference
        plugin.getPlayerManager().setDialogueColor(player, colorCode);
        player.sendMessage(plugin.getConfigManager().getMessage("dialogue-color-set", 
                "color", ChatUtils.colorize(colorCode) + "This is your new dialogue color"));
        
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