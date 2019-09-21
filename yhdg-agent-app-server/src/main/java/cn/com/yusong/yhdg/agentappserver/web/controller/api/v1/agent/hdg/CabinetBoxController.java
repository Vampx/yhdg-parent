package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller("agent_api_v1_agent_hdg_cabinet_box")
@RequestMapping("/agent_api/v1/agent/hdg/cabinet_box")
public class CabinetBoxController extends ApiController {

	static final Logger log = LogManager.getLogger(CabinetBoxController.class);
	@Autowired
    CabinetService cabinetService;
	@Autowired
    UserService userService;
	@Autowired
    CabinetOperateLogService cabinetOperateLogService;
	@Autowired
	CabinetBoxService cabinetBoxService;
	@Autowired
	BatteryService batteryService;
	@Autowired
	BatteryOrderService batteryOrderService;
	@Autowired
	BatteryParameterService batteryParameterService;

	public static class DetailParam {
		public String id;
	}

	/**
	 * 11-查询设备详情
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/box_info.htm")
	public RestResult boxInfo(@RequestBody DetailParam param) {

		Cabinet cabinet = cabinetService.find(param.id);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		List<Map> boxList = new ArrayList<Map>();
		List<CabinetBox> cabinetBoxList = cabinetBoxService.findByCabinetId(cabinet.getId());
		final int EMPTY_BOX = 1, NOT_OUT = 2, ORANGE = 3, FULL_POWER = 4, PROHIBIT = 5 , NOT_ONLINE = 6, BESPEAK = 7;    //1 空箱 2 不可取出(未满和客户的电池) 3 橙色 4 满电 5 禁用 6 电池未上线
		for (CabinetBox box : cabinetBoxList) {
			Map boxMap = new HashMap();
			boxMap.put("boxNum", box.getBoxNum());
			Battery battery = batteryService.find(box.getBatteryId());
			if (battery != null) {
				boxMap.put("isNormal", battery.getIsNormal());
				boxMap.put("abnormalCause", battery.getAbnormalCause());
			} else {
				boxMap.put("isNormal", null);
				boxMap.put("abnormalCause", null);
			}
			boxMap.put("isActive", box.getIsActive());
			boxMap.put("forbiddenCause", box.getForbiddenCause());
			boxMap.put("batteryId", box.getBatteryId());
			boxMap.put("volume", box.getVolume() == null ? 0 : box.getVolume());
			boxMap.put("power", box.getPower());
			boxMap.put("isOpen", box.getIsOpen());
			boxMap.put("shellCode", box.getShellCode());
			boxMap.put("imei", box.getImei());
			boxMap.put("chargeStatus", box.getChargeStatus());
			boxMap.put("forbiddenCause", box.getForbiddenCause());
			int status = EMPTY_BOX; //1 空箱  空闲
			int expectFullTime = 0;
			if (box.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
				status = PROHIBIT;// 5 禁用
			} else if (box.getBatteryId() != null) {
				if ((box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue() || box.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue()) &&
						(box.getBatteryStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()
								|| box.getBatteryStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue())) {
					status = NOT_OUT; // 2 不可取出(未满和客户的电池)
				} else if (box.getBoxStatus() == CabinetBox.BoxStatus.BESPEAK.getValue()) {
					status = BESPEAK; // 预约
				} else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
						&& box.getVolume() < box.getBatteryFullVolume()) {
					status = ORANGE; // 3 未充满 橙色
				} else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
						&& box.getVolume() >= box.getBatteryFullVolume()) {
					status = FULL_POWER; //4 满电(可换)
				}
				if (box.getVolume() != null && box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
					expectFullTime = (int) Math.ceil(((100 - box.getVolume()) * 0.7));
				}
				//上线状态 0 未上线 1 已上线*/
				if (box.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue()) {
					status = NOT_ONLINE;
				}
			} else if(box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()
					|| box.getBoxStatus() == CabinetBox.BoxStatus.FULL_LOCK.getValue()
					|| box.getBoxStatus() == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
				status = NOT_OUT; // 2 不可取出(占箱的格口)
			}
			boxMap.put("status", status);
			boxMap.put("expectFullTime", expectFullTime);
			boxList.add(boxMap);
		}

		Map data = new HashMap();
		data.put("id", cabinet.getId());
		data.put("cabinetName", cabinet.getCabinetName());
		data.put("boxList", boxList);

		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}



	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OpenBoxParam {
		public String cabinetId;
		public String boxNum;
	}

	/**
	 * 13-开箱
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/open_box.htm")
	public RestResult batteryOrderOldBatteryOpenBox(@RequestBody OpenBoxParam param) throws InterruptedException {
		TokenCache.Data tokenData = getTokenData();
		User user = userService.find(tokenData.userId);
		if (StringUtils.isEmpty(param.cabinetId)) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}
		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		CabinetOperateLog operateLog = new CabinetOperateLog();
		operateLog.setAgentId(cabinet.getAgentId() != null ? cabinet.getAgentId() : 0);
		operateLog.setCabinetId(cabinet.getId());
		operateLog.setCabinetName(cabinet.getCabinetName());
		operateLog.setBoxNum(param.boxNum);
		operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
		operateLog.setOperatorType(CabinetOperateLog.OperatorType.AGENT_CABINET.getValue());
		operateLog.setOperator(user.getFullname());

		//发送开箱指令
		RestResult result = ClientBizUtils.openStandardBox(config, param.cabinetId, param.boxNum, cabinet.getSubtype());

		int openSuccess = 0;
		Map data = new HashMap();

		if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
			if (log.isDebugEnabled()) {
				log.debug("open {}, {} box success", param.cabinetId, param.boxNum);
			}
			openSuccess = 1;
			operateLog.setContent("远程开箱成功, 箱门状态: " + (result.getCode() == 0 ? "开门" : "关门"));
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		} else {
			openSuccess = 0;
			if (log.isDebugEnabled()) {
				log.error("open {}, {} box fail", param.cabinetId, param.boxNum);
			}
			operateLog.setContent("远程开箱失败, " + result.getMessage());
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		}

		data.put("openSuccess", openSuccess);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CabinetBoxParam {
		public String cabinetId;
		public String[] boxNumList;
		public int isActive;
		public String forbiddenCause;
	}

	/**
	 * 62-格口启用禁用
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/box_active.htm")
	public RestResult CabinetBoxActive(@RequestBody CabinetBoxParam param) {
		TokenCache.Data tokenData = getTokenData();
		User user = userService.find(tokenData.userId);

		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		String[] boxList = new String[param.boxNumList.length];
		if (param.boxNumList != null && param.boxNumList.length > 0) {
			boxList = param.boxNumList;
		}

		for (String boxNum : boxList) {
			CabinetBox cabinetBox = cabinetBoxService.find(param.cabinetId, boxNum);
			if (cabinetBox == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "箱号不存在");
			}

			//保存日志
			CabinetOperateLog operateLog = new CabinetOperateLog();
			operateLog.setAgentId(cabinet.getAgentId());
			operateLog.setCabinetId(cabinet.getId());
			operateLog.setCabinetName(cabinet.getCabinetName());
			operateLog.setBoxNum(boxNum);
			operateLog.setOperatorType(CabinetOperateLog.OperatorType.AGENT_CABINET.getValue());
			operateLog.setOperator(user.getFullname());
			String operator = user.getFullname();
			if (param.isActive == ConstEnum.Flag.TRUE.getValue()){
				cabinetBoxService.updateBoxActive(param.cabinetId, boxNum, ConstEnum.Flag.TRUE.getValue(),param.forbiddenCause,null,null);
				operateLog.setOperateType(CabinetOperateLog.OperateType.ACTIVE.getValue());
				operateLog.setContent("箱门激活成功");
			} else {
				cabinetBoxService.updateBoxActive(param.cabinetId, boxNum, ConstEnum.Flag.FALSE.getValue(),param.forbiddenCause,operator,new Date());
				operateLog.setOperateType(CabinetOperateLog.OperateType.NO_ACTIVE.getValue());
				operateLog.setContent("箱门禁用成功");
			}

			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null,null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OpenBoxCompleteOrderParam {
		public String cabinetId;
		public String boxNum;
		public String batteryId;
	}

	/**
	 * 20-开箱结束订单
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/open_box_complete_order.htm")
	public RestResult openBoxCompleteOrder(@RequestBody OpenBoxCompleteOrderParam param) throws InterruptedException {
		TokenCache.Data tokenData = getTokenData();
		User user = userService.find(tokenData.userId);

		Cabinet cabinet = cabinetService.find(param.cabinetId);

		CabinetOperateLog operateLog = new CabinetOperateLog();
		operateLog.setAgentId(cabinet.getAgentId() != null ? cabinet.getAgentId() : 0);
		operateLog.setCabinetId(cabinet.getId());
		operateLog.setCabinetName(cabinet.getCabinetName());
		operateLog.setBoxNum(param.boxNum);
		operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
		operateLog.setOperatorType(CabinetOperateLog.OperatorType.PLATFORM.getValue());
		operateLog.setOperator(user.getFullname());

		//发送开箱指令
		RestResult result = ClientBizUtils.openStandardBox(config, param.cabinetId, param.boxNum, cabinet.getSubtype());

		int openSuccess = 0;
		Map data = new HashMap();

		if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
			if (log.isDebugEnabled()) {
				log.debug("open {}, {} box success", param.cabinetId, param.boxNum);
			}
			openSuccess = 1;
			operateLog.setContent("远程开箱成功, 箱门状态: " + (result.getCode() == 0 ? "开门" : "关门"));
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		} else {
			openSuccess = 0;
			if (log.isDebugEnabled()) {
				log.error("open {}, {} box fail", param.cabinetId, param.boxNum);
			}
			operateLog.setContent("远程开箱失败, " + result.getMessage());
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		}

		data.put("openSuccess", openSuccess);

		Battery battery = batteryService.find(param.batteryId);
		if (battery == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
		}
		if (battery.getOrderId() != null) {
			batteryOrderService.complete(battery.getOrderId());
		}

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}


	/**
	 * 21-结束订单
	 * @return
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CompleteOrderParam {
		public String batteryId;
	}

	@ResponseBody
	@RequestMapping(value = "/complete_order.htm")
	public RestResult completeOrderParam(@RequestBody CompleteOrderParam param) {

		Battery battery = batteryService.find(param.batteryId);
		if (battery == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
		}
		if (battery.getOrderId() != null) {
			batteryOrderService.complete(battery.getOrderId());
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null,null);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ForbiddenCauseParam {
		public String cabinetId;
		public String boxNum;
	}

	/**
	 * 155-查看禁用原因
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/view_forbidden_cause.htm")
	public RestResult viewForbiddenCause(@RequestBody ForbiddenCauseParam param) {
		CabinetBox cabinetBox = cabinetBoxService.find(param.cabinetId, param.boxNum);
		Map data = new HashMap();
		if (cabinetBox != null) {
			data.put("forbiddenCause", cabinetBox.getForbiddenCause());
		} else {
			return RestResult.result(RespCode.CODE_2.getValue(),"格口不存在");
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

}
