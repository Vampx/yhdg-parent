package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FwPushMessageTemplateMapper extends MasterMapper {
    public FwPushMessageTemplate find(@Param("alipayfwId") int alipayfwId, @Param("id") int id);
    public List<FwPushMessageTemplate> findByAppId(@Param("alipayfwId")int alipayfwId);
    public int findPageCount(FwPushMessageTemplate search);
    public List<FwPushMessageTemplate> findPageResult(FwPushMessageTemplate search);
    public int update(@Param("id")Integer id,
                      @Param("alipayfwId")Integer alipayfwId,
                      @Param("templateName")String templateName,
                      @Param("variable")String variable,
                      @Param("fwCode")String fwCode,
                      @Param("isActive")Integer isActive,
                      @Param("memo")String memo);
    public int insert(FwPushMessageTemplate search);
}
