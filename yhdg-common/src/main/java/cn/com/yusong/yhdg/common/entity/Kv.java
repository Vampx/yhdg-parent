package cn.com.yusong.yhdg.common.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * key value 实体
 */
public class Kv {
    public static void uniqueCheck(List<Kv> list) {
        Set<String> keySet = new HashSet<String>();
        for(Kv kv : list) {
            if(keySet.contains(kv.k)) {
                throw new IllegalArgumentException(kv.k + " key exist");
            } else {
                keySet.add(kv.k);
            }
        }
    }

    private String k;
    private Object v;

    public Kv(String k, Object v) {
        this.k = k;
        this.v = v;
    }

    public Kv() {
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }
}
