package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Setter
@Getter
public class ShopPriceSetting extends IntIdEntity {
    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;/*运营商编号*/
    String shopId; /*门店id*/
    String shopName;/*门店名称*/
    Integer priceSettingId;/*租车套餐设置id*/
    Date createTime;/*创建时间*/
    Integer vehicleCount;/*库存数*/

    @Transient
    Integer category;//类型 1换电 2租电
    Integer batteryCount;
    String vehicleName;
    String modelName;
    String modelId;
    Integer isActive;
    String settingName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
