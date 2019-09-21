package cn.com.yusong.yhdg.common.tool.weixin;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.util.json.WxMaGsonBuilder;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.util.http.apache.ApacheHttpClientBuilder;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于Memcached的微信配置provider，在实际生产环境中应该将这些配置持久化
 * https://github.com/binarywang/weixin-java-miniapp-demo/blob/master/src/main/java/com/github/binarywang/demo/wx/miniapp/config/WxMaConfiguration.java
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxMaMemcachedConfig implements WxMaConfig {
  protected volatile String msgDataFormat;
  protected volatile String appid;
  protected volatile String secret;
  protected volatile String token;
  protected volatile String aesKey;
  protected volatile long expiresTime;

  protected volatile String httpProxyHost;
  protected volatile int httpProxyPort;
  protected volatile String httpProxyUsername;
  protected volatile String httpProxyPassword;


  protected Lock accessTokenLock = new ReentrantLock();
  protected Lock jsapiTicketLock = new ReentrantLock();
  protected Lock cardApiTicketLock = new ReentrantLock();

  private int id;

  private String accessTokenKey;
  private String expiresTimeKey;

  private String jsapiTicketKey;
  private String jsapiTicketExpiresTimeKey;

  protected String cardApiTicketKey;
  protected String cardApiTicketExpiresTimeKey;

  MemCachedClient memCachedClient;

  public void setValue(String property, Object value) {
    memCachedClient.set(property, value, MemCachedConfig.CACHE_ONE_YEAR);
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

  public WxMaMemcachedConfig(MemCachedClient memCachedClient, int id) {
    this.memCachedClient = memCachedClient;
    this.id = id;

    accessTokenKey = String.format("ma-cfg:%d:1", id);
    expiresTimeKey = String.format("ma-cfg:%d:2", id);

    jsapiTicketKey = String.format("ma-cfg:%d:3", id);
    jsapiTicketExpiresTimeKey = String.format("ma-cfg:%d:4", id);

    cardApiTicketKey = String.format("ma-cfg:%d:5", id);
    cardApiTicketExpiresTimeKey = String.format("ma-cfg:%d:6", id);
  }

  /**
   * 临时文件目录
   */
  protected volatile File tmpDirFile;

  protected volatile ApacheHttpClientBuilder apacheHttpClientBuilder;

  @Override
  public Lock getAccessTokenLock() {
    return this.accessTokenLock;
  }

  @Override
  public Lock getJsapiTicketLock() {
    return this.jsapiTicketLock;
  }

  @Override
  public Lock getCardApiTicketLock() {
    return this.cardApiTicketLock;
  }

  @Override
  public String getAccessToken() {
    return getStringValue(accessTokenKey);
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
  public void expireAccessToken() {
    setValue(expiresTimeKey, 0L);
  }

  @Override
  public String getJsapiTicket() {
    return getStringValue(jsapiTicketKey);
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
  public boolean isJsapiTicketExpired() {
    return System.currentTimeMillis() > getJsapiTicketExpiresTime();
  }

  @Override
  public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
    setJsapiTicket(jsapiTicket);
    setJsapiTicketExpiresTime(System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l);
  }

  @Override
  public void expireJsapiTicket() {
    setJsapiTicketExpiresTime(0);
  }

  /**
   * 卡券api_ticket
   */
  @Override
  public String getCardApiTicket() {
    return getStringValue(cardApiTicketKey);
  }

  @Override
  public boolean isCardApiTicketExpired() {
    return System.currentTimeMillis() > getLongValue(cardApiTicketExpiresTimeKey);
  }

  @Override
  public synchronized void updateCardApiTicket(String cardApiTicket, int expiresInSeconds) {
    setValue(cardApiTicketKey, cardApiTicket);
    setValue(cardApiTicketExpiresTimeKey, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l);
  }

  @Override
  public void expireCardApiTicket() {
    setValue(cardApiTicketExpiresTimeKey, 0);
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
  public String getSecret() {
    return this.secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  @Override
  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public String getAesKey() {
    return this.aesKey;
  }

  public void setAesKey(String aesKey) {
    this.aesKey = aesKey;
  }

  @Override
  public String getMsgDataFormat() {
    return this.msgDataFormat;
  }

  public void setMsgDataFormat(String msgDataFormat) {
    this.msgDataFormat = msgDataFormat;
  }

  @Override
  public String getHttpProxyHost() {
    return this.httpProxyHost;
  }

  public void setHttpProxyHost(String httpProxyHost) {
    this.httpProxyHost = httpProxyHost;
  }

  @Override
  public int getHttpProxyPort() {
    return this.httpProxyPort;
  }

  public void setHttpProxyPort(int httpProxyPort) {
    this.httpProxyPort = httpProxyPort;
  }

  @Override
  public String getHttpProxyUsername() {
    return this.httpProxyUsername;
  }

  public void setHttpProxyUsername(String httpProxyUsername) {
    this.httpProxyUsername = httpProxyUsername;
  }

  @Override
  public String getHttpProxyPassword() {
    return this.httpProxyPassword;
  }

  public void setHttpProxyPassword(String httpProxyPassword) {
    this.httpProxyPassword = httpProxyPassword;
  }

  @Override
  public String toString() {
    return WxMaGsonBuilder.create().toJson(this);
  }

  @Override
  public ApacheHttpClientBuilder getApacheHttpClientBuilder() {
    return this.apacheHttpClientBuilder;
  }

  public void setApacheHttpClientBuilder(ApacheHttpClientBuilder apacheHttpClientBuilder) {
    this.apacheHttpClientBuilder = apacheHttpClientBuilder;
  }

  @Override
  public boolean autoRefreshToken() {
    return true;
  }

  @Override
  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }
}