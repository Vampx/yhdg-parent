package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlipayfwMapper extends MasterMapper {
    Alipayfw find(int id);

    List<Alipayfw> findAll();

    int findPageCount(Alipayfw search);

    List<Alipayfw> findPageResult(Alipayfw search);

    List<Alipayfw> findList(Alipayfw search);

    List<Alipayfw> findByPartnerId(@Param("partnerId") Integer partnerId);

    int insert(Alipayfw entity);

    int insertSql(@Param("sql") String sql);

    int update(Alipayfw entity);

    int delete(int id);
}