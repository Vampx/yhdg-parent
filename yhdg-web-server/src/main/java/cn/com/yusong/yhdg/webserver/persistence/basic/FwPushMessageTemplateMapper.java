package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FwPushMessageTemplateMapper extends MasterMapper {
    public FwPushMessageTemplate find(@Param("alipayfwId") int alipayfwId, @Param("id") int id);
    public List<FwPushMessageTemplate> findByAppId(@Param("alipayfwId")int alipayfwId);
    public int findPageCount(FwPushMessageTemplate search);
    public List<FwPushMessageTemplate> findPageResult(FwPushMessageTemplate search);
    public int update(FwPushMessageTemplate fwPushMessageTemplate);
    public int insert(FwPushMessageTemplate search);
}
