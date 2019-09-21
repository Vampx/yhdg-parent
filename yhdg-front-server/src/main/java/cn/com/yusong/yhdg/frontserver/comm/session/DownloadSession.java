package cn.com.yusong.yhdg.frontserver.comm.session;

public class DownloadSession implements Comparable {
    String id;
    int playlistId;

    public DownloadSession(String id, int playlistId) {
        this.id = id;
        this.playlistId = playlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadSession that = (DownloadSession) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        DownloadSession another = (DownloadSession) o;
        if(playlistId < another.playlistId) {
            return -1;
        } else if(playlistId > another.playlistId) {
            return 1;
        } else {
            return 0;
        }
    }
}
