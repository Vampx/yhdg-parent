package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.yms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService extends AbstractService {
    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    TerminalOnlineMapper terminalOnlineMapper;
    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    PlaylistMapper playlistMapper;
    @Autowired
    TerminalDownloadProgressMapper terminalDownloadProgressMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CabinetMapper cabinetMapper;


    public Page findPage(Terminal search) {
        Page<Terminal> page = search.buildPage();
        page.setTotalItems(terminalMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Terminal> terminalList = terminalMapper.findPageResult(search);
        for (Terminal terminal : terminalList) {

            if (terminal.getAgentId() != null) {
                Agent agent = agentMapper.find(terminal.getAgentId());
                if (agent != null) {
                    terminal.setAgentName(agent.getAgentName());
                }
            }
        }
        page.setResult(terminalList);
        return page;
    }

    public Page findNotAssociatedPage(Terminal search) {
        Page<Terminal> page = search.buildPage();
        page.setTotalItems(terminalMapper.findNotAssociatedPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Terminal> terminalList = terminalMapper.findNotAssociatedPageResult(search);
        for (Terminal terminal : terminalList) {

        }
        page.setResult(terminalList);
        return page;
    }

    public Terminal find(String id) {
        return terminalMapper.find(id);
    }

    public ExtResult insert(Terminal terminal) {
        int total = terminalMapper.insert(terminal);
        return ExtResult.successResult();
    }

    public int update(String[] terminalIds, Integer playlistId) {
        int total = 0;
        for(int i = 0; i < terminalIds.length; i++) {
            Terminal terminal = new Terminal();
            terminal.setPlaylistId(playlistId);
            terminal.setId(terminalIds[i]);
            total = total + terminalMapper.update(terminal);
        }
        return total;
    }

    public ExtResult updateBasicInfo(Terminal terminal) {
        int total = terminalMapper.updateBasicInfo(terminal);
        if (total == 0){


            return ExtResult.successResult("修改失败");
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(String id) {
        terminalMapper.delete(id);
        terminalDownloadProgressMapper.delete(id);
        terminalOnlineMapper.delete(id);
        cabinetMapper.clearTerminalId(id, null);
        return ExtResult.successResult();
    }

    public ExtResult deletePlaylist(String id) {
        terminalMapper.installBlank(id);
        return ExtResult.successResult();
    }

    public ExtResult relevanceCabinet(String cabinetId, String terminalId) {
        if (cabinetMapper.updateTerminalId(cabinetId, terminalId) > 0) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("操作失败");
        }
    }

    public List<Terminal> findId() {
       return terminalMapper.findId();
    }

    public int updatePlaylistVersionList(Integer playlistId, String playlistVersion){
        return  terminalMapper.updatePlaylistVersionList(playlistId,playlistVersion);
    }
}
