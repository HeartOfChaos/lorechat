package com.rolechat.managers;

import com.rolechat.RoleChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    
    private final RoleChat plugin;
    private FileConfiguration config;
    
    public ConfigManager(RoleChat plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
    
    public String getDefaultDialogueColor() {
        return config.getString("defaults.dialogue-color", "&f");
    }
    
    public String getDefaultEmoteColor() {
        return config.getString("defaults.emote-color", "&7");
    }
    
    public List<String> getRestrictedColors() {
        return config.getStringList("restricted-colors");
    }
    
    public void addRestrictedColor(String colorCode) {
        List<String> restrictedColors = getRestrictedColors();
        if (!restrictedColors.contains(colorCode)) {
            restrictedColors.add(colorCode);
            config.set("restricted-colors", restrictedColors);
            plugin.saveConfig();
        }
    }
    
    public boolean isHeardYouEnabled() {
        return config.getBoolean("heardyou", true);
    }
    
    public String getFormat(String channel) {
        return config.getString("formats." + channel, "{nickname}: {message}");
    }
    
    public String getSpyFormat() {
        return config.getString("formats.spy", "&6[Spy] {format}");
    }
    
    public int getRange(String channel) {
        return config.getInt("ranges." + channel, 0);
    }
    
    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', 
                config.getString("messages." + key, "&cMessage not found: " + key));
    }
    
    public String getMessage(String key, String... replacements) {
        String message = getMessage(key);
        
        if (replacements != null && replacements.length % 2 == 0) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replace("{" + replacements[i] + "}", replacements[i + 1]);
            }
        }
        
        return message;
    }
    
    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
}