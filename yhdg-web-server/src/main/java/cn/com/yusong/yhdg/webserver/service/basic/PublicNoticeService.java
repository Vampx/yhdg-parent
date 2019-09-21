package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.PublicNoticeMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PublicNoticeService extends AbstractService {

    @Autowired
    PublicNoticeMapper publicNoticeMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;

    public PublicNotice find(long id) {
        return publicNoticeMapper.find(id);
    }

    public Page findPage(PublicNotice search) {
        Page page = search.buildPage();
        page.setTotalItems(publicNoticeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PublicNotice> list = publicNoticeMapper.findPageResult(search);
        for (PublicNotice publicNotice : list) {
            if (publicNotice.getAgentId() != null)
                publicNotice.setAgentName(findAgentInfo(publicNotice.getAgentId()).getAgentName());
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(PublicNotice publicNotice) {
        publicNotice.setCreateTime(new Date());
        publicNoticeMapper.insert(publicNotice);
        return ExtResult.successResult();
    }

    public ExtResult update(PublicNotice publicNotice) {
        publicNoticeMapper.update(publicNotice);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        publicNoticeMapper.delete(id);
        pushMetaDataMapper.delete(String.valueOf(id));
        return ExtResult.successResult();
    }

    public int pushMetaDataCreate(String sourceId,Integer sourceType) {
        PushMetaData pushMetaData = new PushMetaData();
        pushMetaData.setSourceId(sourceId);
        pushMetaData.setSourceType(sourceType);
        pushMetaData.setCreateTime(new Date());
        return pushMetaDataMapper.insert(pushMetaData);
    }
}
