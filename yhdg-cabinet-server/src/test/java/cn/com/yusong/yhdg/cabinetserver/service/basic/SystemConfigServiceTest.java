package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SystemConfigServiceTest extends BaseJunit4Test {
    @Autowired
    SystemConfigService systemConfigService;

    @Test
    public void findConfigValue() throws DecoderException {
        String id = ConstEnum.SystemConfigKey.WEIXIN_URL.getValue();
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
        memCachedClient.delete(key);

        systemConfigService.findConfigValue(id);

        assertNotNull(memCachedClient.get(key));

       // ByteBuf buf = Unpooled.buffer(1024);
      //  buf.writeBytes(Hex.decodeHex("000".toCharArray()));

        //创建一个16字节的buffer,这里默认是创建heap buffer
        ByteBuf buf = Unpooled.buffer(16);
        //写数据到buffer
        for(int i=0; i<16; i++){
            buf.writeByte(i+1);
        }
        //读数据
        for(int i=0; i<buf.capacity(); i++){
            System.out.print(buf.getByte(i)+", ");
        }


    }

}
