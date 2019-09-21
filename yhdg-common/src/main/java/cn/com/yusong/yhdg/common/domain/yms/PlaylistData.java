package cn.com.yusong.yhdg.common.domain.yms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 播放列表数据
 */
public class PlaylistData {
    public String playListName;
    public String playlistUid;
    public Long id;
    public Integer version;

    public List<Detail> detailList = new ArrayList<Detail>();
    public List subtitleList = new ArrayList();
    public Map downloadDir = new HashMap();

    public static class Detail  {
        public Long id;
        public String name;
        public String beginTime;
        public String endTime;
        public List<File> fileList = new ArrayList<File>();
    }

    public static class File  {
        public String path;
        public String name;
        public String md5;
        public Integer duration;
        public Long length;
    }
}
