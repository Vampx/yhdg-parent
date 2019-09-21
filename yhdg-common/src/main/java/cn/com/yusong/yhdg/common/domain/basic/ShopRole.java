package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2019/6/24.
 */
@Getter
@Setter
public class ShopRole extends IntIdEntity {
    String shopId;   //门店ID
    String roleName; //角色名称
    String memo; //备注
    Date createTime; //创建时间

    @Transient
    List<String> permList = new ArrayList<String>(); //角色保护的oper
    String shopName;
    Integer agentId;
    String agentName;
}
