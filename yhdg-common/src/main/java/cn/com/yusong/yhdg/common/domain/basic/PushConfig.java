package cn.com.yusong.yhdg.common.domain.basic;

/**
 * 推送配置信息
 * 小米
 * 华为
 *
 *
 * Created by ruanjian5 on 2017/12/12.
 */
public class PushConfig {
    private String xiaomiAppSecret;
    private String xiaomiPackageName;

    private String hwAppId;
    private String hwAppSecret;

    private String  JpushMasterSecret;
    private String  JpushAppKey;

    private String MeizuAppId;
    private String MeizuAppSercretKey;

    public String getXiaomiAppSecret() {
        return xiaomiAppSecret;
    }

    public void setXiaomiAppSecret(String xiaomiAppSecret) {
        this.xiaomiAppSecret = xiaomiAppSecret;
    }

    public String getXiaomiPackageName() {
        return xiaomiPackageName;
    }

    public void setXiaomiPackageName(String xiaomiPackageName) {
        this.xiaomiPackageName = xiaomiPackageName;
    }

    public String getHwAppId() {
        return hwAppId;
    }

    public void setHwAppId(String hwAppId) {
        this.hwAppId = hwAppId;
    }

    public String getHwAppSecret() {
        return hwAppSecret;
    }

    public void setHwAppSecret(String hwAppSecret) {
        this.hwAppSecret = hwAppSecret;
    }

    public boolean isSameEntity(PushConfig config){
        if(this.xiaomiAppSecret.equals(config.xiaomiAppSecret)
                &&this.hwAppId.equals(config.hwAppId)
                &&this.hwAppSecret.equals(config.hwAppSecret)
                &&this.JpushAppKey.equals(config.JpushAppKey)
                &&this.JpushMasterSecret.equals(config.JpushMasterSecret)){
            return  true;
        }else
            return  false;
    }

    public String getJpushMasterSecret() {
        return JpushMasterSecret;
    }

    public void setJpushMasterSecret(String jpushMasterSecret) {
        JpushMasterSecret = jpushMasterSecret;
    }

    public String getJpushAppKey() {
        return JpushAppKey;
    }

    public void setJpushAppKey(String jpushAppKey) {
        JpushAppKey = jpushAppKey;
    }

    public String getMeizuAppId() {
        return MeizuAppId;
    }

    public void setMeizuAppId(String meizuAppId) {
        MeizuAppId = meizuAppId;
    }

    public String getMeizuAppSercretKey() {
        return MeizuAppSercretKey;
    }

    public void setMeizuAppSercretKey(String meizuAppSercretKey) {
        MeizuAppSercretKey = meizuAppSercretKey;
    }
}
