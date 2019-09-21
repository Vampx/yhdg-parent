package cn.com.yusong.yhdg.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityControl {
	public abstract String[] limits();
}