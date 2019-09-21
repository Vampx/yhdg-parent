package cn.com.yusong.yhdg.common.entity;

import java.io.Serializable;

public class LoginQrcode implements Serializable {
    public String cabinetId;
    public String cabinetName;
    public Long customerId;
    public int status;

    public LoginQrcode() {
    }

    public LoginQrcode(String cabinetId, String cabinetName, int status) {
        this.cabinetId = cabinetId;
        this.cabinetName = cabinetName;
        this.status = status;
    }
}
