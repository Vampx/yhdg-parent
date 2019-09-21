package cn.com.yusong.yhdg.common.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class NewBoxNum implements Serializable {
    public String serverName;
    public String cabinetId;
    public String orderId;
    public String oldBoxNum;
    public String newBoxNum;
    public Date putTime;

    public NewBoxNum() {
    }

    public NewBoxNum(String serverName, String cabinetId, String orderId, String oldBoxNum, String newBoxNum, Date putTime) {
        this.serverName = serverName;
        this.cabinetId = cabinetId;
        this.orderId = orderId;
        this.oldBoxNum = oldBoxNum;
        this.newBoxNum = newBoxNum;
        this.putTime = putTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
