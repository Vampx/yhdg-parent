package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MpPushMessageTemplateMapper extends MasterMapper {
    public MpPushMessageTemplate find(@Param("weixinmpId")int weixinmpId, @Param("id") long id);
    public List<MpPushMessageTemplate> findByWeixinmpId(@Param("weixinmpId")int weixinmpId);
    public int findPageCount(MpPushMessageTemplate search);
    public List<MpPushMessageTemplate> findPageResult(MpPushMessageTemplate search);
    public int update(@Param("id")Integer id,
                      @Param("weixinmpId")Integer weixinmpId,
                      @Param("templateName")String templateName,
                      @Param("variable")String variable,
                      @Param("mpCode")String mpCode,
                      @Param("isActive")Integer isActive,
                      @Param("memo")String memo);
    public int insert(MpPushMessageTemplate search);
}
