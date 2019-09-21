package cn.com.yusong.yhdg.cabinetserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Platform;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PlatformMapper extends MasterMapper {
    public Platform find(@Param("id") Integer id);
    public int updateBalance(@Param("id") Integer id, @Param("balance") Integer balance);
}
