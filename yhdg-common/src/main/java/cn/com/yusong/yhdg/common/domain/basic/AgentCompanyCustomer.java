package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Setter
@Getter
public class AgentCompanyCustomer extends PageEntity {

    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCompanyId;/*运营公司id*/
    Long customerId;/*骑手id*/
    String customerMobile;/*骑手手机*/
    String customerFullname;/*骑手名称*/
    Date createTime;

    @Transient
    Integer unbindCompanyFlag;
    String agentCompanyName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
