package com.rolechat.managers;

import com.rolechat.RoleChat;
import com.rolechat.models.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    
    private final RoleChat plugin;
    private final Map<UUID, PlayerData> playerDataMap;
    private final File playerDataFolder;
    
    public PlayerManager(RoleChat plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
        this.playerDataFolder = new File(plugin.getDataFolder(), "playerdata");
        
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }
    
    public PlayerData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (!playerDataMap.containsKey(uuid)) {
            loadPlayerData(player);
        }
        
        return playerDataMap.get(uuid);
    }
    
    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        
        if (playerFile.exists()) {
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            
            String dialogueColor = playerConfig.getString("dialogue-color", 
                    plugin.getConfigManager().getDefaultDialogueColor());
            String emoteColor = playerConfig.getString("emote-color", 
                    plugin.getConfigManager().getDefaultEmoteColor());
            String nickname = playerConfig.getString("nickname", player.getName());
            String channel = playerConfig.getString("channel", "talk");
            boolean spyActive = playerConfig.getBoolean("spy-active", false);
            boolean oocEnabled = playerConfig.getBoolean("ooc-enabled", true);
            
            PlayerData playerData = new PlayerData(uuid, dialogueColor, emoteColor, nickname, channel, spyActive, oocEnabled);
            playerDataMap.put(uuid, playerData);
        } else {
            // Create default player data
            PlayerData playerData = new PlayerData(
                    uuid,
                    plugin.getConfigManager().getDefaultDialogueColor(),
                    plugin.getConfigManager().getDefaultEmoteColor(),
                    player.getName(),
                    "talk",
                    false,
                    true
            );
            playerDataMap.put(uuid, playerData);
            savePlayerData(player);
        }
    }
    
    public void savePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (playerDataMap.containsKey(uuid)) {
            PlayerData playerData = playerDataMap.get(uuid);
            File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            
            playerConfig.set("dialogue-color", playerData.getDialogueColor());
            playerConfig.set("emote-color", playerData.getEmoteColor());
            playerConfig.set("nickname", playerData.getNickname());
            playerConfig.set("channel", playerData.getChannel());
            playerConfig.set("spy-active", playerData.isSpyActive());
            playerConfig.set("ooc-enabled", playerData.isOocEnabled());
            
            try {
                playerConfig.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save player data for " + player.getName() + ": " + e.getMessage());
            }
        }
    }
    
    public void saveAllPlayerData() {
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            Player player = plugin.getServer().getPlayer(entry.getKey());
            if (player != null) {
                savePlayerData(player);
            }
        }
    }
    
    public void reloadPlayerData() {
        playerDataMap.clear();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            loadPlayerData(player);
        }
    }
    
    public void setNickname(Player player, String nickname) {
        PlayerData playerData = getPlayerData(player);
        playerData.setNickname(nickname);
        savePlayerData(player);
    }
    
    public void setChannel(Player player, String channel) {
        PlayerData playerData = getPlayerData(player);
        playerData.setChannel(channel);
        savePlayerData(player);
    }
    
    public void toggleSpy(Player player) {
        PlayerData playerData = getPlayerData(player);
        playerData.setSpyActive(!playerData.isSpyActive());
        savePlayerData(player);
    }
    
    public void toggleOOC(Player player) {
        PlayerData playerData = getPlayerData(player);
        playerData.setOocEnabled(!playerData.isOocEnabled());
        savePlayerData(player);
    }
    
    public void setDialogueColor(Player player, String color) {
        PlayerData playerData = getPlayerData(player);
        playerData.setDialogueColor(color);
        savePlayerData(player);
    }
    
    public void setEmoteColor(Player player, String color) {
        PlayerData playerData = getPlayerData(player);
        playerData.setEmoteColor(color);
        savePlayerData(player);
    }
    
    public Player getPlayerByNickname(String nickname) {
        String strippedNickname = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', nickname));
        
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            String playerNickname = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', entry.getValue().getNickname()));
            if (playerNickname.equalsIgnoreCase(strippedNickname)) {
                return plugin.getServer().getPlayer(entry.getKey());
            }
        }
        
        return null;
    }
    
    public boolean hasRestrictedColors(String text) {
        for (String restrictedColor : plugin.getConfigManager().getRestrictedColors()) {
            if (text.contains(restrictedColor)) {
                return true;
            }
        }
        return false;
    }
}