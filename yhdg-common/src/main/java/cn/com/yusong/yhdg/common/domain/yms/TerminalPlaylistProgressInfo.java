package cn.com.yusong.yhdg.common.domain.yms;

import java.util.List;

public class TerminalPlaylistProgressInfo {
    String playlistUid;
    Float percent;//转换进度
    double speed;//下载速度
    List<file> fileList ;

    public List<file> getFileList() {
        return fileList;
    }

    public void setFileList(List<file> fileList) {
        this.fileList = fileList;
    }

    public String getPlaylistUid() {
        return playlistUid;
    }

    public void setPlaylistUid(String playlistUid) {
        this.playlistUid = playlistUid;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public static class file{
        String path;
        String name;
        Long length;
        Float percent;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getLength() {
            return length;
        }

        public void setLength(Long length) {
            this.length = length;
        }

        public Float getPercent() {
            return percent;
        }

        public void setPercent(Float percent) {
            this.percent = percent;
        }
    }

}
