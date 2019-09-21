package cn.com.yusong.yhdg.appserver.entity.exception;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * 断言工具类
 */
public class AssertUtil {
    /**
     * 断言对象为非空，否则抛出包含指定错误信息的RespException。
     *
     * @param str        断言字符串
     * @param resultCode 错误码
     * @param resultMsg  错误码描述
     * @throws RespException
     */
    public static void notBlank(final String str, final int resultCode,
                                final String resultMsg) throws RespException {

        check(new AssertTemplate() {

            @Override
            public void doAssert() {

                Assert.isTrue(StringUtils.isNotBlank(str));
            }
        }, resultCode, resultMsg);
    }


    /**
     * 断言对象非null，否则抛出包含指定错误信息的RespException。
     *
     * @param object     待检查对象
     * @param resultCode 错误码
     * @throws RespException
     */
    public static void notNull(final Object object,
                               final int resultCode, final String resultMsg) throws RespException {

        check(new AssertTemplate() {

            @Override
            public void doAssert() {
                Assert.notNull(object);
            }
        }, resultCode, resultMsg);
    }


    /**
     * 断言表达式的值为true，否则抛出包含指定错误信息的RespException。
     *
     * @param expValue   断言表达式
     * @param resultCode 错误码
     * @throws RespException
     */
    public static void isTrue(final boolean expValue,
                              final int resultCode, final String resultMsg) throws RespException {

        check(new AssertTemplate() {
            @Override
            public void doAssert() {
                Assert.isTrue(expValue);
            }
        }, resultCode, resultMsg);
    }

    /**
     * 断言表达式的值为false，否则抛出包含指定错误信息的RespException。
     *
     * @param expValue   断言表达式
     * @param resultCode 错误码
     * @param resultMsg  错误码描述
     * @throws RespException
     */
    public static void isFalse(final boolean expValue, final int resultCode,
                               final String resultMsg) throws RespException {

        check(new AssertTemplate() {

            @Override
            public void doAssert() {
                Assert.isTrue(!expValue);
            }
        }, resultCode, resultMsg);
    }

    /**
     * 断言集合不为空或null，否则抛出包含指定错误信息的RespException。
     *
     * @param collection 待检查集合
     * @param resultCode 错误码
     * @param resultMsg  错误码描述
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static void notEmpty(final Collection collection, final int resultCode,
                                final String resultMsg) throws Exception {

        check(new AssertTemplate() {

            @Override
            public void doAssert() {

                Assert.notEmpty(collection);
            }
        }, resultCode, resultMsg);
    }


    /**
     * 断言内部接口。
     */
    private static interface AssertTemplate {

        /**
         * 实际断言操作。
         */
        public void doAssert();

    }

    /**
     * 断言检查。
     *
     * @param assertTemplate 断言内部接口
     * @param resultCode     结果码
     * @param resultMsg      结果描述
     * @throws RespException
     */
    private static void check(AssertTemplate assertTemplate, int resultCode,
                              String resultMsg) throws RespException {
        try {
            assertTemplate.doAssert();
        } catch (IllegalArgumentException e) {
            throw RespException.get(resultCode, resultMsg);
        }
    }
}
