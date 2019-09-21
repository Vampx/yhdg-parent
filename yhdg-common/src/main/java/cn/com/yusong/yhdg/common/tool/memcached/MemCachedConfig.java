package cn.com.yusong.yhdg.common.tool.memcached;


public class MemCachedConfig {

    public static final int CACHE_TEN_SECOND = 10;
    public static final int CACHE_ONE_HOUR = 3600;
    public static final int CACHE_TWO_HOUR = 3600 * 2;
    public static final int CACHE_ONE_DAY = 3600 * 24;
    public static final int CACHE_THREE_DAY = 3600 * 24 * 3;
    public static final int CACHE_ONE_MINUTE = 60 * 1;
    public static final int CACHE_THREE_MINUTE = 60 * 3;
    public static final int CACHE_FIVE_MINUTE = 60 * 5;
    public static final int CACHE_TWO_MINUTE = 60 * 2;
    public static final int CACHE_15_MINUTE = 60 * 15;
    public static final int CACHE_ONE_WEEK = 3600 * 24 * 7;
    public static final int CACHE_ONE_YEAR = 3600 * 24 * 365;

    String servers;

    public MemCachedConfig(String servers) {
        this.servers = servers;
    }


}
