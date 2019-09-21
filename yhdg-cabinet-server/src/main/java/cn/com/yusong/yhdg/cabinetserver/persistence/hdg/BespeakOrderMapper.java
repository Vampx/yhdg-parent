package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
public interface BespeakOrderMapper extends MasterMapper {
    public BespeakOrder find(@Param("id") String id);

    public BespeakOrder findSuccessByCustomer(@Param("customerId") long customerId, @Param("status") Integer status);

    public BespeakOrder findByBespeak(@Param("bespeakCabinetId") String bespeakCabinetId, @Param("bespeakBoxNum") String bespeakBoxNum, @Param("status") Integer status);

    public int findFailCountForToday(@Param("customerId") long customerId);

    public void insert(BespeakOrder bespeakOrder);

    public int take(BespeakOrder bespeakOrder);

    public int updateStatus(@Param("id") String id,
                            @Param("fromStatus") Integer fromStatus,
                            @Param("toStatus") Integer toStatus,
                            @Param("cancelTime") Date cancelTime);
}
