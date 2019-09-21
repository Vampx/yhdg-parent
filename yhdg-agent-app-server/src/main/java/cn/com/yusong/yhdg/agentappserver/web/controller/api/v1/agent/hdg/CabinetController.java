package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerExchangeInfoService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Controller("agent_api_v1_agent_hdg_cabinet")
@RequestMapping("/agent_api/v1/agent/hdg/cabinet")
public class CabinetController extends ApiController {

	static final Logger log = LogManager.getLogger(CabinetController.class);

	@Autowired
	CabinetService cabinetService;
	@Autowired
	CabinetDayStatsService cabinetDayStatsService;
	@Autowired
	UserService userService;
	@Autowired
	CabinetCodeService cabinetCodeService;
	@Autowired
	FaultLogService faultLogService;
	@Autowired
	CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
	@Autowired
	CabinetMonthStatsService cabinetMonthStatsService;
	@Autowired
	AgentMonthStatsService agentMonthStatsService;
	@Autowired
	CustomerExchangeInfoService customerExchangeInfoService;
	@Autowired
	CabinetDegreeInputService cabinetDegreeInputService;
	@Autowired
	CabinetTotalStatsService cabinetTotalStatsService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public String keyword;
		public int offset;
		public int limit;
	}
	/**
	 * 7-查询换电柜列表(运营商)
	 *
	 **/
	@ResponseBody
	@RequestMapping(value = "/list")
	public RestResult list(@Valid @RequestBody ListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Cabinet> list = cabinetService.findList(agentId, null, param.keyword, param.offset, param.limit);

		String  statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);

		List<Map> result = new ArrayList<Map>();
		for (Cabinet cabinet : list) {
			Map line = new HashMap();
			line.put("id", cabinet.getId());
			line.put("cabinetName", cabinet.getCabinetName());
			line.put("address", cabinet.getAddressName());
			line.put("chargeFullVolume", cabinet.getChargeFullVolume());
			line.put("permitExchangeVolume", cabinet.getPermitExchangeVolume());
			line.put("upLineStatus", cabinet.getUpLineStatus());
			line.put("isOnline", cabinet.getIsOnline());

			if(cabinet.getUpLineTime() != null){
				line.put("upLineTime",DateFormatUtils.format( cabinet.getUpLineTime(), Constant.DATE_TIME_FORMAT));
			}else{
				line.put("upLineTime","暂无时间");
			}

			//版本
			line.put("version", cabinet.getVersion());

			//查询日统计表
			CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), statsDate);
			if(cabinetDayStats != null){
				//换电订单
				line.put("orderCount", cabinetDayStats.getOrderCount());
				//押金数-骑手
				line.put("foregiftCount", cabinetDayStats.getForegiftCount());
				//押金金额
				line.put("foregiftMoney", cabinetDayStats.getForegiftMoney() - cabinetDayStats.getRefundForegiftMoney());
				//套餐金额
				line.put("packetPeriodMoney", cabinetDayStats.getAgentPacketPeriodMoney() - cabinetDayStats.getAgentRefundPacketPeriodMoney());

				line.put("activeCount", cabinetDayStats.getActiveCustomerCount());
			} else {
				//换电订单
				line.put("orderCount", 0);
				//押金数-骑手
				line.put("foregiftCount", 0);
				//押金金额
				line.put("foregiftMoney", 0);
				//套餐金额
				line.put("packetPeriodMoney", 0);

				line.put("activeCount",0);
			}
			//电价
			line.put("unitPrice", cabinet.getPrice() * 100);
			//电费
			CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinet.getId());
			if(cabinetDayDegreeStats != null){
				line.put("useVolume", cabinetDayDegreeStats.getNum());
				line.put("price", cabinetDayDegreeStats.getNum() * cabinet.getPrice() * 100);
				if(cabinetDayStats != null && cabinetDayStats.getActiveCustomerCount() != 0){
					line.put("referencePrice",  (cabinetDayDegreeStats.getNum() * cabinet.getPrice() * 100)/cabinetDayStats.getActiveCustomerCount());
				} else {
					line.put("referencePrice", 0);
				}
			} else {
				line.put("useVolume",0);
				line.put("price",0);
				line.put("referencePrice",0);
			}

			CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(cabinet.getId(), agentId);
			if(cabinetTotalStats != null){
				//换电订单
				line.put("totalOrderCount", cabinetTotalStats.getOrderCount());
				//当前注册数
				line.put("currentForegiftCount", customerExchangeInfoService.findByBalanceCabinetId(cabinet.getId()));
				//押金数-骑手 总注册数
				line.put("totalForegiftCount", cabinetTotalStats.getForegiftCount());
				//押金金额
				line.put("totalForegiftMoney", cabinetTotalStats.getForegiftMoney() - cabinetTotalStats.getRefundForegiftMoney());
				//套餐金额
				line.put("totalPacketPeriodMoney", cabinetTotalStats.getAgentPacketPeriodMoney() - cabinetTotalStats.getAgentRefundPacketPeriodMoney());
			} else {
				//换电订单
				line.put("totalOrderCount", 0);
				//当前注册数
				line.put("currentForegiftCount", 0);
				//押金数-骑手
				line.put("totalForegiftCount", 0);
				//押金金额
				line.put("totalForegiftMoney", 0);
				//套餐金额
				line.put("totalPacketPeriodMoney", 0);
			}

			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	public static class DetailParam {
		public String id;
	}

	/**
	 * 8-换电柜详情
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/info.htm")
	public RestResult info(@RequestBody DetailParam param) {

		Cabinet cabinet = cabinetService.find(param.id);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		Map data = new HashMap();
		data.put("id", cabinet.getId());
		data.put("cabinetName", cabinet.getCabinetName());
		data.put("address", cabinet.getAddressName());
		data.put("street", cabinet.getStreet());
		data.put("chargeFullVolume", cabinet.getChargeFullVolume());
		data.put("permitExchangeVolume", cabinet.getPermitExchangeVolume());
		data.put("terminalId", cabinet.getTerminalId());
		data.put("lng", cabinet.getLng());
		data.put("lat", cabinet.getLat());
		data.put("isOnline", cabinet.getIsOnline());
		data.put("temp1", cabinet.getTemp1() == null ? 0 : cabinet.getTemp1() / 100);
		data.put("temp2", cabinet.getTemp2() == null ? 0 : cabinet.getTemp2() / 100);
		data.put("boxMinPower", cabinet.getBoxMinPower());
		data.put("boxMaxPower", cabinet.getBoxMaxPower());
		data.put("activeFanTemp", cabinet.getActiveFanTemp());
		data.put("power", cabinet.getMaxChargePower());
		CabinetCode cabinetCode = cabinetCodeService.find(cabinet.getId());
		Map imagePath1 = new HashMap();
		imagePath1.put("imagePath", cabinet.getImagePath1());
		imagePath1.put("imageUrl", staticImagePath(cabinet.getImagePath1()));
		data.put("imagePath1", imagePath1);
		Map imagePath2 = new HashMap();
		imagePath2.put("imagePath", cabinet.getImagePath2());
		imagePath2.put("imageUrl", staticImagePath(cabinet.getImagePath2()));
		data.put("imagePath2", imagePath2);
		data.put("minExchangeVolume", cabinet.getMinExchangeVolume());
		if (cabinetCode != null) {
			data.put("cabinetSn", cabinetCode.getId());
		} else {
			data.put("cabinetSn", "");
		}

		data.put("workTime", cabinet.getWorkTime() != null ? cabinet.getWorkTime() : "");

		data.put("price", cabinet.getPrice() * 100);
		//用电量统计中查找
		int useNum = 0;
		CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinet.getId());
		if(cabinetDayDegreeStats != null){
			useNum = cabinetDayDegreeStats.getEndNum();
		}
		data.put("useVolume", useNum);
		if(cabinet.getPrice() != null ){
			data.put("powerPrice", useNum * cabinet.getPrice() * 100 );
		}else{
			data.put("powerPrice", 0);
		}


		data.put("cabinetForegiftMoney", cabinet.getForegiftMoney());
		data.put("rentMoney", cabinet.getRentMoney());
		data.put("rentPeriodType", cabinet.getRentPeriodType());
		if(cabinet.getRentExpireTime() != null){
			data.put("rentExpireTime",DateFormatUtils.format( cabinet.getRentExpireTime(), Constant.DATE_FORMAT));
		}else{
			data.put("rentExpireTime", null);
		}

		if(cabinet.getUpLineTime() != null){
			data.put("upLineTime",DateFormatUtils.format( cabinet.getUpLineTime(), Constant.DATE_TIME_FORMAT));
		}else{
			data.put("upLineTime","暂无时间");
		}

		String  statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
		CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), statsDate);
		if(cabinetDayStats != null){
			//换电订单
			data.put("orderCount", cabinetDayStats.getOrderCount());
			//押金数-骑手
			data.put("foregiftCount", cabinetDayStats.getForegiftCount());
			//押金金额
			data.put("foregiftMoney", cabinetDayStats.getForegiftMoney() - cabinetDayStats.getRefundForegiftMoney());
			//套餐金额
			data.put("packetPeriodMoney", cabinetDayStats.getAgentPacketPeriodMoney() - cabinetDayStats.getAgentRefundPacketPeriodMoney());
		}else{
			//换电订单
			data.put("orderCount", 0);
			//押金数-骑手
			data.put("foregiftCount", 0);
			//押金金额
			data.put("foregiftMoney", 0);
			//套餐金额
			data.put("packetPeriodMoney", 0);
		}

		CabinetMonthStats cabinetMonthStats = cabinetMonthStatsService.findTotal(cabinet.getId());
		if(cabinetMonthStats != null){
			//换电订单
			data.put("totalOrderCount", cabinetMonthStats.getOrderCount());
			//押金数-骑手
			data.put("totalForegiftCount", cabinetMonthStats.getForegiftCount());
			//押金金额
			data.put("totalForegiftMoney", cabinetMonthStats.getForegiftMoney() - cabinetMonthStats.getRefundForegiftMoney());
			//套餐金额
			data.put("totalPacketPeriodMoney", cabinetMonthStats.getAgentPacketPeriodMoney() - cabinetMonthStats.getAgentRefundPacketPeriodMoney());
		} else {
			//换电订单
			data.put("totalOrderCount", 0);
			//押金数-骑手
			data.put("totalForegiftCount", 0);
			//押金金额
			data.put("totalForegiftMoney", 0);
			//套餐金额
			data.put("totalPacketPeriodMoney", 0);
		}

		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}


	public static class UpdateParam {
		public String id;
		@NotBlank(message = "站点名称不能为空")
		public String cabinetName;
		public Integer chargeFullVolume;
		public Integer permitExchangeVolume;
		public String terminalId;
		public Integer boxMinPower;
		public Integer boxMaxPower;
		public Double price;
		public Integer power;
		public String workTime;
		public Integer minExchangeVolume;
		public Integer areaId;
		public String street;
		public Double lng;
		public Double lat;
		public String imagePath1;
		public String imagePath2;


	}

	/**
	 * 15-修改换电柜
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update.htm")
	public RestResult update(@Valid @RequestBody UpdateParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Cabinet cabinet = cabinetService.find(param.id);

		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		if (StringUtils.isEmpty(param.cabinetName)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "站点名称不能为空");
		}

		cabinet.setAgentId(agentId);
		cabinet.setCabinetName(param.cabinetName);
		cabinet.setChargeFullVolume(param.chargeFullVolume);
		cabinet.setPermitExchangeVolume(param.permitExchangeVolume);
		cabinet.setTerminalId(param.terminalId);
		cabinet.setBoxMinPower(param.boxMinPower);
		cabinet.setBoxMaxPower(param.boxMaxPower);
		cabinet.setPrice(param.price);
		cabinet.setPower(param.power);
		cabinet.setWorkTime(param.workTime);
		cabinet.setImagePath1(param.imagePath1);
		cabinet.setImagePath2(param.imagePath2);
		cabinet.setMinExchangeVolume(param.minExchangeVolume);

		//修改地址
		if(param.areaId != null && param.lng != null && param.lat != null){
			cabinetService.updateAddress(param.id,
					param.areaId,
					param.street,
					param.lng,
					param.lat);
		}

		return cabinetService.update(cabinet);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UpdateDynamicCodeParam {
		@NotBlank(message = "站点id不能为空")
		public String cabinetId;
		public String dynamicCode;
	}

	/**
	 *29-设置动态码
	 * */
	@ResponseBody
	@RequestMapping(value = "/update_dynamic_code")
	public RestResult updateDynamicCode(@Valid @RequestBody UpdateDynamicCodeParam param) {
		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该换电柜不存在");
		}
		if (StringUtils.isEmpty(param.dynamicCode)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "动态码不能为空");
		}
		boolean isInteger = Pattern.compile("^[-\\+]?[\\d]*$").matcher(param.dynamicCode).matches();
		if (!isInteger || param.dynamicCode.length() != 4) {
			return RestResult.result(RespCode.CODE_2.getValue(), "动态码必须为4位数字");
		}

		if (cabinetService.updateDynamicCode(param.cabinetId, param.dynamicCode) == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "设置失败");
		}
		return RestResult.result(RespCode.CODE_0.getValue(), "设置成功");
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetFaultLogListParam {
		public String cabinetId;
		public int offset;
		public int limit;
	}


	/**
	 *30-柜子告警信息列表
	 * */
	@ResponseBody
	@RequestMapping(value = "/fault_log_list.htm")
	public RestResult batteryFaultLogList(@Valid @RequestBody CabinetFaultLogListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<FaultLog> list = faultLogService.findByCabinetId(agentId, param.cabinetId, param.offset, param.limit);
		List<Map> result = new ArrayList<Map>();
		if (list != null) {
			for (FaultLog faultLog : list){
				Map line = new HashMap();
				line.put("faultType", faultLog.getFaultTypeName());
				line.put("faultLevel", faultLog.getFaultLevel());
				line.put("faultContent", faultLog.getFaultContent());
				line.put("createTime", faultLog.getCreateTime());
				result.add(line);
			}
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DegreeStatsListParam {
		public String cabinetId;
		public int offset;
		public int limit;
	}

	/**
	 * 75-查询换电柜电费
	 *
	 **/
	@ResponseBody
	@RequestMapping(value = "/degree_stats_list")
	public RestResult degreeStatsList(@Valid @RequestBody DegreeStatsListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "该换电柜不存在");
		}

		List<CabinetDayDegreeStats> list = cabinetDayDegreeStatsService.findList(agentId, param.cabinetId, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (CabinetDayDegreeStats stats : list) {
			Map line = new HashMap();
			line.put("statsDate", stats.getStatsDate());
			line.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_TIME_FORMAT));
			line.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_TIME_FORMAT));
			line.put("beginNum", stats.getBeginNum());
			line.put("endNum", stats.getEndNum());
			line.put("num", stats.getNum());
			line.put("unitPrice", cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 );
			line.put("price", cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 * stats.getNum());
			CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), stats.getStatsDate());
			if (cabinetDayStats != null) {
				line.put("orderCount", cabinetDayStats.getOrderCount());
				if(cabinetDayStats.getActiveCustomerCount() != 0){
					line.put("referencePrice", (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 * stats.getNum())/cabinetDayStats.getActiveCustomerCount());
				}else{
					line.put("referencePrice", 0);
				}
			} else {
				line.put("orderCount", 0);
				line.put("referencePrice", 0);

			}

			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UpdateAddressParam {
		public String cabinetId;
		public Integer areaId;
		public String street;
		public Double lng;
		public Double lat;

	}

	@ResponseBody
	@RequestMapping(value = "/update_address.htm")
	public RestResult updateAddress(@RequestBody UpdateAddressParam param) {
		return  cabinetService.updateAddress(param.cabinetId,
				param.areaId,
				param.street,
				param.lng,
				param.lat);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetBatteryListParam {
		public String cabinetId;
	}

	/**
	 * 12-查询设备电池类型
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_battery_list.htm")
	public RestResult cabinetBatteryList(@RequestBody CabinetBatteryListParam param) {
		return cabinetService.cabinetBatteryList(param.cabinetId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetForegiftListParam {
		public Integer batteryType;
	}

	/**
	 * 17-查询设备押金
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_foregift_list.htm")
	public RestResult cabinetForegiftList(@RequestBody CabinetForegiftListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return cabinetService.cabinetForegiftList(param.batteryType,agentId);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetPriceListParam {
		public Integer batteryType;
		public Long foregiftId;
	}

	/**
	 * 18-查询设备租金
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_price_list.htm")
	public RestResult cabinetPriceList(@RequestBody CabinetPriceListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return cabinetService.cabinetPriceList(param.batteryType,agentId, param.foregiftId);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetStatsListParam {
		public String cabinetId;
		public int offset;
		public int limit;
	}

	/**
	 * 9-查询设备收入
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cabinet_stats_list.htm")
	public RestResult cabinetStatsList(@RequestBody CabinetStatsListParam param) throws IOException {
		int agentId = getTokenData().agentId;
		if (agentId == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
		}

		NotNullMap data = new NotNullMap();

		List<CabinetDayStats> statsList = cabinetDayStatsService.findListByCabinetId(agentId, param.cabinetId);

		double foregiftMoney = 0;
		double foregiftRefundMoney = 0;
		double packetPeriodMoney = 0;
		double refundPacketPeriodMoney = 0;
		double orderCount = 0;
		double activeCount = 0;

		for (CabinetDayStats e : statsList){
			foregiftMoney = foregiftMoney + e.getForegiftMoney();
			foregiftRefundMoney = foregiftRefundMoney + e.getRefundForegiftMoney();
			packetPeriodMoney = packetPeriodMoney + e.getAgentPacketPeriodMoney();
			refundPacketPeriodMoney = refundPacketPeriodMoney + e.getAgentRefundPacketPeriodMoney();
			orderCount = orderCount + e.getOrderCount();
			activeCount = activeCount + e.getActiveCustomerCount();
		}

		CabinetDayDegreeStats totalElectricPrice = cabinetDayDegreeStatsService.findForAgent(agentId, null, param.cabinetId);

		data.put("totalForegiftMoney", (int)foregiftMoney - foregiftRefundMoney);
		data.put("totalPacketPeriodMoney", (int)packetPeriodMoney - refundPacketPeriodMoney);
		if (totalElectricPrice != null) {
			data.put("totalElectricPrice", totalElectricPrice.getPrice() * 100);
		} else {
			data.put("totalElectricPrice", 0);
		}
		data.put("totalOrderCount", orderCount);
		data.put("totalActiveCount", activeCount);

		List<CabinetDayStats> cabinetDayStats = cabinetDayStatsService.findByCabinetList(agentId, param.cabinetId, null, null,null,param.offset, param.limit);

		List<NotNullMap> list = new ArrayList<NotNullMap>();
		for (CabinetDayStats stats : cabinetDayStats) {
			NotNullMap notNullMap = new NotNullMap();

			notNullMap.put("statsDate", stats.getStatsDate());
			notNullMap.put("cabinetId", stats.getCabinetId());
			notNullMap.put("cabinetName", stats.getCabinetName());
			notNullMap.put("foregiftMoney", stats.getForegiftMoney() - stats.getRefundForegiftMoney());
			notNullMap.put("packetPeriodMoney", stats.getAgentPacketPeriodMoney() - stats.getAgentRefundPacketPeriodMoney());
			notNullMap.put("price", stats.getUnitPrice());
			notNullMap.put("useVolume", stats.getElectricDegree());
			notNullMap.put("powerPrice", stats.getElectricPrice());
			notNullMap.put("activeCustomerCount", stats.getActiveCustomerCount());
			notNullMap.put("foregiftCount", stats.getForegiftCount());
			notNullMap.put("orderCount", stats.getOrderCount());

			list.add(notNullMap);
		}
		data.put("cabinetList", list);


		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetInsuranceListParam {
		public Integer batteryType;
	}

	/**
	 * 10-查询设备保险
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "cabinet_insurance_list.htm")
	public RestResult cabinetInsuranceList(@RequestBody CabinetInsuranceListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		return cabinetService.cabinetInsuranceList(param.batteryType,agentId);
	}


	public static class InputDegreeParam {
		public String cabinetId;
		public int degree;
		public int systemDegree;
	}
	/**
	 * 50-电费录入
	 */
	@ResponseBody
	@RequestMapping(value = "/input_degree.htm")
	public RestResult inputDegree(@Valid @RequestBody InputDegreeParam param) {
		String userName = userService.find(getTokenData().userId).getLoginName();
		return cabinetDegreeInputService.create(param.cabinetId, param.degree, param.systemDegree, userName);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class InputDegreeCabinetListParam {
		public String keyword;
		public int offset;
		public int limit;
	}
	/**
	 * 51-电费录入设备列表
	 *
	 **/
	@ResponseBody
	@RequestMapping(value = "/input_degree_list")
	public RestResult inputDegreeList(@Valid @RequestBody InputDegreeCabinetListParam param) {
		TokenCache.Data tokenData = getTokenData();
		int agentId = tokenData.agentId;
		List<Cabinet> list = cabinetService.findList(agentId, null, param.keyword, param.offset, param.limit);

		List<Map> result = new ArrayList<Map>();
		for (Cabinet cabinet : list) {
			Map line = new HashMap();
			line.put("id", cabinet.getId());
			line.put("cabinetName", cabinet.getCabinetName());
			line.put("inputDegreeNum", cabinet.getInputDegreeNum() != null ? cabinet.getInputDegreeNum() : "");
			line.put("inputDegreeMoney", cabinet.getInputDegreeMoney() != null ? cabinet.getInputDegreeMoney() * 100 : "");
			line.put("price", cabinet.getPrice() * 100);
			if (cabinet.getUpLineTime() != null) {
				CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findDayDegree(cabinet.getId(), DateFormatUtils.format(cabinet.getUpLineTime(), Constant.DATE_FORMAT));
				line.put("initNum", cabinetDayDegreeStats != null ? cabinetDayDegreeStats.getBeginNum() : 0);
			} else {
				line.put("initNum", 0);
			}

			if(cabinet.getInputDegreeTime() != null){
				line.put("inputDegreeTime",DateFormatUtils.format(cabinet.getInputDegreeTime(), Constant.DATE_FORMAT));
			} else {
				line.put("inputDegreeTime","");
			}
			CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinet.getId());
			if (cabinetDayDegreeStats != null) {
				line.put("cabinetNum",cabinetDayDegreeStats.getEndNum());
			} else {
				line.put("cabinetNum","");
			}
			result.add(line);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
	}
}
