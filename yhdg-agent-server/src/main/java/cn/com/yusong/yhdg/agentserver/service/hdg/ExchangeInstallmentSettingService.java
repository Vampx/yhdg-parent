package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordPayDetailMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeInstallmentSettingService extends AbstractService {
	@Autowired
	ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
	@Autowired
	ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;
	@Autowired
	CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
	@Autowired
	CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
	@Autowired
	AgentMapper agentMapper;

	public ExchangeInstallmentSetting find(Long id) {
		return exchangeInstallmentSettingMapper.find(id);
	}

	public Page findPage(ExchangeInstallmentSetting search) {
		Page page = search.buildPage();
		page.setTotalItems(exchangeInstallmentSettingMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<ExchangeInstallmentSetting> list = exchangeInstallmentSettingMapper.findPageResult(search);
		for (ExchangeInstallmentSetting exchangeInstallmentSetting : list) {
			List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = exchangeInstallmentDetailMapper.findListBySettingId(exchangeInstallmentSetting.getId());
			exchangeInstallmentSetting.setInstallmentNum(exchangeInstallmentDetailList.size());
			for (ExchangeInstallmentDetail exchangeInstallmentDetail : exchangeInstallmentDetailList) {
				//最后分期时间
				if (exchangeInstallmentDetail.getNum() == exchangeInstallmentDetailList.size()) {
					exchangeInstallmentSetting.setFinalInstallmentTime(exchangeInstallmentDetail.getExpireTime());
				}
			}
			int paidInstallmentMoney = 0;
			exchangeInstallmentSetting.setPaidInstallmentNum(0);
			exchangeInstallmentSetting.setPaidInstallmentMoney(paidInstallmentMoney);
			exchangeInstallmentSetting.setInstallmentRestMoney(exchangeInstallmentSetting.getTotalMoney() - paidInstallmentMoney);
			//已完成分期数
			CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.findByExchangeSettingId(exchangeInstallmentSetting.getId(), Battery.Category.EXCHANGE.getValue());
			if (customerInstallmentRecord != null) {
				List<CustomerInstallmentRecordPayDetail> customerInstallmentRecordPayDetailList = customerInstallmentRecordPayDetailMapper.findPaidListByRecordId(customerInstallmentRecord.getId(), Battery.Category.EXCHANGE.getValue(), ConstEnum.PayStatus.PAYD.getValue());
				for (CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail : customerInstallmentRecordPayDetailList) {
					paidInstallmentMoney += customerInstallmentRecordPayDetail.getMoney();
				}
				exchangeInstallmentSetting.setPaidInstallmentNum(customerInstallmentRecordPayDetailList.size());
				exchangeInstallmentSetting.setPaidInstallmentMoney(paidInstallmentMoney);
				exchangeInstallmentSetting.setInstallmentRestMoney(exchangeInstallmentSetting.getTotalMoney() - paidInstallmentMoney);
			}
			SystemBatteryType batteryType = findBatteryType(exchangeInstallmentSetting.getBatteryType());
			if (batteryType != null) {
				exchangeInstallmentSetting.setBatteryTypeName(batteryType.getTypeName());
			}
		}
		page.setResult(list);
		return page;
	}

	public boolean findUnique(Long id, String mobile) {
		return exchangeInstallmentSettingMapper.findUnique(id, mobile) == 0;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(String data) throws IOException, ParseException {
		Map dataMap = (Map) YhdgUtils.decodeJson(data,Map.class);
		if(dataMap == null){
			return ExtResult.failResult("传参为空！");
		}
		String mobile = dataMap.get("mobile").toString();
		String fullname = dataMap.get("fullname").toString();
		Integer agentId = Integer.parseInt(dataMap.get("agentId").toString());
		String deadlineTimeString = dataMap.get("deadlineTime").toString();
		Date deadlineTime = DateUtils.parseDate(deadlineTimeString, new String[]{Constant.DATE_TIME_FORMAT});
		Integer totalMoney = Integer.parseInt(dataMap.get("totalMoney").toString());
		Integer batteryType = Integer.parseInt(dataMap.get("batteryType").toString());
		Long foregiftId = Long.parseLong(dataMap.get("foregiftId").toString());
		Integer foregiftMoney = Integer.parseInt(dataMap.get("foregiftMoney").toString());
		Long packetId = Long.parseLong(dataMap.get("packetId").toString());
		Integer packetMoney = Integer.parseInt(dataMap.get("packetMoney").toString());
		String initialInsuranceId = dataMap.get("insuranceId").toString();
		Long insuranceId = null;
		if (StringUtils.isNotEmpty(initialInsuranceId)) {
			insuranceId = Long.parseLong(initialInsuranceId);
		}
		Integer insuranceMoney = Integer.parseInt(dataMap.get("insuranceMoney").toString());

		ExchangeInstallmentSetting exchangeInstallmentSetting = new ExchangeInstallmentSetting();
		exchangeInstallmentSetting.setMobile(mobile);
		exchangeInstallmentSetting.setFullname(fullname);
		exchangeInstallmentSetting.setAgentId(agentId);
		exchangeInstallmentSetting.setDeadlineTime(deadlineTime);
		exchangeInstallmentSetting.setTotalMoney(totalMoney);
		exchangeInstallmentSetting.setBatteryType(batteryType);
		exchangeInstallmentSetting.setForegiftId(foregiftId);
		exchangeInstallmentSetting.setForegiftMoney(foregiftMoney);
		exchangeInstallmentSetting.setPacketId(packetId);
		exchangeInstallmentSetting.setPacketMoney(packetMoney);
		exchangeInstallmentSetting.setInsuranceId(insuranceId);
		exchangeInstallmentSetting.setInsuranceMoney(insuranceMoney);
		Agent agent = agentMapper.find(exchangeInstallmentSetting.getAgentId());
		if (agent != null) {
			exchangeInstallmentSetting.setAgentName(agent.getAgentName());
			exchangeInstallmentSetting.setAgentCode(agent.getAgentCode());
		}
		exchangeInstallmentSetting.setCreateTime(new Date());

		List<Map> detailList = (List) dataMap.get("detailList");
		List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = new ArrayList<ExchangeInstallmentDetail>();
		for (Map detail : detailList) {
			ExchangeInstallmentDetail exchangeInstallmentDetail = new ExchangeInstallmentDetail();
			Integer recentForegiftMoney = Integer.parseInt(detail.get("recentForegiftMoney").toString());
			Integer recentPacketMoney = Integer.parseInt(detail.get("recentPacketMoney").toString());
			Integer recentInsuranceMoney = Integer.parseInt(detail.get("recentInsuranceMoney").toString());
			Integer num = Integer.parseInt(detail.get("num").toString());
			String recentExpireTimeString = detail.get("recentExpireTime").toString();
			if (recentExpireTimeString.trim().equals("23:59:59")) {
				continue;
			}
			Date recentExpireTime = DateUtils.parseDate(recentExpireTimeString, new String[]{Constant.DATE_TIME_FORMAT});
			exchangeInstallmentDetail.setForegiftMoney(recentForegiftMoney);
			exchangeInstallmentDetail.setPacketMoney(recentPacketMoney);
			exchangeInstallmentDetail.setInsuranceMoney(recentInsuranceMoney);
			exchangeInstallmentDetail.setNum(num);
			exchangeInstallmentDetail.setMoney(recentForegiftMoney + recentPacketMoney + recentInsuranceMoney);
			exchangeInstallmentDetail.setExpireTime(recentExpireTime);
			exchangeInstallmentDetailList.add(exchangeInstallmentDetail);
		}
		if (exchangeInstallmentSettingMapper.insert(exchangeInstallmentSetting) >= 1) {
			for (ExchangeInstallmentDetail installmentDetail : exchangeInstallmentDetailList) {
				installmentDetail.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentDetailMapper.insert(installmentDetail);
			}
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	public ExtResult update(String data) throws IOException, ParseException {
		Map dataMap = (Map) YhdgUtils.decodeJson(data,Map.class);
		if(dataMap == null){
			return ExtResult.failResult("传参为空！");
		}
		Long id = Long.parseLong(dataMap.get("id").toString());
		String mobile = dataMap.get("mobile").toString();
		String fullname = dataMap.get("fullname").toString();
		String deadlineTimeString = dataMap.get("deadlineTime").toString();
		Date deadlineTime = DateUtils.parseDate(deadlineTimeString, new String[]{Constant.DATE_TIME_FORMAT});
		Integer totalMoney = Integer.parseInt(dataMap.get("totalMoney").toString());
		Integer batteryType = Integer.parseInt(dataMap.get("batteryType").toString());
		Long foregiftId = Long.parseLong(dataMap.get("foregiftId").toString());
		Integer foregiftMoney = Integer.parseInt(dataMap.get("foregiftMoney").toString());
		Long packetId = Long.parseLong(dataMap.get("packetId").toString());
		Integer packetMoney = Integer.parseInt(dataMap.get("packetMoney").toString());
		String initialInsuranceId = dataMap.get("insuranceId").toString();
		Long insuranceId = null;
		if (StringUtils.isNotEmpty(initialInsuranceId)) {
			insuranceId = Long.parseLong(initialInsuranceId);
		}
		Integer insuranceMoney = Integer.parseInt(dataMap.get("insuranceMoney").toString());

		ExchangeInstallmentSetting exchangeInstallmentSetting = new ExchangeInstallmentSetting();
		exchangeInstallmentSetting.setId(id);
		exchangeInstallmentSetting.setMobile(mobile);
		exchangeInstallmentSetting.setFullname(fullname);
		exchangeInstallmentSetting.setDeadlineTime(deadlineTime);
		exchangeInstallmentSetting.setTotalMoney(totalMoney);
		exchangeInstallmentSetting.setBatteryType(batteryType);
		exchangeInstallmentSetting.setForegiftId(foregiftId);
		exchangeInstallmentSetting.setForegiftMoney(foregiftMoney);
		exchangeInstallmentSetting.setPacketId(packetId);
		exchangeInstallmentSetting.setPacketMoney(packetMoney);
		exchangeInstallmentSetting.setInsuranceId(insuranceId);
		exchangeInstallmentSetting.setInsuranceMoney(insuranceMoney);
		exchangeInstallmentSetting.setCreateTime(new Date());

		//已完成分期数
		CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.findByExchangeSettingId(exchangeInstallmentSetting.getId(), Battery.Category.EXCHANGE.getValue());
		if (customerInstallmentRecord != null) {
			List<CustomerInstallmentRecordPayDetail> customerInstallmentRecordPayDetailList = customerInstallmentRecordPayDetailMapper.findPaidListByRecordId(customerInstallmentRecord.getId(), Battery.Category.EXCHANGE.getValue(), ConstEnum.PayStatus.PAYD.getValue());
			exchangeInstallmentSetting.setPaidInstallmentNum(customerInstallmentRecordPayDetailList.size());
		}
		if (exchangeInstallmentSetting.getPaidInstallmentNum() != null && exchangeInstallmentSetting.getPaidInstallmentNum() > 0) {
			return ExtResult.failResult("客户交过分期金额，无法修改分期设置");
		}

		List<Map> detailList = (List) dataMap.get("detailList");
		List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = new ArrayList<ExchangeInstallmentDetail>();
		for (Map detail : detailList) {
			ExchangeInstallmentDetail exchangeInstallmentDetail = new ExchangeInstallmentDetail();
			Integer recentForegiftMoney = Integer.parseInt(detail.get("recentForegiftMoney").toString());
			Integer recentPacketMoney = Integer.parseInt(detail.get("recentPacketMoney").toString());
			Integer recentInsuranceMoney = Integer.parseInt(detail.get("recentInsuranceMoney").toString());
			Integer num = Integer.parseInt(detail.get("num").toString());
			String recentExpireTimeString = detail.get("recentExpireTime").toString();
			if (recentExpireTimeString.trim().equals("23:59:59")) {
				continue;
			}
			Date recentExpireTime = DateUtils.parseDate(recentExpireTimeString, new String[]{Constant.DATE_TIME_FORMAT});
			exchangeInstallmentDetail.setForegiftMoney(recentForegiftMoney);
			exchangeInstallmentDetail.setPacketMoney(recentPacketMoney);
			exchangeInstallmentDetail.setInsuranceMoney(recentInsuranceMoney);
			exchangeInstallmentDetail.setNum(num);
			exchangeInstallmentDetail.setMoney(recentForegiftMoney + recentPacketMoney + recentInsuranceMoney);
			exchangeInstallmentDetail.setExpireTime(recentExpireTime);
			exchangeInstallmentDetailList.add(exchangeInstallmentDetail);
		}
		if (exchangeInstallmentSettingMapper.update(exchangeInstallmentSetting) >= 1) {
			exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
			for (ExchangeInstallmentDetail installmentDetail : exchangeInstallmentDetailList) {
				installmentDetail.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentDetailMapper.insert(installmentDetail);
			}
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Long id) {
		if (exchangeInstallmentSettingMapper.delete(id) >= 1) {
			exchangeInstallmentDetailMapper.deleteBySettingId(id);
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}
}
