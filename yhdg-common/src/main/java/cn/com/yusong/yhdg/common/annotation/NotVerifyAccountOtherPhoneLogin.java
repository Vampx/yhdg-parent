package cn.com.yusong.yhdg.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotVerifyAccountOtherPhoneLogin {
}
