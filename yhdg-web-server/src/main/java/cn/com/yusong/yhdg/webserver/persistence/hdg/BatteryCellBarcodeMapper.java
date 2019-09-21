package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellBarcode;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryCellBarcodeMapper extends MasterMapper {

	String findMaxCode(@Param("cellFormatId") long cellFormatId);

	BatteryCellBarcode findMaxCodeCellBarcode(@Param("cellFormatId") long cellFormatId);

	BatteryCellBarcode findByBarcode(@Param("barcode") String barcode);

	List<BatteryCellBarcode> findList(@Param("cellFormatId") long cellFormatId);

	int findPageCount(BatteryCellBarcode batteryCellBarcode);

	List<BatteryCellBarcode> findPageResult(BatteryCellBarcode batteryCellBarcode);

	int insert(BatteryCellBarcode batteryCellBarcode);

	int update(BatteryCellBarcode batteryCellBarcode);

	int delete(@Param("id") long id);

}
