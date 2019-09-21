package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SmsConfig;
import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SmsConfigMapper extends MasterMapper {
    public SmsConfig find(int id);
    public SmsConfigInfo findInfo(int id);
    public List<SmsConfig> findByApp(int appId);
    public int findPageCount(SmsConfig search);
    public List<SmsConfig> findPageResult(SmsConfig search);
    public int insert(@Param("sql") String sql);
    public int insertSmsConfig(SmsConfig entity);
    public int update(SmsConfig entity);
    public int updateBalance(@Param("id") int id, @Param("balance") String balance, @Param("updateTime") Date updateTime);
    public int deleteByApp(int appId);
}
