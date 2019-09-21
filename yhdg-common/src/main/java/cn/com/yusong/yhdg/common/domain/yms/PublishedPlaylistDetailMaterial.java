package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;

/**
 * Created by zhoub on 2017/8/9.
 */
public class PublishedPlaylistDetailMaterial extends LongIdEntity {

    Long detailId;
    Long materialId;
    Integer version;
    Integer orderNum;
    Integer duration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
