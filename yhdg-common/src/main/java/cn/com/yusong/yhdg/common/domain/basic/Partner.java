package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Partner extends IntIdEntity {

    String partnerName; /*商户名称*/

    String mpAppName;
    String mpAppId;
    String mpAppSecret;
    String mpPartnerCode;
    String mpPartnerKey;

    String maAppName;
    String maAppId;
    String maAppSecret;
    String maPartnerCode;
    String maPartnerKey;

    String fwAppName;
    String fwAppId;
    String fwPubKey;
    String fwPriKey;
    String fwAliKey;

    String alipayName;
    String alipayAppId;
    String alipayPubKey;
    String alipayPriKey;
    String alipayAliKey;

    String weixinName;
    String weixinAppId;
    String weixinPartnerCode;
    String weixinPartnerKey;

    Date createTime;
}
