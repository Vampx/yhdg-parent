package cn.com.yusong.yhdg.common.domain.zd;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * Vip套餐客户
 */
@Setter
@Getter
public class VipRentPriceCustomer extends LongIdEntity{

    Long priceId;/*主键*/
    String mobile; /*手机号*/
    Date createTime;

    Integer agentId;
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
