package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.utils.AppUtils;

import java.util.Date;

public class PublishedMaterial {

    Long materialId;
    Integer agentId; //代理商id
    Long groupId; //素材组Id
    Integer materialType; //界面类型
    String materialName; //素材名称
    Integer duration; //持续时间
    String coverPath; //封面路径
    Integer convertStatus;//视频转换状态
    Integer convertProgress;//视频转换进度
    String filePath; //文件路径
    Long size; //大小
    Integer width; //宽度
    Integer height; //高度
    Long ownerId;//业主ID
    String ownerName; //业主名称
    Date createTime;//创建时间
    String md5Sum;//md5签名算法
    int version;//版本

    @Transient
    Integer orderNum;

    public void copy(Material material) {
        setMaterialId(material.getId());
        setAgentId(material.getAgentId());
        setGroupId(material.getGroupId());
        setMaterialType(material.getMaterialType());
        setMaterialName(material.getMaterialName());
        setDuration(material.getDuration());
        setCoverPath(material.getCoverPath());
        setConvertStatus(material.getConvertStatus());
        setConvertProgress(material.getConvertProgress());
        setFilePath(material.getFilePath());
        setSize(material.getSize());
        setWidth(material.getWidth());
        setHeight(material.getHeight());
        setOwnerId(material.getOwnerId());
        setOwnerName(material.getOwnerName());
        setCreateTime(material.getCreateTime());
        setMd5Sum(material.getMd5Sum());
        setVersion(material.getVersion());
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Integer getConvertStatus() {
        return convertStatus;
    }

    public void setConvertStatus(Integer convertStatus) {
        this.convertStatus = convertStatus;
    }

    public Integer getConvertProgress() {
        return convertProgress;
    }

    public void setConvertProgress(Integer convertProgress) {
        this.convertProgress = convertProgress;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFtpPath() {
        return AppUtils.convertFtpPath(filePath);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMd5Sum() {
        return md5Sum;
    }

    public void setMd5Sum(String md5Sum) {
        this.md5Sum = md5Sum;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
