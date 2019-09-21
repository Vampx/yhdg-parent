package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeixinmpTemplateMessageMapper extends MasterMapper {
    public List<WeixinmpTemplateMessage> findAgent(@Param("agentId") int agentId, @Param("sourceTypes") Integer[] sourceTypes, @Param("types") Integer[] types, @Param("source") Integer source, @Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
}
