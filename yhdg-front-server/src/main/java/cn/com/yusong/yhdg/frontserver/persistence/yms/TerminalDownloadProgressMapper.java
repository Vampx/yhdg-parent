package cn.com.yusong.yhdg.frontserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.springframework.stereotype.Component;

@Component("TerminalDownloadProgressMapper")
public interface TerminalDownloadProgressMapper extends MasterMapper {
    TerminalDownloadProgress find(String id);
    int insert(TerminalDownloadProgress entity);
    int update(TerminalDownloadProgress entity);
}
