package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Weixinma;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeixinmaMapper extends MasterMapper {
    Weixinma find(int id);

    List<Weixinma> findAll();

    int findPageCount(Weixinma search);

    List<Weixinma> findPageResult(Weixinma search);

    List<Weixinma> findList(Weixinma search);

    int insert(Weixinma entity);

    int update(Weixinma entity);

    List<Weixinma> findByPartnerId(@Param("partnerId") Integer partnerId);

    int delete(int id);
}