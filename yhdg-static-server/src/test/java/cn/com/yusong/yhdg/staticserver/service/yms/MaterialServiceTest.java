package cn.com.yusong.yhdg.staticserver.service.yms;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import cn.com.yusong.yhdg.staticserver.persistence.yms.MaterialIdGeneratorMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class MaterialServiceTest extends BaseJunit4Test {
    @Autowired
    MaterialService materialService;
    @Autowired
    MaterialIdGeneratorService materialIdGeneratorService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Material material = newMaterial(materialIdGeneratorService.newId(), agent.getId(), 1, 1);
        insertMaterial(material);

        assertNotNull(materialService.find(material.getId()));
    }

    @Test
    public void updateConvertStatus() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        long id = materialIdGeneratorService.newId();
        Material material = newMaterial(id, agent.getId(), 1, 1);
        insertMaterial(material);

        assertEquals(1, materialService.updateConvertStatus(id, 1, ""));
    }

    @Test
    public void updateFilePathSize() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        long id = materialIdGeneratorService.newId();
        Material material = newMaterial(id, agent.getId(), 1, 1);
        insertMaterial(material);

        assertEquals(1, materialService.updateFilePathSize(id, "aa", 1, "aa"));
    }

    @Test
    public void updateProgress() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        long id = materialIdGeneratorService.newId();
        Material material = newMaterial(id, agent.getId(), 1, 1);
        insertMaterial(material);

        assertEquals(1, materialService.updateProgress(id, 1));
    }

//    @Test
//    public void create() throws Exception {
//        Partner partner = newPartner();
//        insertPartner(partner);
//
//        Agent agent = newAgent(partner.getId());
//        insertAgent(agent);
//
//        long id = materialIdGeneratorService.newId();
//        Material material = newMaterial(id, agent.getId(), 1, 1);
//
//        String rootPath = new File("").getCanonicalPath();
//        File file = new File(rootPath + "\\aa.jpg");
////        if(!file.exists()){
////            file.createNewFile();
////        }
////        file = new File("C:\\Users\\yusong\\Videos\\aa.mp4");
////        FileInputStream in =  new FileInputStream(file);
////        MultipartFile multipartFile = new MockMultipartFile("bb",in);
////
////        materialService.create(multipartFile, material);
////
////        assertNotNull(materialService.find(material.getId()));
//
////        file.delete();
//    }
//
//    @Test
//    public void replace() {
//    }
}