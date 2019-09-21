package cn.com.yusong.yhdg.routeserver.protocol;

import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class ResponseMessage {

    int rtnCode;
    String rtnMsg;

    public String encode() {
        Map<String, String> param = new LinkedTreeMap<String, String>();
        param.put("MsgCode", String.format("%09d", getMsgCode()));
        param.put("RtnCode", String.format("%d", getRtnCode()));
        param.put("RtnMsg", StringUtils.trimToEmpty(getRtnMsg()));
        writeContent(param);

        StringBuilder result = new StringBuilder();
        for(Map.Entry<String, String> entry : param.entrySet()) {
            if(result.length() != 0) {
                result.append("|");
            }
            result.append(entry.getKey() + "=" + entry.getValue());
        }
        return result.toString();
    }

    public abstract int getMsgCode();
    public abstract void writeContent(Map<String, String> param);

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }
}
