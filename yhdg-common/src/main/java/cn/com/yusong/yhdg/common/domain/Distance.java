package cn.com.yusong.yhdg.common.domain;

import java.util.Comparator;

public interface Distance {
    public double getDistance();

    public void setDistance(double distance);

    public Double getLng();

    public Double getLat();

    public static Comparator<Distance> comparator = new Comparator<Distance>() {
        @Override
        public int compare(Distance a, Distance b) {
            return (int) (a.getDistance() - b.getDistance());
        }
    };



}
