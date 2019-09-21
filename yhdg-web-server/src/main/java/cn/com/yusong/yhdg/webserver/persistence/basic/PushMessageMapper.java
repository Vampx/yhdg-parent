package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PushMessageMapper extends MasterMapper {
    PushMessage find(long id);
    int findPageCount(PushMessage search);
    List<PushMessage> findPageResult(PushMessage search);
}
