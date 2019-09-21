package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordPayDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.management.resources.agent;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

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
	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	ExchangeInstallmentCabinetMapper exchangeInstallmentCabinetMapper;
	@Autowired
	ExchangeInstallmentCountDetailMapper exchangeInstallmentCountDetailMapper;
	@Autowired
	ExchangeInstallmentCountMapper exchangeInstallmentCountMapper;
	@Autowired
	ExchangeInstallmentCustomerMapper exchangeInstallmentCustomerMapper;
	@Autowired
	ExchangeInstallmentStationMapper exchangeInstallmentStationMapper;

	public ExchangeInstallmentSetting find(Long id) {
		return exchangeInstallmentSettingMapper.find(id);
	}

	public Page findPage(ExchangeInstallmentSetting search) {
		Page page = search.buildPage();
		page.setTotalItems(exchangeInstallmentSettingMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<ExchangeInstallmentSetting> list = exchangeInstallmentSettingMapper.findPageResult(search);
		for (ExchangeInstallmentSetting exchangeInstallmentSetting : list) {
			List<ExchangeInstallmentCustomer> settingId = exchangeInstallmentCustomerMapper.findSettingId(exchangeInstallmentSetting.getId());
			int customermobileNum =0;
			StringBuffer stringBuffer =new StringBuffer();
			for (ExchangeInstallmentCustomer exchangeInstallmentCustomer: settingId) {
				customermobileNum++;
				stringBuffer.append(exchangeInstallmentCustomer.getCustomerMobile()+',');
			}
			String s = stringBuffer.toString();
			exchangeInstallmentSetting.setCustomermobileNum(customermobileNum);
			if(StringUtils.isNotBlank(s)){
				exchangeInstallmentSetting.setCustomermobile(s.substring(0,s.length()-1));
			}

			List<ExchangeInstallmentCabinet> settingId1 = exchangeInstallmentCabinetMapper.findSettingId(exchangeInstallmentSetting.getId());
			StringBuffer stringBuffers =new StringBuffer();
			int cabinetNameNum=0;
			for (ExchangeInstallmentCabinet exchangeInstallmentCabinet: settingId1) {
				cabinetNameNum++;
				stringBuffers.append(exchangeInstallmentCabinet.getCabinetName()+',');
			}
			exchangeInstallmentSetting.setCabinetNameNum(cabinetNameNum);
			if(StringUtils.isNotBlank(stringBuffers.toString())){
				exchangeInstallmentSetting.setCabinetName(stringBuffers.toString().substring(0,stringBuffers.toString().length()-1));
			}
			List<ExchangeInstallmentStation> settingId2 = exchangeInstallmentStationMapper.findSettingId(exchangeInstallmentSetting.getId());
			exchangeInstallmentSetting.setStationNameNum(settingId2.size());

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
		if(id == null){
			return ExtResult.failResult("分期ID为空");
		}
		ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(id);
		if(exchangeInstallmentSetting == null){
			return ExtResult.failResult("未找到此分期");
		}
		List<ExchangeInstallmentCabinet> settingId = exchangeInstallmentCabinetMapper.findSettingId(exchangeInstallmentSetting.getId());
		for (ExchangeInstallmentCabinet exchangeInstallmentCabinet: settingId) {
			exchangeInstallmentCabinetMapper.deleteCabinetId(exchangeInstallmentCabinet);
		}
		List<ExchangeInstallmentStation> settingId3 = exchangeInstallmentStationMapper.findSettingId(exchangeInstallmentSetting.getId());
		for (ExchangeInstallmentStation exchangeInstallmentStation: settingId3) {
			exchangeInstallmentStationMapper.deleteStationId(exchangeInstallmentStation);
		}
		List<ExchangeInstallmentCustomer> settingId1 = exchangeInstallmentCustomerMapper.findSettingId(exchangeInstallmentSetting.getId());
		for (ExchangeInstallmentCustomer exchangeInstallmentCustomer: settingId1) {
			exchangeInstallmentCustomerMapper.deleteCustomerMobile(exchangeInstallmentCustomer);
		}
		List<ExchangeInstallmentCount> settingId2 = exchangeInstallmentCountMapper.findSettingId(exchangeInstallmentSetting.getId());
		for (ExchangeInstallmentCount exchangeInstallmentCount: settingId2) {
			List<ExchangeInstallmentCountDetail> countId = exchangeInstallmentCountDetailMapper.findCountId(exchangeInstallmentCount.getId());
			for (ExchangeInstallmentCountDetail exchangeInstallmentCountDetail: countId) {
				exchangeInstallmentCountDetailMapper.delete(exchangeInstallmentCountDetail.getId());
			}
			exchangeInstallmentCountMapper.delete(exchangeInstallmentCount.getId());
		}
		if (exchangeInstallmentSettingMapper.delete(id) >= 1) {
			exchangeInstallmentDetailMapper.deleteBySettingId(id);
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult addInstallmentSetting(String settingMaps) throws IOException, ParseException {
		if(settingMaps == null){
			return ExtResult.failResult("传参为空！");
		}
		Map settingMap = (Map)YhdgUtils.decodeJson(settingMaps,Map.class);
		if(settingMap == null){
			return ExtResult.failResult("传参为空！");
		}
		if(settingMap.get("agentId") == null){
			return ExtResult.failResult("营运商不能为空！");
		}
		long agentId = Long.parseLong((String) settingMap.get("agentId"));
		Agent agent = agentMapper.find(agentId);
		if(agent==null){
			return ExtResult.failResult("营运商不存在");
		}
		//解析换电押金分期设置
		String fullname = (String) settingMap.get("fullname");
		Date deadlineTime = DateUtils.parseDate(settingMap.get("deadlineTime").toString(), new String[]{Constant.DATE_FORMAT});
		Integer settingType = Integer.parseInt(settingMap.get("settingType").toString());
		Integer isActive = Integer.parseInt(settingMap.get("isActive").toString()) ;
		//解析换电分期期数总表
		List<Integer> standardCounts = (List<Integer>) settingMap.get("standardCounts");
		List<Integer> standardFeeTypes = (List<Integer>) settingMap.get("standardFeeTypes");
		List<Object> standardFeeMoneys = (List<Object>) settingMap.get("standardFeeMoneys");
		List<Object> standardFeePercentages = (List<Object>) settingMap.get("standardFeePercentages");
		//解析换电分期期数详情
		List<Integer> nums = (List<Integer>) settingMap.get("nums");
		List<Integer> feeTypes = (List<Integer>) settingMap.get("feeTypes");
		List<Object> feeMoneys = (List<Object>) settingMap.get("feeMoneys");
		List<Object> feePercentages = (List<Object>) settingMap.get("feePercentages");
		List<List<Integer>> customNums = (List<List<Integer>>) settingMap.get("customNums");
		List<List<Integer>> minForegiftPercentages = (List<List<Integer>>) settingMap.get("minForegiftPercentages");
		List<List<Integer>> minPacketPeriodPercentages = (List<List<Integer>>) settingMap.get("minPacketPeriodPercentages");

		//解析绑定骑手
		List<String> customerMobiles = (List<String>) settingMap.get("customerMobiles");
		//解析绑定设备
		List<String> cabineIds = (List<String>) settingMap.get("cabineIds");
		List<String> cabineNames = (List<String>) settingMap.get("cabineNames");
		//解析绑定设备
		List<String> stationIds = (List<String>) settingMap.get("stationIds");
		List<String> stationNames = (List<String>) settingMap.get("stationNames");

		//添加换电押金分期设置
		ExchangeInstallmentSetting exchangeInstallmentSetting = new ExchangeInstallmentSetting();
		exchangeInstallmentSetting.setSettingType(settingType);
		exchangeInstallmentSetting.setAgentId(agent.getId());
		exchangeInstallmentSetting.setAgentName(agent.getAgentName());
		exchangeInstallmentSetting.setAgentCode(agent.getAgentCode());
		exchangeInstallmentSetting.setDeadlineTime(deadlineTime);
		exchangeInstallmentSetting.setSettingType(settingType);
		exchangeInstallmentSetting.setFullname(fullname);
		exchangeInstallmentSetting.setSettingName(ExchangeInstallmentSetting.SettingType.getName(settingType));
		exchangeInstallmentSetting.setIsActive(isActive);
		exchangeInstallmentSetting.setCreateTime(new Date());
		exchangeInstallmentSettingMapper.insert(exchangeInstallmentSetting);
		//添加换电分期期数总表
		if(standardCounts!=null&&standardCounts.size()>0&&settingType==ExchangeInstallmentSetting.SettingType.STANDARD_STAGING.getValue()){
			int i=0;
			for (Integer standardCount:standardCounts) {
				ExchangeInstallmentCount exchangeInstallmentCount =new ExchangeInstallmentCount();
				exchangeInstallmentCount.setCount(standardCounts.get(i));
				exchangeInstallmentCount.setFeeType(standardFeeTypes.get(i));
				exchangeInstallmentCount.setFeeMoney(standardFeeMoneys.size() == 0?0:standardFeeMoneys.get(i)==null?0:(int)(Double.parseDouble(standardFeeMoneys.get(i).toString())*100));
				exchangeInstallmentCount.setFeePercentage(standardFeePercentages.size() == 0?0:standardFeePercentages.get(i)==null?0:(int)(Double.parseDouble(standardFeePercentages.get(i).toString())*100));
				exchangeInstallmentCount.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentCountMapper.insert(exchangeInstallmentCount);
				i++;
			}
		}else if(nums!=null&&nums.size()>0&&settingType==ExchangeInstallmentSetting.SettingType.CUSTOM_STAGING.getValue()){
			//添加换电分期期数详情
			int i=0;
			for (Integer num: nums) {
				ExchangeInstallmentCount exchangeInstallmentCount =new ExchangeInstallmentCount();
				exchangeInstallmentCount.setCount(num);
				exchangeInstallmentCount.setFeeType(feeTypes.get(i));
				exchangeInstallmentCount.setFeeMoney(feeMoneys.size()==0?0:feeMoneys.get(i)==null?0:(int)(Double.parseDouble(feeMoneys.get(i).toString())*100));
				exchangeInstallmentCount.setFeePercentage(feePercentages.size()==0?0:(feePercentages.get(i)==null)?0:(int)(Double.parseDouble(feePercentages.get(i).toString())*100));
				exchangeInstallmentCount.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentCountMapper.insert(exchangeInstallmentCount);
				int j =0;
				for (Integer customNum:customNums.get(i)) {
					ExchangeInstallmentCountDetail exchangeInstallmentCountDetail = new ExchangeInstallmentCountDetail();
					exchangeInstallmentCountDetail.setCountId(exchangeInstallmentCount.getId());
					exchangeInstallmentCountDetail.setFeeType(ExchangeInstallmentCountDetail.FeeType.NO_HANDLING_FEE.getValue());
					exchangeInstallmentCountDetail.setFeeMoney(0);
					exchangeInstallmentCountDetail.setFeePercentage(0);
					exchangeInstallmentCountDetail.setMinPacketPeriodMoney(0);
					exchangeInstallmentCountDetail.setMinForegiftMoney(0);
					exchangeInstallmentCountDetail.setMinForegiftPercentage(minForegiftPercentages.get(i).get(j));
					exchangeInstallmentCountDetail.setMinPacketPeriodPercentage(minPacketPeriodPercentages.get(i).get(j));
					exchangeInstallmentCountDetail.setNum(customNum);
					exchangeInstallmentCountDetailMapper.insert(exchangeInstallmentCountDetail);
					j++;
				}
				i++;
			}
		}

		//添加绑定骑手
		if(customerMobiles!=null&&customerMobiles.size()>0){
			for (String customerMobile:customerMobiles) {
				Customer byMobile = customerMapper.findByMobile(customerMobile);
				Long id =null;
				String mobile =null;
				String fullname1=null;
				if(byMobile!=null){
					id = byMobile.getId();
					mobile = byMobile.getMobile();
					fullname1 = byMobile.getFullname();

				}
				if(mobile==null){
					mobile=customerMobile;
				}
				ExchangeInstallmentCustomer exchangeInstallmentCustomer =new ExchangeInstallmentCustomer();
				exchangeInstallmentCustomer.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentCustomer.setCustomerId(id);
				exchangeInstallmentCustomer.setCustomerMobile(mobile);
				exchangeInstallmentCustomer.setCustomerFullname(fullname1);
				exchangeInstallmentCustomerMapper.insert(exchangeInstallmentCustomer);
			}
		}
		//添加绑定设备
		if(cabineIds!=null&&cabineIds.size()>0){
			Integer i=0;
			for (String cabineId:cabineIds) {
				ExchangeInstallmentCabinet exchangeInstallmentCabinet =new ExchangeInstallmentCabinet();
				exchangeInstallmentCabinet.setCabinetId(cabineIds.get(i));
				exchangeInstallmentCabinet.setCabinetName(cabineNames.get(i));
				exchangeInstallmentCabinet.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentCabinetMapper.insert(exchangeInstallmentCabinet);
				i++;
			}
		}
		//添加绑定站点
		if(stationIds!=null&&stationIds.size()>0){
			Integer i=0;
			for (String stationId:stationIds) {
				ExchangeInstallmentStation exchangeInstallmentStation =new ExchangeInstallmentStation();
				exchangeInstallmentStation.setStationId(stationIds.get(i));
				exchangeInstallmentStation.setStationName(stationNames.get(i));
				exchangeInstallmentStation.setSettingId(exchangeInstallmentSetting.getId());
				exchangeInstallmentStationMapper.insert(exchangeInstallmentStation);
				i++;
			}
		}
		return  DataResult.successResult(exchangeInstallmentSetting.getId());
	}
	@Transactional(rollbackFor = Throwable.class)
	public ExtResult updateExchangeInstallmentSetting(ExchangeInstallmentSetting installmentSetting){
		if(installmentSetting.getId()==null){
			return ExtResult.failResult("分期设置ID为空");
		}
		ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(installmentSetting.getId());
		if(exchangeInstallmentSetting == null){
			return ExtResult.failResult("此分期设置不存在");
		}
		int update = exchangeInstallmentSettingMapper.update(installmentSetting);
		if(update == 0){
			return ExtResult.failResult("修改失败");
		}else{
			return ExtResult.successResult("修改成功");
		}
	}

}
