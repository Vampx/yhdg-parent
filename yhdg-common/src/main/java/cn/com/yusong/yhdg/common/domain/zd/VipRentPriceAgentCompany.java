package cn.com.yusong.yhdg.common.domain.zd;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 租电Vip关联运营公司
 */
@Setter
@Getter
public class VipRentPriceAgentCompany extends LongIdEntity{

    String agentCompanyId;/*主键*/
    Long priceId; /*VIP 套餐Id*/
    @Transient
    String ids; //由逗号分割的Id字符串
    String companyName;/*运营公司名称*/
}
