package cn.com.yusong.yhdg.agentserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylistDetailMaterial;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhoub on 2017/8/9.
 */
public interface PublishedPlaylistDetailMaterialMapper extends MasterMapper {
    int insert(PublishedPlaylistDetailMaterial publishedPlaylistAreaMaterial);
    int deleteByDetailId(@Param("detailId") Long detailId);

}
