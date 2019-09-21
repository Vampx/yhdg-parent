package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户押金退款详情记录
 */
public class CustomerForegiftRefundDetailed extends StringIdEntity {
    Integer refundType;             //退款类型
    Integer num;                    //退款编号
    Integer refundMoney;            //退款金额
    String refundPhoto;             //退款照片
    String refundOperator;          //退款人
    Date createTime;

    public String getRefundTypeName() {
        if(refundType != null) {
            return ConstEnum.PayType.getName(refundType);
        }
        return "";
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Integer refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundPhoto() {
        return refundPhoto;
    }

    public void setRefundPhoto(String refundPhoto) {
        this.refundPhoto = refundPhoto;
    }

    public String getRefundOperator() {
        return refundOperator;
    }

    public void setRefundOperator(String refundOperator) {
        this.refundOperator = refundOperator;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
