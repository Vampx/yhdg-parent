package cn.com.yusong.yhdg.common.domain.zc;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 租车VIP 套餐关联门店
 */
@Getter
@Setter
public class VehicleVipPriceShop extends IntIdEntity {
	String shopId;/*门店id*/
	Integer priceId;/*vip套餐id*/

	@Transient
	String ids; //由逗号分割的Id字符串
	String shopName;

}
