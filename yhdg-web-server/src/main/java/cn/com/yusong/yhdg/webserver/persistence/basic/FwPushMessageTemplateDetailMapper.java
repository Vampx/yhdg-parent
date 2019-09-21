package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface FwPushMessageTemplateDetailMapper extends MasterMapper {
    public FwPushMessageTemplateDetail find(@Param("id") String id, @Param("alipayfwId") int alipayfwId, @Param("templateId") int templateId);
    public List<FwPushMessageTemplateDetail> findByTemplateId(@Param("alipayfwId") int alipayfwId, @Param("templateId") int templateId);
    public int findPageCount(FwPushMessageTemplateDetail search);
    public List<FwPushMessageTemplateDetail> findPageResult(FwPushMessageTemplateDetail search);
    public int update(FwPushMessageTemplateDetail search);
    public int insert(FwPushMessageTemplateDetail search);

}
