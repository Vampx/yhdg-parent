package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeixinmpTemplateMessageMapper extends MasterMapper {

    public WeixinmpTemplateMessage find(int id);

    /**
     * 查询要发送的消息
     * @param status 状态
     * @param limit 条数
     * @param lastId 最后的id
     * @return 查询到的数据集合
     */
    List<WeixinmpTemplateMessage> findListByStatus(@Param("status") int status, @Param("limit") int limit, @Param("lastId") Long lastId);

    /**
     * 送消息结束修改状态数据
     * @param id id
     * @param handleTime 处理时间
     * @param status 状态
     * @param resendNum 发送次数
     * @return 更改的条数
     */
    int complete(@Param("id") long id, @Param("handleTime") Date handleTime, @Param("status") int status, @Param("resendNum") Integer resendNum);

    long findMessageCountByStatus(@Param("status") int status, @Param("type") int type, @Param("begin") Date begin, @Param("end") Date end);

    long findReadCountByStatus(@Param("status") int status, @Param("type") int type, @Param("begin") Date begin, @Param("end") Date end);

    long findReadCountSumByStatus(@Param("status") int status, @Param("type") int type, @Param("begin") Date begin, @Param("end") Date end);

    public int insert(WeixinmpTemplateMessage message);

    int updateStatus(@Param("status") int status, @Param("id") long id, @Param("handleTime") Date handleTime);

    int updateVariable(@Param("id") long id, @Param("variable") String variable);
}
