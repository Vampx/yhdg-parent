package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.PlaylistDetailMapper;
import cn.com.yusong.yhdg.agentserver.persistence.yms.PlaylistDetailMaterialMapper;
import cn.com.yusong.yhdg.common.domain.yms.PlayListDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PlaylistDetailService {

    @Autowired
    PlaylistDetailMapper playlistDetailMapper;
    @Autowired
    PlaylistDetailMaterialMapper playlistAreaMaterialMapper;

    public Page findPage(PlayListDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(playlistDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(playlistDetailMapper.findPageResult(search));
        return page;
    }

    public PlayListDetail find(int id) {
        return playlistDetailMapper.find(id);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(PlayListDetail playListDetail) {
        int i = playlistDetailMapper.update(playListDetail);
        if (i==0){
            return ExtResult.failResult("修改失败，请重试");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(int id) {
        int i = playlistDetailMapper.delete(id);
        if (i==0){
            return ExtResult.failResult("删除失败，请重试");
        }
        return ExtResult.successResult();
    }

    public int insert(PlayListDetail playListDetail) {
        playListDetail.setCreateTime(new Date());
        return playlistDetailMapper.insert(playListDetail);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(int playlistId, Long[] materialId) {

        for (int a = 0; a < materialId.length; a++) {
            PlayListDetail playListDetail = playlistDetailMapper.findByPlaylistAndMaterial(playlistId, materialId[a]);
            if (null == playListDetail) {
                playListDetail = new PlayListDetail();
                playListDetail.setPlaylistId(playlistId);
                playListDetail.setMaterialId(materialId[a]);
                playListDetail.setOrderNum(1);
                insert(playListDetail);
            } else {
                continue;
                //return ExtResult.failResult("不可重复添加!");
            }
        }
        return ExtResult.successResult();

    }


}
