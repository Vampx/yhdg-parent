package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformAccountMapper extends MasterMapper {
    PlatformAccount find(@Param("id") Integer id);
    int findPageCount(PlatformAccount PlatformAccount);
    List<PlatformAccount> findPageResult(PlatformAccount PlatformAccount);
    public int insert(PlatformAccount platformAccount);
    int updateBalance(@Param("id") int id, @Param("balance") long balance);
    public int delete(@Param("id") Integer id);

    int updateAccount(@Param("id") int id,
                      @Param("mpAccountName") String mpAccountName,
                      @Param("mpOpenId") String mpOpenId,
                      @Param("alipayAccountName") String alipayAccountName,
                      @Param("alipayAccount") String alipayAccount);
}
