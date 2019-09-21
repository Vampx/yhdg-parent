package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.tool.json.MobileSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 客户信息
 */
@Setter
@Getter
public class CustomerInfo extends LongIdEntity {

    String mobile;/*手机号*/
    String photoPath;/*照片路径*/
    String fullname;/*昵称*/
    Integer isActive;/*是否启用*/
    Integer registerType;/*登陆类型*/
    Integer pushType; //推送类型
    String pushToken; //推送token
    String mpOpenId;/*微信账号*/
    String fwOpenId;

    //@JsonSerialize(using = MobileSerializer.class)
    public String getMobile() {
        return mobile;
    }
}
