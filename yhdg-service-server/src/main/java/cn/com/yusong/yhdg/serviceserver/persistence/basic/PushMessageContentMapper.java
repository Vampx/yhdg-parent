package cn.com.yusong.yhdg.serviceserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.PushMessageContent;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PushMessageContentMapper extends MasterMapper {
    public PushMessageContent find(int id);
    public int insert(PushMessageContent content);
}
