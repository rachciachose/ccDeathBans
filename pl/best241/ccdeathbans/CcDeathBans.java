// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans;

import pl.best241.rdbplugin.pubsub.PubSub;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import pl.best241.ccsectors.api.CcSectorsAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.best241.ccdeathbans.managers.TimeManager;
import pl.best241.ccdeathbans.backend.RedisBackend;
import pl.best241.ccdeathbans.config.MessagesData;
import pl.best241.ccdeathbans.listeners.PubSubRecieveMessageListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import pl.best241.ccdeathbans.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CcDeathBans extends JavaPlugin
{
    private static CcDeathBans plugin;
    
    public void onEnable() {
        CcDeathBans.plugin = this;
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PubSubRecieveMessageListener(), (Plugin)this);
        MessagesData.loadMessages((Plugin)CcDeathBans.plugin);
        TimeManager.setBanTimeInMillis(RedisBackend.getBanTime());
    }
    
    public static CcDeathBans getPlugin() {
        return CcDeathBans.plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("hcres")) {
            if (sender.hasPermission("ccDeathBans.respawnPlayer")) {
                if (args.length == 1) {
                    UUID uuid;
                    try {
                        uuid = CcSectorsAPI.getUUID(args[0]);
                    }
                    catch (Exception ex) {
                        sender.sendMessage(ChatColor.DARK_GRAY + " »" + ChatColor.RED + "Blad podczas pobierania UUID z bazy danych!");
                        Logger.getLogger(CcDeathBans.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }
                    if (uuid == null) {
                        sender.sendMessage(MessagesData.playerNotInDatabase);
                        return false;
                    }
                    final Long banTime = RedisBackend.getPlayerBanTime(uuid);
                    if (banTime == null || banTime < System.currentTimeMillis()) {
                        sender.sendMessage(MessagesData.playerNotDeath);
                        return false;
                    }
                    RedisBackend.removePlayerBanned(uuid);
                    sender.sendMessage(MessagesData.successfullyRespawnedPlayer.replace("%nick", args[0]).replace("%uuid", uuid.toString()));
                }
                else {
                    sender.sendMessage(MessagesData.hcresUsage);
                }
            }
            else {
                sender.sendMessage(MessagesData.noPermissions);
            }
        }
        else if (cmd.getName().equalsIgnoreCase("hcbantime")) {
            if (sender.hasPermission("ccDeathBans.hcBanTime")) {
                if (args.length == 1) {
                    try {
                        final Long value = Long.parseLong(args[0]);
                        RedisBackend.setBanTime(value * 60L * 1000L);
                        broadcastBanTime(value * 60L * 1000L);
                    }
                    catch (Exception ex) {
                        sender.sendMessage(MessagesData.valueNotInteger);
                    }
                }
                else {
                    sender.sendMessage(MessagesData.hcbantimeUsage);
                }
            }
            else {
                sender.sendMessage(MessagesData.noPermissions);
            }
        }
        return false;
    }
    
    public static void broadcastBan(final UUID uuid) {
        PubSub.broadcast("ccDeathBans.addBanKick", uuid.toString());
    }
    
    public static void broadcastBanTime(final Long time) {
        PubSub.broadcast("ccDeathBans.banTime", time.toString());
    }
}
