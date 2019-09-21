package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.persistence.yms.PlaylistMapper;
import cn.com.yusong.yhdg.common.domain.yms.Playlist;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService extends AbstractService {

    @Autowired
    PlaylistMapper playlistMapper;


   public Playlist find(Integer id){
       return playlistMapper.find(id);
    }
}
