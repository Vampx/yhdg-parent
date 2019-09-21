package cn.com.yusong.yhdg.staticserver.service.yms;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.yms.PlaylistData;
import cn.com.yusong.yhdg.common.domain.yms.PublishedMaterial;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylist;
import cn.com.yusong.yhdg.common.domain.yms.PublishedPlaylistDetail;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.ZipUtils;
import cn.com.yusong.yhdg.staticserver.persistence.yms.PublishedMaterialMapper;
import cn.com.yusong.yhdg.staticserver.persistence.yms.PublishedPlaylistDetailMapper;
import cn.com.yusong.yhdg.staticserver.persistence.yms.PublishedPlaylistMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class PackPlaylistTool {
    @Autowired
    PublishedPlaylistMapper publishedPlaylistMapper;
    @Autowired
    PublishedPlaylistDetailMapper publishedPlaylistDetailMapper;
    @Autowired
    PublishedMaterialMapper publishedMaterialMapper;

    public DataResult doPack(File root, Long playlistId, int version) throws IOException {
        PublishedPlaylist publishedPlaylist = publishedPlaylistMapper.findByVersion(playlistId, version);
        if(publishedPlaylist == null) {
            return DataResult.failResult("发布库中没有最新版本", null);
        }

        Map<File, String> files = new HashMap<File, String>();
        PlaylistData data = new PlaylistData();
        data.id = playlistId;
        data.playlistUid = String.format("playlist-%d-%d", publishedPlaylist.getPlaylistId(), publishedPlaylist.getVersion());
        data.playListName = publishedPlaylist.getPlaylistName();
        data.version = version;
        data.downloadDir.put("path", "/mnt/internal_sd/kdg/download/playlist");

        List<PublishedPlaylistDetail> detailList = publishedPlaylistDetailMapper.findByPlaylist(publishedPlaylist.getId());
        for(PublishedPlaylistDetail publishedPlaylistDetail : detailList) {
            PlaylistData.Detail detail = new PlaylistData.Detail();
            detail.name = publishedPlaylistDetail.getDetailName();
            detail.id = publishedPlaylistDetail.getId();
            if(publishedPlaylistDetail.getBeginTime() != null){
                detail.beginTime = String.valueOf(publishedPlaylistDetail.getBeginTime().getTime());
            }

            if(publishedPlaylistDetail.getEndTime() != null){
                detail.endTime = String.valueOf(publishedPlaylistDetail.getEndTime().getTime());
            }

            Date date = new Date();
            Long currentTime = date.getTime();
            Long endTime = publishedPlaylistDetail.getEndTime().getTime();
            //明细结束时间大于7天
            if (currentTime - endTime > (24 * 60 * 60 * 1000 * 7)) {
                continue;
            }

            List<PublishedMaterial> materialList = publishedMaterialMapper.findByDetail(publishedPlaylistDetail.getId(), ConstEnum.VideoConvertStatus.SUCCESS.getValue());
            for (PublishedMaterial material : materialList) {
                PlaylistData.File file = new PlaylistData.File();
                file.duration = material.getDuration();
                file.length = material.getSize();
                file.md5 = material.getMd5Sum();
                file.name = material.getMaterialName();
                file.path = material.getFtpPath();
                detail.fileList.add(file);
                files.put(new File(root, material.getFilePath()), AppUtils.convertFtpPath(material.getFilePath()));
            }
            data.detailList.add(detail);
        }

        File jsonFile = new File(root, "/static/temp/" + IdUtils.uuid());
        FileUtils.write(jsonFile, AppUtils.encodeJson(data), Constant.ENCODING_UTF_8);

        String path = String.format("playlist-%d.json", version);
        files.put(jsonFile, path);

        String zipPath = "/static/temp/adv.zip";
        File zip = new File(root, zipPath);
        ZipUtils.zip(zip, files);

        jsonFile.delete();

        return DataResult.successResult((Object) zipPath);
    }

}
