package com.rolechat.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    
    private static final Pattern COLOR_PATTERN = Pattern.compile("&([0-9a-fk-or])");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    
    /**
     * Translates color codes in a string, including hex color codes
     * 
     * @param text The text to translate
     * @return The text with color codes translated
     */
    public static String colorize(String text) {
        if (text == null) return "";
        
        // Process hex color codes first (&#RRGGBB format)
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            ChatColor hexColor = ChatColor.of("#" + hexCode);
            matcher.appendReplacement(buffer, hexColor.toString());
        }
        matcher.appendTail(buffer);
        
        // Then process standard color codes
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
    
    /**
     * Strips all color codes from a string
     * 
     * @param text The text to strip
     * @return The text without color codes
     */
    public static String stripColor(String text) {
        if (text == null) return "";
        return ChatColor.stripColor(colorize(text));
    }
    
    /**
     * Checks if a string contains specific color codes
     * 
     * @param text The text to check
     * @param colorCodes The color codes to check for
     * @return True if the text contains any of the specified color codes
     */
    public static boolean containsColorCodes(String text, String... colorCodes) {
        if (text == null || colorCodes == null) return false;
        
        // Check standard color codes
        Matcher matcher = COLOR_PATTERN.matcher(text);
        while (matcher.find()) {
            String foundCode = "&" + matcher.group(1);
            for (String code : colorCodes) {
                if (foundCode.equalsIgnoreCase(code)) {
                    return true;
                }
            }
        }
        
        // Check hex color codes
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        while (hexMatcher.find()) {
            String foundCode = "&#" + hexMatcher.group(1);
            for (String code : colorCodes) {
                if (foundCode.equalsIgnoreCase(code)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Gets all color codes from a string
     * 
     * @param text The text to check
     * @return An array of all color codes found in the text
     */
    public static String[] getColorCodes(String text) {
        if (text == null) return new String[0];
        
        StringBuilder codes = new StringBuilder();
        
        // Get standard color codes
        Matcher matcher = COLOR_PATTERN.matcher(text);
        while (matcher.find()) {
            codes.append("&").append(matcher.group(1)).append(",");
        }
        
        // Get hex color codes
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        while (hexMatcher.find()) {
            codes.append("&#").append(hexMatcher.group(1)).append(",");
        }
        
        if (codes.length() > 0) {
            codes.deleteCharAt(codes.length() - 1);
            return codes.toString().split(",");
        }
        
        return new String[0];
    }
}