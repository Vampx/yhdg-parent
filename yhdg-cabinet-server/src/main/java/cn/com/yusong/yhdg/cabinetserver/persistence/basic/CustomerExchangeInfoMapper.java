package cn.com.yusong.yhdg.cabinetserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerExchangeInfoMapper extends MasterMapper {
    public CustomerExchangeInfo find(@Param("id") long id);
    public int updateErrorMessage(@Param("id") long id, @Param("errorTime") Date errorTime, @Param("errorMessage") String errorMessage);

}
