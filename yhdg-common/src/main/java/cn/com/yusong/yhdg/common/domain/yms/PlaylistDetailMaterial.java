package cn.com.yusong.yhdg.common.domain.yms;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;

public class PlaylistDetailMaterial extends LongIdEntity{

    Long materialId;
    Long detailId;
    Integer orderNum;
    Integer duration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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
}
