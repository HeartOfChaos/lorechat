package com.rolechat.commands;

import com.rolechat.RoleChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    
    private final RoleChat plugin;
    
    public TabCompleter(RoleChat plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (command.getName().equalsIgnoreCase("rolechat")) {
            if (args.length == 1) {
                completions.addAll(Arrays.asList("reload", "help", "restrict"));
            }
        } else if (command.getName().equalsIgnoreCase("realname")) {
            if (args.length == 1) {
                // Suggest nicknames of online players
                completions.addAll(plugin.getServer().getOnlinePlayers().stream()
                        .map(player -> plugin.getPlayerManager().getPlayerData(player).getNickname())
                        .collect(Collectors.toList()));
            }
        } else if (command.getName().equalsIgnoreCase("nickname")) {
            if (args.length == 1) {
                // Suggest current nickname
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    completions.add(plugin.getPlayerManager().getPlayerData(player).getNickname());
                }
            }
        }
        
        return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}