// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans.config;

import org.bukkit.plugin.Plugin;

public class MessagesData
{
    public static String playerNotInDatabase;
    public static String playerNotDeath;
    public static String successfullyRespawnedPlayer;
    public static String hcresUsage;
    public static String noPermissions;
    public static String youWillRespawnIn;
    public static String hcbantimeUsage;
    public static String valueNotInteger;
    private static MessagesConfig config;
    
    public static void loadMessages(final Plugin plugin) {
        (MessagesData.config = new MessagesConfig(plugin, "messages.yml")).saveDefaultConfig();
        MessagesData.config.reloadCustomConfig();
        MessagesData.playerNotInDatabase = MessagesData.config.getString("playerNotInDatabase");
        MessagesData.playerNotDeath = MessagesData.config.getString("playerNotDeath");
        MessagesData.successfullyRespawnedPlayer = MessagesData.config.getString("successfullyRespawnedPlayer");
        MessagesData.hcresUsage = MessagesData.config.getString("hcresUsage");
        MessagesData.noPermissions = MessagesData.config.getString("noPermissions");
        MessagesData.youWillRespawnIn = MessagesData.config.getString("youWillRespawnIn");
        MessagesData.valueNotInteger = MessagesData.config.getString("valueNotInteger");
    }
}
