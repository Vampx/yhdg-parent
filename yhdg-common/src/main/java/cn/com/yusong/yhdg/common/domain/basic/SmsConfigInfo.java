package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsConfigInfo extends IntIdEntity {

    Integer partnerId;
    Integer smsType;
    String configName;
    String account;
    String password;
    Integer isActive;
    String sign; //短信签名
    Integer signPlace; //签名位置 左边 右边

    String partnerName;


}
