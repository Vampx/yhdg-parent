package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;

/**
 *   终端下载进度
 */
public class TerminalDownloadProgress extends StringIdEntity {

    String playlistProgressInfo;

    public String getPlaylistProgressInfo() {
        return playlistProgressInfo;
    }

    public void setPlaylistProgressInfo(String playlistProgressInfo) {
        this.playlistProgressInfo = playlistProgressInfo;
    }
}