package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwTemplateMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AlipayfwTemplateMessageMapper extends MasterMapper {
    public int insert(AlipayfwTemplateMessage entity);

    public int update(AlipayfwTemplateMessage entity);

    public int findPageCount(AlipayfwTemplateMessage search);

    public List<AlipayfwTemplateMessage> findPageResult(AlipayfwTemplateMessage search);

    public AlipayfwTemplateMessage find(Long id);

    public int findCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("variable") String variable);
}
