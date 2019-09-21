package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeixinmpMapper extends MasterMapper {
    Weixinmp find(int id);

    List<Weixinmp> findAll();

    int findPageCount(Weixinmp search);

    List<Weixinmp> findPageResult(Weixinmp search);

    List<Weixinmp> findList(Weixinmp search);

    List<Weixinmp> findByPartnerId(@Param("partnerId") Integer partnerId);

    int insert(Weixinmp entity);

    int insertSql(@Param("sql") String sql);

    int update(Weixinmp entity);

    int delete(int id);
}