package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PlatformAccountMapper extends MasterMapper {
    public PlatformAccount find(@Param("id")int id);
    public int updateBalance(@Param("id") int id, @Param("balance") int balance);
}
