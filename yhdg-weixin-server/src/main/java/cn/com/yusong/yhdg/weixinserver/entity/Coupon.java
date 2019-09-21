package cn.com.yusong.yhdg.weixinserver.entity;

public class Coupon {

    public static class CouponKey {
        public int couponType;
        public String couponId;

        public CouponKey(int couponType, String couponId) {
            this.couponType = couponType;
            this.couponId = couponId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CouponKey key = (CouponKey) o;

            if (couponType != key.couponType) return false;
            return couponId.equals(key.couponId);

        }

        @Override
        public int hashCode() {
            int result = couponType;
            result = 31 * result + couponId.hashCode();
            return result;
        }
    }
}
