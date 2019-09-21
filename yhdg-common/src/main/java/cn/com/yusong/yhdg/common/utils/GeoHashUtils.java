package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

import java.util.Calendar;

public class GeoHashUtils {
    private static final int NUMBER_OF_CHARACTERS = 8;

    public static void main(String[] args) {
        System.out.println(getGeoHashString(Constant.LNG, Constant.LAT));
    }

    public static String getGeoHashString(double lng, double lat) {
        return GeoHash.geoHashStringWithCharacterPrecision(lat, lng, NUMBER_OF_CHARACTERS);
    }

    public static double getDistanceInMeters(double lng1, double lat1, double lng2, double lat2) {
        return VincentyGeodesy.distanceInMeters(new WGS84Point(lat1, lng1), new WGS84Point(lat2, lng2));
    }

    private static final double EARTH_RADIUS = 6378137.0;

    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
