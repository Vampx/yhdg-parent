package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.RentInstallmentDetailMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RentInstallmentDetailService extends AbstractService {
	@Autowired
	RentInstallmentDetailMapper rentInstallmentDetailMapper;

	public Page findPage(RentInstallmentDetail search) {
		Page page = search.buildPage();
		page.setTotalItems(rentInstallmentDetailMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		page.setResult(rentInstallmentDetailMapper.findPageResult(search));
		return page;
	}

	public List<RentInstallmentDetail> findListBySettingId(Long settingId) {
		return rentInstallmentDetailMapper.findListBySettingId(settingId);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(RentInstallmentDetail rentInstallmentDetail) {
		if (rentInstallmentDetailMapper.insert(rentInstallmentDetail) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(RentInstallmentDetail rentInstallmentDetail) {
		if (rentInstallmentDetailMapper.update(rentInstallmentDetail) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Long settingId, Integer num) {
		if (rentInstallmentDetailMapper.delete(settingId, num) >= 1) {
			//调整期数
			List<RentInstallmentDetail> rentInstallmentDetailList = rentInstallmentDetailMapper.findListBySettingId(settingId);
			for (RentInstallmentDetail rentInstallmentDetail : rentInstallmentDetailList) {
				if (rentInstallmentDetail.getNum() > num) {
					rentInstallmentDetailMapper.updateNum(settingId, rentInstallmentDetail.getNum(), rentInstallmentDetail.getNum() - 1);
				}
			}
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}
}
