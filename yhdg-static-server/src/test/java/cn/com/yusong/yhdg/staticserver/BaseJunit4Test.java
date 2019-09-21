package cn.com.yusong.yhdg.staticserver;

import cn.com.yusong.yhdg.common.test.TestHelper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:applicationContext.xml"}) //加载配置文件
@Transactional(rollbackFor = Throwable.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BaseJunit4Test extends TestHelper {
    @Autowired
    protected JdbcTemplate historyTemplate;
}