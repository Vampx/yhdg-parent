package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 电芯规格
 */
@Setter
@Getter
public class BatteryCellFormat extends LongIdEntity {
	Long cellModelId;//电芯型号Id
	String cellMfr;//电芯厂家
	String cellModel;//电芯型号
	String cellFormatName; //电芯规格名称
	Double nominalCap;//组包容量 单位Ah
	Double minNominalCap;//组包容量最小范围 单位Ah
	Double maxNominalCap;//组包容量最大范围 单位Ah
	Double chgCutVol;//充电截至电压 单位V
	Double nominalVol;//标称电压 单位V
	Double minChgTemp;//充电温度最小范围 单位℃
	Double maxChgTemp;//充电温度最大范围 单位℃
	Double minDsgTemp;//放电温度最小范围 单位℃
	Double maxDsgTemp;//放电温度最大范围 单位℃
	Double acResistance;//交流内阻 单位mΩ
	Double minAcResistance;//交流内阻最小范围 单位℃
	Double maxAcResistance;//交流内阻最大范围 单位℃
	Double resilienceVol;//回弹电压 单位V
	Double minResilienceVol;//回弹电压最小范围 单位V
	Double maxResilienceVol;//回弹电压最大范围 单位V
	Double staticVol;//静置电压 单位V
	Double minStaticVol;//静置电压最小范围 单位V
	Double maxStaticVol;//静置电压最大范围 单位V
	Integer circle;//循环次数
	Double minCircle;/*循环次数最小范围*/
	Double maxCircle;/*循环次数最大范围*/
	String barcodeRule;//条码规则
	Double chgRate;//充电电流倍率 单位C
	Double chgCurrent;//充电电流 单位A
	Double chgTime;//充电时间 单位小时
	Double maxContinueChgCurrent;//最大持续充电电流 单位A
	Double maxContinueDsgCurrent;//最大持续放电电流 单位A
	String dsgCutVol;//放电截至电压
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
