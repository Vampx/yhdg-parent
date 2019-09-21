package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecordTransferLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.LaxinRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.LaxinRecordTransferLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class LaxinRecordService {
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    LaxinRecordTransferLogMapper laxinRecordTransferLogMapper;

    public LaxinRecord find(String id) {
        return laxinRecordMapper.find(id);
    }

    public Page findPage(LaxinRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(laxinRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(laxinRecordMapper.findPageResult(search));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public int resetAccount(String id, String mpOpenId, String accountName, String operatorName) {
        int effect = laxinRecordMapper.resetAccount(id, LaxinRecord.Status.FAIL.getValue(), LaxinRecord.Status.TRANSFER.getValue(), mpOpenId, accountName);
        if (effect > 0) {
            LaxinRecordTransferLog transferLog = new LaxinRecordTransferLog();
            transferLog.setRecordId(id);
            transferLog.setOperatorName(operatorName);
            transferLog.setContent(String.format("重置[mpOpenId=%s, accountName=%s]", mpOpenId, accountName));
            transferLog.setCreateTime(new Date());
            laxinRecordTransferLogMapper.insert(transferLog);
        }
        return effect;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateCancel(LaxinRecord laxinRecord) {
        laxinRecord.setStatus(LaxinRecord.Status.CANCEL.getValue());
        int total = laxinRecordMapper.updateCancel(laxinRecord);
        if (total == 1) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("取消失败！");
        }
    }
}
