package cn.com.yusong.yhdg.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewModel {
	public abstract int value();
	
	public int JSON = 1;
	public int INNER_PAGE = 2;
    public int JSON_BOOLEAN = 3;
    public int JSON_ARRAY = 4;
}