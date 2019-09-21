package cn.com.yusong.yhdg.common.domain.yms;

import java.util.List;

public class PlaylistProgressInfo {
    int id;
    int version;
    Float percent;//转换进度
    double speed;//下载速度
    List<Playlist> playlist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public List<Playlist> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Playlist> playlist) {
        this.playlist = playlist;
    }

    public static class File{
        String path;
        String name;
        Long length;
        Long downloadBytes;

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

        public Long getDownloadBytes() {
            return downloadBytes;
        }

        public void setDownloadBytes(Long downloadBytes) {
            this.downloadBytes = downloadBytes;
        }
    }

    public static class Playlist {
        Integer id;
        List<File> fileList;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<File> getFileList() {
            return fileList;
        }

        public void setFileList(List<File> fileList) {
            this.fileList = fileList;
        }
    }


}
