package cn.com.yusong.yhdg.frontserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalCode;
import org.apache.ibatis.annotations.Param;

public interface TerminalCodeMapper {
    TerminalCode find(@Param("id") String id);
    TerminalCode findByCode(@Param("code") String code);
    int insert(TerminalCode entity);
}
