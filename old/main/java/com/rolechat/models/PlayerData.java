package com.rolechat.models;

import java.util.UUID;

public class PlayerData {
    
    private final UUID uuid;
    private String dialogueColor;
    private String emoteColor;
    private String nickname;
    private String channel;
    private boolean spyActive;
    private boolean oocEnabled;
    
    public PlayerData(UUID uuid, String dialogueColor, String emoteColor, String nickname, String channel, boolean spyActive, boolean oocEnabled) {
        this.uuid = uuid;
        this.dialogueColor = dialogueColor;
        this.emoteColor = emoteColor;
        this.nickname = nickname;
        this.channel = channel;
        this.spyActive = spyActive;
        this.oocEnabled = oocEnabled;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public String getDialogueColor() {
        return dialogueColor;
    }
    
    public void setDialogueColor(String dialogueColor) {
        this.dialogueColor = dialogueColor;
    }
    
    public String getEmoteColor() {
        return emoteColor;
    }
    
    public void setEmoteColor(String emoteColor) {
        this.emoteColor = emoteColor;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getChannel() {
        return channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    public boolean isSpyActive() {
        return spyActive;
    }
    
    public void setSpyActive(boolean spyActive) {
        this.spyActive = spyActive;
    }
    
    public boolean isOocEnabled() {
        return oocEnabled;
    }
    
    public void setOocEnabled(boolean oocEnabled) {
        this.oocEnabled = oocEnabled;
    }
}