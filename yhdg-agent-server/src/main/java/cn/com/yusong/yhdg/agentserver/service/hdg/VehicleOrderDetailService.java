//package cn.com.yusong.yhdg.agentserver.service.hdg;
//
//import cn.com.yusong.yhdg.common.entity.pagination.Page;
//import cn.com.yusong.yhdg.agentserver.persistence.hdg.VehicleOrderDetailMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class VehicleOrderDetailService {
//    @Autowired
//    VehicleOrderDetailMapper rentVehicleOrderDetailMapper;
//
//    public Page findPage(VehicleOrderDetail search) {
//        Page page = search.buildPage();
//        page.setTotalItems(rentVehicleOrderDetailMapper.findPageCount(search));
//        search.setBeginIndex(page.getOffset());
//        List<VehicleOrderDetail> list = rentVehicleOrderDetailMapper.findPageResult(search);
//        page.setResult(list);
//        return page;
//    }
//
//}
