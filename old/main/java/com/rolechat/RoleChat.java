package com.rolechat;

import com.rolechat.commands.*;
import com.rolechat.listeners.ChatListener;
import com.rolechat.managers.ConfigManager;
import com.rolechat.managers.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RoleChat extends JavaPlugin {
    
    private static RoleChat instance;
    private ConfigManager configManager;
    private PlayerManager playerManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        configManager = new ConfigManager(this);
        playerManager = new PlayerManager(this);
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        
        // Register commands
        registerCommands();
        
        getLogger().info("RoleChat has been enabled!");
    }
    
    @Override
    public void onDisable() {
        // Save player data
        playerManager.saveAllPlayerData();
        
        getLogger().info("RoleChat has been disabled!");
    }
    
    private void registerCommands() {
        // Create tab completer
        TabCompleter tabCompleter = new TabCompleter(this);
        
        // Register command executors
        getCommand("spy").setExecutor(new SpyCommand(this));
        getCommand("nickname").setExecutor(new NicknameCommand(this));
        getCommand("realname").setExecutor(new RealnameCommand(this));
        getCommand("whisper").setExecutor(new ChannelCommand(this, "whisper"));
        getCommand("shout").setExecutor(new ChannelCommand(this, "shout"));
        getCommand("talk").setExecutor(new ChannelCommand(this, "talk"));
        getCommand("ooc").setExecutor(new ChannelCommand(this, "ooc"));
        getCommand("toggleooc").setExecutor(new ToggleOOCCommand(this));
        getCommand("dialoguepref").setExecutor(new DialoguePrefCommand(this));
        getCommand("emotepref").setExecutor(new EmotePrefCommand(this));
        getCommand("heardyou").setExecutor(new HeardYouCommand(this));
        getCommand("rolechat").setExecutor(new RoleChatCommand(this));
        
        // Register tab completers
        getCommand("rolechat").setTabCompleter(tabCompleter);
        getCommand("realname").setTabCompleter(tabCompleter);
        getCommand("nickname").setTabCompleter(tabCompleter);
    }
    
    public static RoleChat getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    public void reload() {
        configManager.reloadConfig();
        playerManager.reloadPlayerData();
    }
}