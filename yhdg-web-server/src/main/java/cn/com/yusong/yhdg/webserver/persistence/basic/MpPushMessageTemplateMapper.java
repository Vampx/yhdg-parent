package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MpPushMessageTemplateMapper extends MasterMapper {
    public MpPushMessageTemplate find(@Param("weixinmpId")int weixinmpId, @Param("id") long id);
    public List<MpPushMessageTemplate> findByWeixinmpId(@Param("weixinmpId")int weixinmpId);
    public int findPageCount(MpPushMessageTemplate search);
    public int findByReceiverPageCount(MpPushMessageTemplate search);
    public List<MpPushMessageTemplate> findByUserPageResult(MpPushMessageTemplate search);
    public List<MpPushMessageTemplate> findPageResult(MpPushMessageTemplate search);
    public int update(MpPushMessageTemplate mpPushMessageTemplate);
    public int insert(MpPushMessageTemplate search);
}
