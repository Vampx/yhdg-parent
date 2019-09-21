package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface PublicNoticeMapper extends MasterMapper {

    PublicNotice find(long id);
    int findPageCount(PublicNotice search);
    List<PublicNotice> findPageResult(PublicNotice search);
    int insert(PublicNotice entity);
    int update(PublicNotice entity);
    int delete(long id);
}
