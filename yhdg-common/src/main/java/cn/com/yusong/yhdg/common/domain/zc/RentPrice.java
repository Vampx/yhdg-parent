package cn.com.yusong.yhdg.common.domain.zc;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import lombok.Getter;
import lombok.Setter;
/**
 * 租车套餐
 */
@Getter
@Setter
public class RentPrice extends LongIdEntity {

	String priceName;/*套餐名称*/
	Long priceSettingId;/*租车套餐设置id*/
	Integer batteryType;/*电池型号*/
	Integer agentId; /*运营商id*/
	String agentName; /*运营商名称*/
	String agentCode;/*运营商编号*/
	Integer foregiftPrice; /*押金金额*/

	Integer vehicleForegiftPrice; /*车辆押金金额*/
	Integer batteryForegiftPrice; /*电池押金金额*/
	Integer rentPrice;/*租金金额*/
	Integer dayCount;/*天数*/
	Integer vehicleRentPrice; /*车辆租金金额*/
	Integer batteryRentPrice;/*电池租金金额*/
	String memo;/*备注*/
	@Transient
	Integer modelId;/*车型*/
	Integer minPrice;/*最小金额*/
	Integer maxPrice;/*最大金额*/
	String modelImagePath;/*车型外观图*/
	Integer category;/*1 换电 2 租电 3不租电*/

}
