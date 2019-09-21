package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电池条码
 */
@Setter
@Getter
public class BatteryBarcode extends LongIdEntity {
	Long batteryFormatId;//电池规格Id
	String cellMfr;//电芯厂家
	String cellModel ;//电芯型号
	String barcode;//条码
	String operator;//操作人
	Date createTime;//创建时间

	@Transient
	Integer codeCount;//条码数量
	String barcodeRule;//条码规则

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
