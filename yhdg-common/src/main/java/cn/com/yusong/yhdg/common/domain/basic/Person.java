package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Setter
@Getter
public class Person extends LongIdEntity {
    Integer agentId;//运营商id
    String mobile;//手机号码
    String password;//登陆密码
    String fullname;//全名
    String photoPath;//图像路径
    Integer gender;//性别
    Integer isActive;//是否启用
    Integer isAdmin;//1：是；0：否
    Integer isProtected;    /*1 受保护的 0 不受保护*/
    Integer isPush;
    String memo;//备注
    Date createTime;//最后登陆时间
    Date lastLoginTime;//最后登陆时间

    @Transient
    String permIds;
    String agentName;
    @Transient
    List<Part> exportList;
    List<Part> platformList;
    List<String> permIdList = new ArrayList<String>();
    Integer partId;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
