package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电芯型号
 */
@Setter
@Getter
public class BatteryCellModel extends LongIdEntity {
	String cellMfr;//电芯厂家
	String cellModel;//电芯型号
	String memo;//备注
	String operator;//操作人
	Date createTime;//创建时间

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
