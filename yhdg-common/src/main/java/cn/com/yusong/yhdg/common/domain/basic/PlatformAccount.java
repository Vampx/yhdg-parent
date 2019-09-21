package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@Setter
@Getter
public class PlatformAccount extends IntIdEntity {
	String partnerName;
	Integer balance;
	String mpAccountName;
	String mpOpenId;
	String alipayAccountName;
	String alipayAccount;
	Date createTime;

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

}
