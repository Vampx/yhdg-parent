package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *分发服务器
 */
public class FrontServer extends LongIdEntity {
    String serverName;
    String version;
    String ip;
    int port;
    int ftpPort;
    int downloadCount;
    String apUrl;
    String ftpEncoding;
    String ftpUser;
    String ftpPassword;

    @Transient
    Date heartTime;

    @Transient
    Integer isOnline;

    @Transient
   Float downloadProgress;

    @Transient
    Float downloadSpeed;


    public Float getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(Float downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public Float getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(Float downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getApUrl() {
        return apUrl;
    }

    public void setApUrl(String apUrl) {
        this.apUrl = apUrl;
    }

    public String getFtpEncoding() {
        return ftpEncoding;
    }

    public void setFtpEncoding(String ftpEncoding) {
        this.ftpEncoding = ftpEncoding;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
}
