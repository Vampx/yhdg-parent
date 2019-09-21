package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerMapper extends MasterMapper {
    public Customer find(long id);

    public Customer findByMobile(@Param("partnerId")int partnerId, @Param("mobile") String mobile);

    public int updateBalance(@Param("id") long id, @Param("balance") long balance, @Param("giftBalance") long giftBalance);

    public int updateAgent(@Param("id") long id, @Param("agentId") int agentId);

    public int updateCabinet(@Param("id") long id, @Param("belongCabinetId") String belongCabinetId);

    public int updateIdCardAuthRecordStats(@Param("id") long id, @Param("idCardAuthRecordStatus") Integer idCardAuthRecordStatus);

    public int updateHdForegiftStatus(@Param("id") long id, @Param("hdForegiftStatus") Integer hdForegiftStatus);

    public int updateZdForegiftStatus(@Param("id") long id, @Param("zdForegiftStatus") Integer zdForegiftStatus);

    int updateLaxinInfo(@Param("id") long id, @Param("laxinMobile") String laxinMobile, @Param("laxinFullname") String laxinFullname);
}
