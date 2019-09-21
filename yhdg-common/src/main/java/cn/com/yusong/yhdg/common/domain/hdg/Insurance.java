package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电池类型保费
 */
@Setter
@Getter
public class Insurance extends LongIdEntity {

	Integer agentId;//运营商id
	Integer batteryType;/*电池类型*/
	String insuranceName;//保险名称
	Integer price;//价格
	Integer paid;//保额
	Integer monthCount;//时长
	Integer isActive;/*是否有效*/
	String memo;
	Date createTime;//创建时间

	@Transient
	String agentName, batteryTypeName;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
