// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccdeathbans.managers;

public class TimeManager
{
    private static long banTimeInMillis;
    
    public static String getTimeTo(final long timeTo, final long timeForm) {
        long diff = timeTo - timeForm;
        long seconds = diff / 1000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        final long days = hours / 24L;
        diff -= seconds * 1000L;
        seconds -= minutes * 60L;
        minutes -= hours * 60L;
        hours -= days * 24L;
        String time = "";
        if (days != 0L) {
            time = time + days + "d ";
        }
        if (hours != 0L) {
            time = time + hours + "h ";
        }
        if (minutes != 0L) {
            time = time + minutes + "m ";
        }
        if (seconds != 0L) {
            time = time + seconds + "s ";
        }
        return time.substring(0, time.length() - 1);
    }
    
    public static long getBanTimeInMillis() {
        return TimeManager.banTimeInMillis + System.currentTimeMillis();
    }
    
    public static long getKickTimeInSeconds() {
        return 1L;
    }
    
    public static void setBanTimeInMillis(final long banTime) {
        TimeManager.banTimeInMillis = banTime;
    }
}
