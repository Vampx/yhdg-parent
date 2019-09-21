package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Vip套餐关联设备表
 */
@Setter
@Getter
public class VipPriceCabinet extends LongIdEntity{

    String cabinetId;/*主键*/
    Long priceId; /*VIP 套餐Id*/
    @Transient
    String ids; //由逗号分割的Id字符串
    String cabinetName;
}
