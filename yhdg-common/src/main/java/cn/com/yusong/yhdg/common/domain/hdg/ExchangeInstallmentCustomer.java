package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeInstallmentCustomer extends PageEntity {
    Long settingId;/*换电分期设置ID*/
    Long customerId;
    String customerMobile;
    String customerFullname;
}
