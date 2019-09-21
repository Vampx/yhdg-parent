package cn.com.yusong.yhdg.common.domain.zc;


import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
/**
 * 车辆型号
 */
@Getter
@Setter
public class VehicleModel extends IntIdEntity {


	Integer agentId;/*运营商id*/
	String agentName;/*运营商名称*/
	String agentCode;/*运营商编号*/
	String modelName;/*车型名称*/
	Integer isActive;/*是否有效*/
	String modelImagePath;/*车型外观图片*/

	String viewImagePath1;/*详情页浏览图片*/
	String viewImagePath2;/*详情页浏览图片*/
	String viewImagePath3;/*详情页浏览图片*/
	String viewImagePath4;/*详情页浏览图片*/

	String productImagePath1;/*详情页产品图片*/
	String productImagePath2;/*详情页产品图片*/
	String productImagePath3;/*详情页产品图片*/
	String productImagePath4;/*详情页产品图片*/
	String productImagePath5;/*详情页产品图片*/
	String productImagePath6;/*详情页产品图片*/

	String afterSaleImagePath1;/*售后标准图片*/
	String afterSaleImagePath2;/*售后标准图片*/
	String afterSaleImagePath3;/*售后标准图片*/
	String afterSaleImagePath4;/*售后标准图片*/
	String afterSaleImagePath5;/*售后标准图片*/
	String afterSaleImagePath6;/*售后标准图片*/

	String faqImagePath1;/*问题解答图片*/
	String faqImagePath2;/*问题解答图片*/
	String faqImagePath3;/*问题解答图片*/
	String faqImagePath4;/*问题解答图片*/
	String faqImagePath5;/*问题解答图片*/
	String faqImagePath6;/*问题解答图片*/

	String memo;/*备注*/
	Date createTime;/*创建时间*/

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

}
