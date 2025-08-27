package com.rolechat.commands;

import com.rolechat.RoleChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RoleChatCommand implements CommandExecutor {
    
    private final RoleChat plugin;
    
    public RoleChatCommand(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rolechat.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reload();
                sender.sendMessage(plugin.getConfigManager().getMessage("reload-success"));
                break;
            case "restrict":
                if (!sender.hasPermission("rolechat.restrict") && !sender.hasPermission("rolechat.*")) {
                    sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /rolechat restrict <color code>");
                    return true;
                }
                // Add the color code to the restricted list
                String colorCode = args[1];
                plugin.getConfigManager().addRestrictedColor(colorCode);
                sender.sendMessage("§aAdded §r" + colorCode + "§r §ato the restricted colors list.");
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                showHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6=== RoleChat Commands ===");
        sender.sendMessage("§e/rolechat reload §7- Reload the plugin configuration");
        sender.sendMessage("§e/rolechat help §7- Show this help message");
        sender.sendMessage("§e/rolechat restrict <color code> §7- Add a color code to the restricted list");
        sender.sendMessage("§e/spy §7- Toggle spy mode");
        sender.sendMessage("§e/nickname <nickname> §7- Set your nickname");
        sender.sendMessage("§e/realname <nickname> §7- Get a player's real name from their nickname");
        sender.sendMessage("§e/whisper [message] §7- Set whisper as your channel or send a whisper message");
        sender.sendMessage("§e/talk [message] §7- Set talk as your channel or send a talk message");
        sender.sendMessage("§e/shout [message] §7- Set shout as your channel or send a shout message");
        sender.sendMessage("§e/ooc [message] §7- Set OOC as your channel or send an OOC message");
        sender.sendMessage("§e/toggleooc §7- Toggle visibility of OOC messages");
        sender.sendMessage("§e/dialoguepref <color code> §7- Set your dialogue color preference");
        sender.sendMessage("§e/emotepref <color code> §7- Set your emote color preference");
        sender.sendMessage("§e/heardyou §7- Toggle the 'No one heard you' message");
    }
}