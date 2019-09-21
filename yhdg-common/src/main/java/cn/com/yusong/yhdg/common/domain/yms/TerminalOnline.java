package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *   终端监控
 */
public class TerminalOnline extends StringIdEntity {

    Float cpu;
    Float memory;
    Integer playVolume;
    Integer playMode;
    Long cardCapacity;      //SD卡容量
    Long restCapacity;      //SD卡剩余数量
    Date heartTime;         //最后连接时间
    Integer newworkSignal;
    Integer isNormal;       //正常标志
    Integer isOnline;       //在线
    String statusInfo;      //状态信息
    String pageUid;
    String strategyUid;
    String playFile;        // 播放的素材名称
    Float speed;
    Float downloadProgress;

    @Transient
    String version;
    @Transient
    String terminalName;
    @Transient
    Integer agentId;
    @Transient
    Date date ;
    @Transient
    String strategyName; //策略名称
    @Transient
    String descendant;
    @Transient
    String playlistName; //播放列表名称
    @Transient
    String refreshTime;
    @Transient
    Integer groupId; //换电柜分组
    @Transient
    String cabinetName; //换电柜名称
    @Transient
    String address; //换电柜地址
    @Transient
    String cabinetId; //换电柜id
    @Transient
    String agentName; //运营商名称


    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getCpu() {
        return cpu;
    }

    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    public Float getMemory() {
        return memory;
    }

    public void setMemory(Float memory) {
        this.memory = memory;
    }

    public Integer getNewworkSignal() {
        return newworkSignal;
    }

    public void setNewworkSignal(Integer newworkSignal) {
        this.newworkSignal = newworkSignal;
    }

    public Integer getPlayVolume() {
        return playVolume;
    }

    public void setPlayVolume(Integer playVolume) {
        this.playVolume = playVolume;
    }

    public Integer getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Integer playMode) {
        this.playMode = playMode;
    }

    public Long getCardCapacity() {
        return cardCapacity;
    }

    public void setCardCapacity(Long cardCapacity) {
        this.cardCapacity = cardCapacity;
    }

    public Long getRestCapacity() {
        return restCapacity;
    }

    public void setRestCapacity(Long restCapacity) {
        this.restCapacity = restCapacity;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public Integer getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(Integer isNormal) {
        this.isNormal = isNormal;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Float getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(Float downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getPageUid() {
        return pageUid;
    }

    public void setPageUid(String pageUid) {
        this.pageUid = pageUid;
    }

    public String getStrategyUid() {
        return strategyUid;
    }

    public void setStrategyUid(String strategyUid) {
        this.strategyUid = strategyUid;
    }

    public String getPlayFile() {
        return playFile;
    }

    public void setPlayFile(String playFile) {
        this.playFile = playFile;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
