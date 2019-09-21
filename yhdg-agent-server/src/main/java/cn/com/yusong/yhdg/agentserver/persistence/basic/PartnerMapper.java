package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartnerMapper extends MasterMapper {
    public Partner find(int id);
    int findPageCount(Partner search);
    List<Partner> findPageResult(Partner search);
    List<Partner> findList(Partner search);
    public List<Partner> findAll();
    public int insert(Partner partner);
    public int insertSql(@Param("sql") String sql);
    public int update(Partner partner);
    public int delete(int id);

}
