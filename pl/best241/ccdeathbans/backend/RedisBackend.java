// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans.backend;

import redis.clients.jedis.Jedis;
import pl.best241.rdbplugin.JedisFactory;
import java.util.UUID;

public class RedisBackend
{
    public static void addPlayerBanned(final UUID uuid, final long timeTo) {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        jedis.hset("ccDeathBans.playersBanned", uuid.toString(), String.valueOf(timeTo));
        JedisFactory.getInstance().returnJedis(jedis);
    }
    
    public static Long getPlayerBanTime(final UUID uuid) {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        final String hget = jedis.hget("ccDeathBans.playersBanned", uuid.toString());
        JedisFactory.getInstance().returnJedis(jedis);
        if (hget == null) {
            return null;
        }
        return Long.valueOf(hget);
    }
    
    public static void removePlayerBanned(final UUID uuid) {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        jedis.hdel("ccDeathBans.playersBanned", new String[] { uuid.toString() });
        JedisFactory.getInstance().returnJedis(jedis);
    }
    
    public static void setBanTime(final Long time) {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        jedis.set("ccDeathBans.banTime", time.toString());
        JedisFactory.getInstance().returnJedis(jedis);
    }
    
    public static long getBanTime() {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        final String get = jedis.get("ccDeathBans.banTime");
        JedisFactory.getInstance().returnJedis(jedis);
        if (get == null) {
            return 1800000L;
        }
        return Long.valueOf(get);
    }
}
