package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class ExchangeInstallmentDetailService extends AbstractService {
	@Autowired
	ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;

	public Page findPage(ExchangeInstallmentDetail search) {
		Page page = search.buildPage();
		page.setTotalItems(exchangeInstallmentDetailMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		page.setResult(exchangeInstallmentDetailMapper.findPageResult(search));
		return page;
	}

	public List<ExchangeInstallmentDetail> findListBySettingId(Long settingId) {
		return exchangeInstallmentDetailMapper.findListBySettingId(settingId);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(ExchangeInstallmentDetail exchangeInstallmentDetail) {
		if (exchangeInstallmentDetailMapper.insert(exchangeInstallmentDetail) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(ExchangeInstallmentDetail exchangeInstallmentDetail) {
		if (exchangeInstallmentDetailMapper.update(exchangeInstallmentDetail) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Long settingId, Integer num) {
		if (exchangeInstallmentDetailMapper.delete(settingId, num) >= 1) {
			//调整期数
			List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = exchangeInstallmentDetailMapper.findListBySettingId(settingId);
			for (ExchangeInstallmentDetail exchangeInstallmentDetail : exchangeInstallmentDetailList) {
				if (exchangeInstallmentDetail.getNum() > num) {
					exchangeInstallmentDetailMapper.updateNum(settingId, exchangeInstallmentDetail.getNum(), exchangeInstallmentDetail.getNum() - 1);
				}
			}
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}
}
