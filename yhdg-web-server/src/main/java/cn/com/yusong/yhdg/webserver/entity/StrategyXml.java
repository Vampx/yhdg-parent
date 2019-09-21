package cn.com.yusong.yhdg.webserver.entity;

import java.io.Serializable;

public class StrategyXml implements Serializable {
    public String uid;
    public String xml;

    public StrategyXml() {
    }

    public StrategyXml(String uid, String xml) {
        this.uid = uid;
        this.xml = xml;
    }
}