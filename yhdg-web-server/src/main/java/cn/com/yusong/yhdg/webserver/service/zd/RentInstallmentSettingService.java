package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentDetail;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordPayDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentInstallmentDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
public class RentInstallmentSettingService extends AbstractService {
	@Autowired
	RentInstallmentSettingMapper rentInstallmentSettingMapper;
	@Autowired
	RentInstallmentDetailMapper rentInstallmentDetailMapper;
	@Autowired
	CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
	@Autowired
	CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
	@Autowired
	AgentMapper agentMapper;

	public RentInstallmentSetting find(Long id) {
		return rentInstallmentSettingMapper.find(id);
	}

	public Page findPage(RentInstallmentSetting search) {
		Page page = search.buildPage();
		page.setTotalItems(rentInstallmentSettingMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<RentInstallmentSetting> list = rentInstallmentSettingMapper.findPageResult(search);
		for (RentInstallmentSetting rentInstallmentSetting : list) {
			List<RentInstallmentDetail> rentInstallmentDetailList = rentInstallmentDetailMapper.findListBySettingId(rentInstallmentSetting.getId());
			rentInstallmentSetting.setInstallmentNum(rentInstallmentDetailList.size());
			for (RentInstallmentDetail rentInstallmentDetail : rentInstallmentDetailList) {
				//最后分期时间
				if (rentInstallmentDetail.getNum() == rentInstallmentDetailList.size()) {
					rentInstallmentSetting.setFinalInstallmentTime(rentInstallmentDetail.getExpireTime());
				}
			}
			int paidInstallmentMoney = 0;
			rentInstallmentSetting.setPaidInstallmentNum(0);
			rentInstallmentSetting.setPaidInstallmentMoney(paidInstallmentMoney);
			rentInstallmentSetting.setInstallmentRestMoney(rentInstallmentSetting.getTotalMoney() - paidInstallmentMoney);
			//已完成分期数
			CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.findByRentSettingId(rentInstallmentSetting.getId(), Battery.Category.RENT.getValue());
			if (customerInstallmentRecord != null) {
				List<CustomerInstallmentRecordPayDetail> customerInstallmentRecordPayDetailList = customerInstallmentRecordPayDetailMapper.findPaidListByRecordId(customerInstallmentRecord.getId(), Battery.Category.RENT.getValue(), ConstEnum.PayStatus.PAYD.getValue());
				for (CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail : customerInstallmentRecordPayDetailList) {
					paidInstallmentMoney += customerInstallmentRecordPayDetail.getMoney();
				}
				rentInstallmentSetting.setPaidInstallmentNum(customerInstallmentRecordPayDetailList.size());
				rentInstallmentSetting.setPaidInstallmentMoney(paidInstallmentMoney);
				rentInstallmentSetting.setInstallmentRestMoney(rentInstallmentSetting.getTotalMoney() - paidInstallmentMoney);
			}
			SystemBatteryType batteryType = findBatteryType(rentInstallmentSetting.getBatteryType());
			if (batteryType != null) {
				rentInstallmentSetting.setBatteryTypeName(batteryType.getTypeName());
			}
		}
		page.setResult(list);
		return page;
	}

	public boolean findUnique(Long id, String mobile) {
		return rentInstallmentSettingMapper.findUnique(id, mobile) == 0;
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

		RentInstallmentSetting rentInstallmentSetting = new RentInstallmentSetting();
		rentInstallmentSetting.setMobile(mobile);
		rentInstallmentSetting.setFullname(fullname);
		rentInstallmentSetting.setAgentId(agentId);
		rentInstallmentSetting.setDeadlineTime(deadlineTime);
		rentInstallmentSetting.setTotalMoney(totalMoney);
		rentInstallmentSetting.setBatteryType(batteryType);
		rentInstallmentSetting.setForegiftId(foregiftId);
		rentInstallmentSetting.setForegiftMoney(foregiftMoney);
		rentInstallmentSetting.setPacketId(packetId);
		rentInstallmentSetting.setPacketMoney(packetMoney);
		rentInstallmentSetting.setInsuranceId(insuranceId);
		rentInstallmentSetting.setInsuranceMoney(insuranceMoney);
		Agent agent = agentMapper.find(rentInstallmentSetting.getAgentId());
		if (agent != null) {
			rentInstallmentSetting.setAgentName(agent.getAgentName());
			rentInstallmentSetting.setAgentCode(agent.getAgentCode());
		}
		rentInstallmentSetting.setCreateTime(new Date());

		List<Map> detailList = (List) dataMap.get("detailList");
		List<RentInstallmentDetail> rentInstallmentDetailList = new ArrayList<RentInstallmentDetail>();
		for (Map detail : detailList) {
			RentInstallmentDetail rentInstallmentDetail = new RentInstallmentDetail();
			Integer recentForegiftMoney = Integer.parseInt(detail.get("recentForegiftMoney").toString());
			Integer recentPacketMoney = Integer.parseInt(detail.get("recentPacketMoney").toString());
			Integer recentInsuranceMoney = Integer.parseInt(detail.get("recentInsuranceMoney").toString());
			Integer num = Integer.parseInt(detail.get("num").toString());
			String recentExpireTimeString = detail.get("recentExpireTime").toString();
			if (recentExpireTimeString.trim().equals("23:59:59")) {
				continue;
			}
			Date recentExpireTime = DateUtils.parseDate(recentExpireTimeString, new String[]{Constant.DATE_TIME_FORMAT});
			rentInstallmentDetail.setForegiftMoney(recentForegiftMoney);
			rentInstallmentDetail.setPacketMoney(recentPacketMoney);
			rentInstallmentDetail.setInsuranceMoney(recentInsuranceMoney);
			rentInstallmentDetail.setNum(num);
			rentInstallmentDetail.setMoney(recentForegiftMoney + recentPacketMoney + recentInsuranceMoney);
			rentInstallmentDetail.setExpireTime(recentExpireTime);
			rentInstallmentDetailList.add(rentInstallmentDetail);
		}
		if (rentInstallmentSettingMapper.insert(rentInstallmentSetting) >= 1) {
			for (RentInstallmentDetail rentInstallmentDetail : rentInstallmentDetailList) {
				rentInstallmentDetail.setSettingId(rentInstallmentSetting.getId());
				rentInstallmentDetailMapper.insert(rentInstallmentDetail);
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

		RentInstallmentSetting rentInstallmentSetting = new RentInstallmentSetting();
		rentInstallmentSetting.setId(id);
		rentInstallmentSetting.setMobile(mobile);
		rentInstallmentSetting.setFullname(fullname);
		rentInstallmentSetting.setDeadlineTime(deadlineTime);
		rentInstallmentSetting.setTotalMoney(totalMoney);
		rentInstallmentSetting.setBatteryType(batteryType);
		rentInstallmentSetting.setForegiftId(foregiftId);
		rentInstallmentSetting.setForegiftMoney(foregiftMoney);
		rentInstallmentSetting.setPacketId(packetId);
		rentInstallmentSetting.setPacketMoney(packetMoney);
		rentInstallmentSetting.setInsuranceId(insuranceId);
		rentInstallmentSetting.setInsuranceMoney(insuranceMoney);
		rentInstallmentSetting.setCreateTime(new Date());

		//已完成分期数
		CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.findByRentSettingId(rentInstallmentSetting.getId(), Battery.Category.RENT.getValue());
		if (customerInstallmentRecord != null) {
			List<CustomerInstallmentRecordPayDetail> customerInstallmentRecordPayDetailList = customerInstallmentRecordPayDetailMapper.findPaidListByRecordId(customerInstallmentRecord.getId(), Battery.Category.RENT.getValue(), ConstEnum.PayStatus.PAYD.getValue());
			rentInstallmentSetting.setPaidInstallmentNum(customerInstallmentRecordPayDetailList.size());
		}
		if (rentInstallmentSetting.getPaidInstallmentNum() != null && rentInstallmentSetting.getPaidInstallmentNum() > 0) {
			return ExtResult.failResult("客户交过分期金额，无法修改分期设置");
		}

		List<Map> detailList = (List) dataMap.get("detailList");
		List<RentInstallmentDetail> rentInstallmentDetailList = new ArrayList<RentInstallmentDetail>();
		for (Map detail : detailList) {
			RentInstallmentDetail rentInstallmentDetail = new RentInstallmentDetail();
			Integer recentForegiftMoney = Integer.parseInt(detail.get("recentForegiftMoney").toString());
			Integer recentPacketMoney = Integer.parseInt(detail.get("recentPacketMoney").toString());
			Integer recentInsuranceMoney = Integer.parseInt(detail.get("recentInsuranceMoney").toString());
			Integer num = Integer.parseInt(detail.get("num").toString());
			String recentExpireTimeString = detail.get("recentExpireTime").toString();
			if (recentExpireTimeString.trim().equals("23:59:59")) {
				continue;
			}
			Date recentExpireTime = DateUtils.parseDate(recentExpireTimeString, new String[]{Constant.DATE_TIME_FORMAT});
			rentInstallmentDetail.setForegiftMoney(recentForegiftMoney);
			rentInstallmentDetail.setPacketMoney(recentPacketMoney);
			rentInstallmentDetail.setInsuranceMoney(recentInsuranceMoney);
			rentInstallmentDetail.setNum(num);
			rentInstallmentDetail.setMoney(recentForegiftMoney + recentPacketMoney + recentInsuranceMoney);
			rentInstallmentDetail.setExpireTime(recentExpireTime);
			rentInstallmentDetailList.add(rentInstallmentDetail);
		}
		if (rentInstallmentSettingMapper.update(rentInstallmentSetting) >= 1) {
			rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
			for (RentInstallmentDetail installmentDetail : rentInstallmentDetailList) {
				installmentDetail.setSettingId(rentInstallmentSetting.getId());
				rentInstallmentDetailMapper.insert(installmentDetail);
			}
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Long id) {
		if (rentInstallmentSettingMapper.delete(id) >= 1) {
			rentInstallmentDetailMapper.deleteBySettingId(id);
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}
}
