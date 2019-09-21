package cn.com.yusong.yhdg.common.domain.basic;
import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 *运营商收入支出日统计表
 */
@Setter
@Getter
public class AgentDayInOutMoney extends PageEntity {
    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;
    String statsDate; /*统计日期 格式2017-01-01*/

    
    Integer agentInMoney;
    Integer agentOutMoney;

    Date updateTime;



    public void init() {

        agentInMoney = 0;
        agentOutMoney = 0;

        updateTime = new Date();
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

}