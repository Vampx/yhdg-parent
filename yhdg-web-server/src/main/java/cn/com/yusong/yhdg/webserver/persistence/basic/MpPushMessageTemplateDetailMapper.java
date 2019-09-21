package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MpPushMessageTemplateDetailMapper extends MasterMapper {
    public MpPushMessageTemplateDetail find(@Param("id") String id, @Param("weixinmpId") int weixinmpId, @Param("templateId") int templateId);
    public List<MpPushMessageTemplateDetail> findByTemplateId(@Param("weixinmpId") int weixinmpId, @Param("templateId") int templateId);
    public int findPageCount(MpPushMessageTemplateDetail search);
    public List<MpPushMessageTemplateDetail> findPageResult(MpPushMessageTemplateDetail search);
    public int update(MpPushMessageTemplateDetail search);
    public int insert(MpPushMessageTemplateDetail search);

}
