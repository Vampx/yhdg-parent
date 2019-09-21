package cn.com.yusong.yhdg.common.domain.yms;

import java.util.ArrayList;
import java.util.List;

public class DownloadQueueInfo {

    String time;
    List<String> terminalList = new ArrayList<String>() ;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getTerminalList() {
        return terminalList;
    }

    public void setTerminalList(List<String> terminalList) {
        this.terminalList = terminalList;
    }
}
