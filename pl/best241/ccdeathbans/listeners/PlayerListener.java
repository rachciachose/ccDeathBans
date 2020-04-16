// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import pl.best241.ccsectors.api.CcSectorsAPI;
import pl.best241.ccsectors.data.DimSwitch;
import pl.best241.ccsectors.data.SafeTeleportType;
import pl.best241.ccsectors.api.TeleportLocation;
import org.bukkit.plugin.Plugin;
import pl.best241.ccdeathbans.CcDeathBans;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.UUID;
import pl.best241.ccdeathbans.config.MessagesData;
import pl.best241.ccdeathbans.managers.TimeManager;
import pl.best241.ccdeathbans.backend.RedisBackend;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onAsyncLoging(final AsyncPlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();
        final Long banTime = RedisBackend.getPlayerBanTime(uuid);
        if (banTime != null) {
            if (banTime >= System.currentTimeMillis()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                final String timeTo = TimeManager.getTimeTo(banTime, System.currentTimeMillis());
                event.setKickMessage(MessagesData.youWillRespawnIn.replace("%time", timeTo));
            }
            else {
                RedisBackend.removePlayerBanned(uuid);
            }
        }
    }
    
    @EventHandler
    public static void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (player.isDead()) {
            player.setHealth(20);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (!player.hasPermission("ccDeathBans.deathless")) {
            System.out.println("Player death add ban");
            final UUID uuid = player.getUniqueId();
            Bukkit.getScheduler().runTaskLater((Plugin)CcDeathBans.getPlugin(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    RedisBackend.addPlayerBanned(uuid, TimeManager.getBanTimeInMillis());
                    CcDeathBans.broadcastBan(player.getUniqueId());
                }
            }, 20L * TimeManager.getKickTimeInSeconds());
            Bukkit.getScheduler().runTask((Plugin)CcDeathBans.getPlugin(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    try {
                        CcSectorsAPI.teleportPlayerAuto(player.getUniqueId(), new TeleportLocation(player.getLocation()), new TeleportLocation(player.getWorld().getSpawnLocation()), SafeTeleportType.SAFE, DimSwitch.AUTO, false);
                    }
                    catch (Exception ex) {
                        Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        player.setFireTicks(0);
    }
}
