package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WithdrawTransferLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WithdrawTransferLogMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class WithdrawTransferLogService extends AbstractService {
    @Autowired
    private WithdrawTransferLogMapper withdrawTransferLogMapper;

    public WithdrawTransferLog find(Long id) {
        WithdrawTransferLog withdrawTransferLog = withdrawTransferLogMapper.find(id);
        return withdrawTransferLog;
    }

    public Page findPage(WithdrawTransferLog search) {
        Page page = search.buildPage();
        page.setTotalItems(withdrawTransferLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<WithdrawTransferLog> withdrawTransferLogList = withdrawTransferLogMapper.findPageResult(search);
        page.setResult(withdrawTransferLogList);

        return page;
    }

    public ExtResult insert(WithdrawTransferLog withdrawTransferLog) {
        withdrawTransferLog.setCreateTime(new Date());
        withdrawTransferLogMapper.insert(withdrawTransferLog);
        return ExtResult.successResult();
    }

}
