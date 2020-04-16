// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans.api;

import pl.best241.ccdeathbans.backend.RedisBackend;
import pl.best241.ccdeathbans.managers.TimeManager;
import java.util.UUID;
import org.bukkit.entity.Player;

public class CcDeathBansAPI
{
    public static void banPlayerDeath(final Player player) {
        banPlayerDeath(player.getUniqueId());
    }
    
    public static void banPlayerDeath(final UUID uuid) {
        RedisBackend.addPlayerBanned(uuid, TimeManager.getBanTimeInMillis());
    }
}
