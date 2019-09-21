package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.SystemConfigMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PacketPeriodOrderService extends AbstractService {
	@Autowired
	PacketPeriodOrderMapper packetPeriodOrderMapper;
	@Autowired
	SystemConfigMapper systemConfigMapper;

	public PacketPeriodOrder find(String id) {
		return packetPeriodOrderMapper.find(id);
	}

	public PacketPeriodOrder findOneEnabled(long customerId, int agentId) {
		PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customerId, PacketPeriodOrder.Status.USED.getValue(), agentId);
		if (packetPeriodOrder == null) {
			packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customerId, PacketPeriodOrder.Status.NOT_USE.getValue(), agentId);
		}
		return packetPeriodOrder;
	}

	public PacketPeriodOrder findLastEndTime(long customerId) {
		return packetPeriodOrderMapper.findLastEndTime(customerId, PacketPeriodOrder.Status.USED.getValue());
	}

	public List<PacketPeriodOrder> findListByNoUsed(long customerId) {
		return packetPeriodOrderMapper.findListByNoUsed(customerId, PacketPeriodOrder.Status.NOT_USE.getValue());
	}

	public List<PacketPeriodOrder> findListByShop(String shopId, String keyword, int offset, int limit) {
		List<PacketPeriodOrder> list = packetPeriodOrderMapper.findListByShop(shopId, keyword, offset, limit);
		for (PacketPeriodOrder order : list) {
			order.setBatteryTypeName(findBatteryType(order.getBatteryType()).getTypeName());
		}

		return list;
	}

	public int countShopTodayOrderMoney(String shopId, Date startTime, Date endTime) {
		return packetPeriodOrderMapper.countShopTodayOrderMoney(shopId, startTime, endTime);
	}

	public int findCountByShopAndStatus(String shopId, List<Integer> statusList) {
		return packetPeriodOrderMapper.findCountByShopAndStatus(shopId, statusList);
	}

	public List<PacketPeriodOrder> findListByCabinetId(int agentId, String cabinetId, String keyword, Date beginTime, Date endTime) {
		return packetPeriodOrderMapper.findListByCabinetId(agentId, cabinetId, keyword, beginTime, endTime);
	}


	public RestResult willExpireList(int agentId) {
		List<Map> list = new ArrayList<Map>();
		Date now = new Date();
		int expireDay = Integer.valueOf(systemConfigMapper.findConfigValue(ConstEnum.SystemConfigKey.PACKET_PERIOD_ORDER_EXPIRE.getValue()));
		Date expireDate = DateUtils.addDays(now, expireDay);

		List<PacketPeriodOrder> packetPeriodOrders = packetPeriodOrderMapper.findExpireList(expireDate, agentId);
		for (PacketPeriodOrder packetPeriodOrder : packetPeriodOrders) {
			Map line = new HashMap();
			line.put("id", packetPeriodOrder.getId());
			line.put("customerFullname", packetPeriodOrder.getCustomerFullname());
			line.put("customerMobile", packetPeriodOrder.getCustomerMobile());
			String leavingDay = AppUtils.formatTimeUnit((packetPeriodOrder.getEndTime().getTime() - now.getTime()) / 1000);
			line.put("leavingDay",leavingDay);
			line.put("endDate", packetPeriodOrder.getEndTime() == null ? "" : DateFormatUtils.format(packetPeriodOrder.getEndTime(), Constant.DATE_FORMAT));
			line.put("cabinetName",packetPeriodOrder.getCabinetName());
			list.add(line);
		}

		Map returnMap = new HashMap();
		returnMap.put("expireDay",expireDay);
		returnMap.put("count",packetPeriodOrders.size());
		returnMap.put("orderList",list);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnMap);
	}


}
