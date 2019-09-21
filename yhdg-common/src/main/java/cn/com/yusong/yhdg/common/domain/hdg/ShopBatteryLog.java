package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopBatteryLog extends IntIdEntity {

    Integer agentId;
    String agentName;
    String agentCode;
    String shopId;/*门店Id*/
    String shopName; /*门店名称*/
    Integer category; /*1 换电 2 租电*/
    String batteryId; /*电池id*/
    Integer action; /*1收入 2 支出*/
    String memo;/*操作说明*/
}
