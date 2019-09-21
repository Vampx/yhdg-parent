package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 系统电池类型
 *
 */
@Setter
@Getter
public class SystemBatteryType extends IntIdEntity {

	String typeName;//类型名称
	Integer isActive;//是否启用
	String memo;//备注
	Integer ratedVoltage;//额定电压
	Integer ratedCapacity;//额定容量
	Date createTime;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}
}
