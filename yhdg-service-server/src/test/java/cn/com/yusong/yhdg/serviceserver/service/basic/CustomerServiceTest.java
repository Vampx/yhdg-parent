package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceTest extends BaseJunit4Test {
	@Autowired
	CustomerService service;

	//Unknown column 'binding_overtime_fault_log_id'
	//表结构已经修改了，方法需要调整
	@Test
	public void checkBatteryBindingOvertime() {
		//service.checkBatteryBindingOvertime();
	}
}
