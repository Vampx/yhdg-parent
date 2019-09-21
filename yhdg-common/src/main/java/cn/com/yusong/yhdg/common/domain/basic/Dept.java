package cn.com.yusong.yhdg.common.domain.basic;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 部门
 */
@Getter
@Setter
public class Dept extends IntIdEntity {

    Integer agentId; //代理商ID
    String deptName; //部门名称
    Integer parentId; //上级部门
    String memo;  //备注
    Date createTime; //创建时间
    @Transient
    String agentName; //运营商名称


}
