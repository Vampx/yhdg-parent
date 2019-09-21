package cn.com.yusong.yhdg.common.tool.weixin;

import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.enums.TicketType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.concurrent.locks.Lock;

public class WxMpMemcachedConfigStorage implements WxMpConfigStorage {

    private String accessTokenKey;
    private String expiresTimeKey;

    private String jsapiTicketKey;
    private String jsapiTicketExpiresTimeKey;

    private String cardApiTicketKey;
    private String cardApiTicketExpiresTime;

    protected volatile String key;
    protected volatile String appId;
    protected volatile String secret;
    protected volatile String partnerId;
    protected volatile String partnerKey;
    protected volatile String token;
    protected volatile String aesKey;

    protected volatile String oauth2redirectUri;

    protected volatile String httpProxyHost;
    protected volatile int httpProxyPort;
    protected volatile String httpProxyUsername;
    protected volatile String httpProxyPassword;

    public WxMpMemcachedConfigStorage(String key) {
        this.key = key;

        accessTokenKey = String.format("wx-cfg:%s:1", key);
        expiresTimeKey = String.format("wx-cfg:%s:2", key);

        jsapiTicketKey = String.format("wx-cfg:%s:3", key);
        jsapiTicketExpiresTimeKey = String.format("wx-cfg:%s:4", key);

        cardApiTicketKey = String.format("wx-cfg:%s:5", key);
        cardApiTicketExpiresTime = String.format("wx-cfg:%s:6", key);
    }

    /**
     * 临时文件目录
     */
    protected volatile File tmpDirFile;

    protected volatile SSLContext sslContext;

    protected volatile ApacheHttpClientBuilder apacheHttpClientBuilder;

    MemCachedClient memCachedClient;

    public void setValue(String property, Object value) {
        memCachedClient.set(property, value, MemCachedConfig.CACHE_ONE_DAY);
    }

    public Object getValue(String property) {
        return memCachedClient.get(property);
    }

    public String getStringValue(String property) {
        return (String) getValue(property);
    }

    public int getIntValue(String property) {
        Integer result = (Integer) getValue(property);
        return result == null ? 0 : result;
    }

    public long getLongValue(String property) {
        Long result = (Long) getValue(property);
        return result == null ? 0 : result;
    }

    @Override
    public String getAccessToken() {
        return getStringValue(accessTokenKey);
    }

    @Override
    public Lock getAccessTokenLock() {
        return null;
    }

    @Override
    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > getLongValue(expiresTimeKey);
    }

    @Override
    public synchronized void updateAccessToken(WxAccessToken accessToken) {
        updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        setValue(accessTokenKey, accessToken);
        setValue(expiresTimeKey, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l);
    }

    @Override
    public String getTicket(TicketType type) {
        return null;
    }

    @Override
    public Lock getTicketLock(TicketType type) {
        return null;
    }

    @Override
    public void expireAccessToken() {
        setValue(expiresTimeKey, 0L);
    }


    public void setJsapiTicket(String jsapiTicket) {
        setValue(jsapiTicketKey, jsapiTicket);
    }

    public long getJsapiTicketExpiresTime() {
        return getLongValue(jsapiTicketExpiresTimeKey);
    }

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        setValue(jsapiTicketExpiresTimeKey, jsapiTicketExpiresTime);
    }

    @Override
    public boolean isTicketExpired(TicketType type) {
        return System.currentTimeMillis() > getJsapiTicketExpiresTime();
    }

    @Override
    public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        setJsapiTicket(jsapiTicket);
        setJsapiTicketExpiresTime(System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l);
    }

    @Override
    public void expireTicket(TicketType type) {
        setJsapiTicketExpiresTime(0);
    }

    @Override
    public long getExpiresTime() {
        return getLongValue(expiresTimeKey);
    }

    public void setAccessToken(String accessToken) {
        setValue(accessTokenKey, accessToken);
    }

    public void setExpiresTime(long expiresTime) {
        setValue(expiresTimeKey, expiresTime);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }


    @Override
    public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
        return this.apacheHttpClientBuilder;
    }

    @Override
    public boolean autoRefreshToken() {
        return false;
    }

    public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
        this.apacheHttpClientBuilder = apacheHttpClientBuilder;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }


    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public void setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getAesKey() {
        return aesKey;
    }

    @Override
    public String getTemplateId() {
        return null;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    @Override
    public String getOauth2redirectUri() {
        return oauth2redirectUri;
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
    }

    @Override
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    @Override
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    @Override
    public String getHttpProxyUsername() {
        return httpProxyUsername;
    }

    public void setHttpProxyUsername(String httpProxyUsername) {
        this.httpProxyUsername = httpProxyUsername;
    }

    @Override
    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    @Override
    public File getTmpDirFile() {
        return tmpDirFile;
    }

    public void setTmpDirFile(File tmpDirFile) {
        this.tmpDirFile = tmpDirFile;
    }

    public SSLContext getSSLContext() {
        return sslContext;
    }

    public void setSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public MemCachedClient getMemCachedClient() {
        return memCachedClient;
    }

    public void setMemCachedClient(MemCachedClient memCachedClient) {
        this.memCachedClient = memCachedClient;
    }
}
