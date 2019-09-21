package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface WeixinmpTemplateMessageMapper extends MasterMapper {

    public int insert(WeixinmpTemplateMessage entity);

    public int update(WeixinmpTemplateMessage entity);

    public int findPageCount(WeixinmpTemplateMessage search);

    public List<WeixinmpTemplateMessage> findPageResult(WeixinmpTemplateMessage search);

    public WeixinmpTemplateMessage find(int id);

    public int findCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("variable") String variable);
}
