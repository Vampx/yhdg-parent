package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalMapper extends MasterMapper {
    Terminal find(String id);

    int findPageCount(Terminal terminal);

    List<Terminal> findPageResult(Terminal terminal);

    int findNotAssociatedPageCount(Terminal terminal);

    List<Terminal> findNotAssociatedPageResult(Terminal terminal);

    String hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    List<Terminal> findByPlaylist(long playlistId);

    List<String> findIdByStrategy(long strategyId);

    List<Terminal> findId();

    int insert(Terminal terminal);

    int installBlank(String id);

    int update(Terminal terminal);

    int updateBasicInfo(Terminal terminal);

    int delete(String id);

    int updatePlaylistVersionList(@Param("playlistId") Integer playlistId, @Param("playlistVersion") String playlistVersion);
}
