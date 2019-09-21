package cn.com.yusong.yhdg.common.domain.hdg;

import lombok.Getter;
import lombok.Setter;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * hdg_collection_address 实体类
 * PAN
 */ 
@Setter
@Getter
public class CollectionAddress extends PageEntity {
	public Long addressId;
	public Long customerId;
	public Date reportTime;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getReportTime() {
		return reportTime;
	}
}

