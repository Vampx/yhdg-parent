package cn.com.yusong.yhdg.common.entity;

import java.io.Serializable;
import java.util.Date;

public class TransientData implements Serializable {
    public Object value;
    public Date createTime = new Date();

    public static TransientData build(Object value) {
        TransientData instance = new TransientData();
        instance.value = value;
        return instance;
    }
}
