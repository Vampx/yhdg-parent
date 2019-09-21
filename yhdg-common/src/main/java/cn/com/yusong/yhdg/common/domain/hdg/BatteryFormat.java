package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 电池规格
 */
@Setter
@Getter
public class BatteryFormat extends LongIdEntity {

	Long cellModelId;//电芯型号Id
	String batteryFormatName;//电池规格名称
	String cellMfr;//电芯厂家
	String cellModel ;//电芯型号
	Double nominalPow;//标称容量 单位Ah
	Double nominalCap;//组包容量 单位Ah
	Double minNominalCap;//组包容量最小范围 单位Ah
	Double maxNominalCap;//组包容量最大范围 单位Ah
	Double acResistance;//交流内阻 单位mΩ
	Double minAcResistance;//交流内阻最小范围 单位℃
	Double maxAcResistance;//交流内阻最大范围 单位℃
	Double resilienceVol;//回弹电压 单位V
	Double minResilienceVol;//回弹电压最小范围 单位V
	Double maxResilienceVol;//回弹电压最大范围 单位V
	Double staticVol;//静置电压 单位V
	Double minStaticVol;//静置电压最小范围 单位V
	Double maxStaticVol;//静置电压最大范围 单位V
	String barcodeRule;//条码规则
	Integer circle;//循环次数
	Double minCircle;/*循环次数最小范围*/
	Double maxCircle;/*循环次数最大范围*/
	Integer cellCount;/*电芯串数*/
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
