package cn.com.yusong.yhdg.common.domain.zd;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 租电Vip关联门店
 */
@Setter
@Getter
public class VipRentPriceShop extends LongIdEntity{

    String shopId;/*主键*/
    Long priceId; /*VIP 套餐Id*/
    @Transient
    String ids; //由逗号分割的Id字符串
    String shopName;
}
