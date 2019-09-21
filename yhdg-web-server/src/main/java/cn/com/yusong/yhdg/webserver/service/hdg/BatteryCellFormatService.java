package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellBarcode;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellFormat;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryCellFormatService extends AbstractService {

    @Autowired
    BatteryCellFormatMapper batteryCellFormatMapper;
    @Autowired
    BatteryCellBarcodeMapper batteryCellBarcodeMapper;

    public BatteryCellFormat find(long id) {
        return batteryCellFormatMapper.find(id);
    }

    public Page findPage(BatteryCellFormat search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryCellFormatMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryCellFormat> list = batteryCellFormatMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(BatteryCellFormat batteryCellFormat) {
        batteryCellFormat.setCreateTime(new Date());
        int result = batteryCellFormatMapper.insert(batteryCellFormat);
        if (result > 0) {
            return DataResult.successResult(batteryCellFormat.getId());
        }
        return DataResult.failResult("对不起! 保存失败", null);
    }

    public ExtResult update(BatteryCellFormat batteryCellFormat) {
        batteryCellFormatMapper.update(batteryCellFormat);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        List<BatteryCellBarcode> list = batteryCellBarcodeMapper.findList(id);
        if (list.size() > 0) {
            return ExtResult.failResult("该规格已生成电芯条码，不可删除！");
        }
        batteryCellFormatMapper.delete(id);
        return ExtResult.successResult();
    }
}
