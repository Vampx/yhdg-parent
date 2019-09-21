package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.zc.VehicleModelService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller("api_v1_customer_zc_vehicle_model")
@RequestMapping(value = "/api/v1/customer/zc/vehicle_model")
public class VehicleModelController extends ApiController {
	static final Logger log = LogManager.getLogger(VehicleModelController.class);

	@Autowired
	VehicleModelService vehicleModelService;
	@Autowired
	CustomerService customerService;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailParam {
		public int id;
	}

	/**
	 * 17-根据车型查询广告图片
	 * <p>
	 * detail
	 */

	@ResponseBody
	@RequestMapping(value = "/detail.htm")
	public RestResult couponTicketList(@Valid @RequestBody DetailParam param) {
		VehicleModel vehicleModel = vehicleModelService.find(param.id);
		if (vehicleModel == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "车型不存在");
		}
		NotNullMap data = new NotNullMap();
		data.put("id", vehicleModel.getId());
		data.put("modelName", vehicleModel.getModelName());
		List<String> viewImageList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(vehicleModel.getViewImagePath1())) {
			viewImageList.add(staticImagePath(vehicleModel.getViewImagePath1()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getViewImagePath2())) {
			viewImageList.add(staticImagePath(vehicleModel.getViewImagePath2()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getViewImagePath3())) {
			viewImageList.add(staticImagePath(vehicleModel.getViewImagePath3()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getViewImagePath4())) {
			viewImageList.add(staticImagePath(vehicleModel.getViewImagePath4()));
		}
		List<String> productImageList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(vehicleModel.getProductImagePath1())) {
			productImageList.add(staticImagePath(vehicleModel.getProductImagePath1()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getProductImagePath2())) {
			productImageList.add(staticImagePath(vehicleModel.getProductImagePath2()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getProductImagePath3())) {
			productImageList.add(staticImagePath(vehicleModel.getProductImagePath3()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getProductImagePath4())) {
			productImageList.add(staticImagePath(vehicleModel.getProductImagePath4()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getProductImagePath5())) {
			productImageList.add(staticImagePath(vehicleModel.getProductImagePath5()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getProductImagePath6())) {
			productImageList.add(staticImagePath(vehicleModel.getProductImagePath6()));
		}
		List<String> afterSaleImageList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(vehicleModel.getAfterSaleImagePath1())) {
			afterSaleImageList.add(staticImagePath(vehicleModel.getAfterSaleImagePath1()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getAfterSaleImagePath2())) {
			afterSaleImageList.add(staticImagePath(vehicleModel.getAfterSaleImagePath2()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getAfterSaleImagePath3())) {
			afterSaleImageList.add(staticImagePath(vehicleModel.getAfterSaleImagePath3()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getAfterSaleImagePath4())) {
			afterSaleImageList.add(staticImagePath(vehicleModel.getAfterSaleImagePath4()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getAfterSaleImagePath5())) {
			afterSaleImageList.add(staticImagePath(vehicleModel.getAfterSaleImagePath5()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getAfterSaleImagePath5())) {
			afterSaleImageList.add(staticImagePath(vehicleModel.getAfterSaleImagePath6()));
		}
		List<String> faqImageList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(vehicleModel.getFaqImagePath1())) {
			faqImageList.add(staticImagePath(vehicleModel.getFaqImagePath1()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getFaqImagePath2())) {
			faqImageList.add(staticImagePath(vehicleModel.getFaqImagePath2()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getFaqImagePath3())) {
			faqImageList.add(staticImagePath(vehicleModel.getFaqImagePath3()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getFaqImagePath4())) {
			faqImageList.add(staticImagePath(vehicleModel.getFaqImagePath4()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getFaqImagePath5())) {
			faqImageList.add(staticImagePath(vehicleModel.getFaqImagePath5()));
		}
		if (StringUtils.isNotEmpty(vehicleModel.getFaqImagePath6())) {
			faqImageList.add(staticImagePath(vehicleModel.getFaqImagePath6()));
		}
		data.put("viewImageList", viewImageList);
		data.put("productImageList", productImageList);
		data.put("afterSaleImageList", afterSaleImageList);
		data.put("faqImageList", faqImageList);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}
}
