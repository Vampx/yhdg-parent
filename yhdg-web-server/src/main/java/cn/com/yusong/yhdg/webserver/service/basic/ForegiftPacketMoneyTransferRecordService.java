package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.ForegiftPacketMoneyTransferRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.ForegiftPacketMoneyTransferRecordMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForegiftPacketMoneyTransferRecordService extends AbstractService{

    @Autowired
    ForegiftPacketMoneyTransferRecordMapper foregiftPacketMoneyTransferRecordMapper;

    public ForegiftPacketMoneyTransferRecord find(Long id) {
        return foregiftPacketMoneyTransferRecordMapper.find(id);
    }

    public Page findPage(ForegiftPacketMoneyTransferRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(foregiftPacketMoneyTransferRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ForegiftPacketMoneyTransferRecord> pageResult = foregiftPacketMoneyTransferRecordMapper.findPageResult(search);
        page.setResult(pageResult);
        return page;
    }

}
