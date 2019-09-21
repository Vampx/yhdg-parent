package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 换电Vip关联门店
 */
@Setter
@Getter
public class VipPriceShop extends LongIdEntity{

    String shopId;/*主键*/
    Long priceId; /*VIP 套餐Id*/
    @Transient
    String ids; //由逗号分割的Id字符串
    String shopName;
}
