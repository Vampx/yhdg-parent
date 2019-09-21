package cn.com.yusong.yhdg.serviceserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface RentInsuranceOrderMapper extends MasterMapper {

    public List<ShopDayStats> findShopIncrement(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<ShopDayStats> findShopRefund(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<Map> findIncrement(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<Map> findRefund(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
