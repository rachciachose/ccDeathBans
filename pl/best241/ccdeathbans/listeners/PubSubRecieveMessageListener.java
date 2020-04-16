// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans.listeners;

import org.bukkit.event.EventHandler;
import pl.best241.rdbplugin.pubsub.PubSub;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import pl.best241.ccdeathbans.config.MessagesData;
import pl.best241.ccdeathbans.managers.TimeManager;
import pl.best241.ccdeathbans.backend.RedisBackend;
import pl.best241.ccdeathbans.CcDeathBans;
import org.bukkit.Bukkit;
import java.util.UUID;
import pl.best241.rdbplugin.events.PubSubRecieveMessageEvent;
import org.bukkit.event.Listener;

public class PubSubRecieveMessageListener implements Listener
{
    @EventHandler
    public static void onMessageRecieve(final PubSubRecieveMessageEvent event) {
        final String channel = event.getPayload().getSubChannel();
        final String message = event.getMessage();
        if (channel.equals("ccDeathBans.addBanKick")) {
            final UUID uuid = UUID.fromString(message);
            Bukkit.getScheduler().runTask((Plugin)CcDeathBans.getPlugin(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    System.out.println("Recieved ban for " + uuid);
                    final Player player = Bukkit.getPlayer(uuid);
                    if (player == null || !player.isOnline()) {
                        return;
                    }
                    final Long banTime = RedisBackend.getPlayerBanTime(uuid);
                    if (banTime == null) {
                        return;
                    }
                    final String timeTo = TimeManager.getTimeTo(banTime, System.currentTimeMillis());
                    player.kickPlayer(MessagesData.youWillRespawnIn.replace("%time", timeTo));
                }
            });
        }
        else if (channel.equals("ccDeathBans.banTime")) {
            final Long time = Long.parseLong(message);
            TimeManager.setBanTimeInMillis(time);
        }
        else if (event.getChannel().equals("reloadAllMessagesRequest")) {
            MessagesData.loadMessages((Plugin)CcDeathBans.getPlugin());
            PubSub.broadcast("reloadAllMessagesResponse", CcDeathBans.getPlugin().getName());
        }
    }
}
