package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryCellFormatMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryCellModelMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryCellModelService extends AbstractService {
	@Autowired
	BatteryCellModelMapper batteryCellModelMapper;
	@Autowired
	BatteryCellFormatMapper batteryCellFormatMapper;

	public BatteryCellModel find(Long id) {
		return batteryCellModelMapper.find(id);
	}

	public Page findPage(BatteryCellModel search) {
		Page page = search.buildPage();
		page.setTotalItems(batteryCellModelMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<BatteryCellModel> batteryCellModelList = batteryCellModelMapper.findPageResult(search);
		page.setResult(batteryCellModelList);
		return page;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(BatteryCellModel batteryCellModel) {
		batteryCellModel.setCreateTime(new Date());
		if (batteryCellModelMapper.insert(batteryCellModel) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("创建失败");
		}
	}

	public ExtResult update(BatteryCellModel batteryCellModel) {
		if (batteryCellModelMapper.update(batteryCellModel) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Long id) {
		if (batteryCellFormatMapper.findByCellModelId(id).size() > 0) {
			return ExtResult.failResult("该电芯型号已存在电芯规格，不可删除");
		}
		if (batteryCellModelMapper.delete(id) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}
}
