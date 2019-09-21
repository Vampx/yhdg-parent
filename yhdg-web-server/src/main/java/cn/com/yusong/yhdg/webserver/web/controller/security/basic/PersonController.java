package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Part;
import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.service.basic.PartService;
import cn.com.yusong.yhdg.webserver.service.basic.PersonService;
import cn.com.yusong.yhdg.webserver.service.basic.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/person")
public class PersonController extends SecurityController {

    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.USER_PHOTO_PATH.getValue();

    @Autowired
    PersonService personService;
    @Autowired
    PartService partService;
    @Autowired
    AppConfig appConfig;

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "unique.htm")
    public ExtResult unique(Long id, String mobile) {
        boolean unique = personService.findUnique(id, mobile);
        if(unique) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("手机号重复");
        }
    }

    @SecurityControl(limits = "basic.Person:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Person:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Person search) {
        return PageResult.successResult(personService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add() {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id) {
        Person entity = personService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            List<Part> partTypeList = new ArrayList<Part>();
            for (Part.PartType partType : Part.PartType.values()) {
                Part part = new Part();
                part.setPartType(partType.getValue());
                part.setPartTypeName(partType.getName());
                partTypeList.add(part);
            }
            model.addAttribute("partTypeList", partTypeList);
            List<Part> platformList = partService.findList(entity.getMobile(), Part.PartType.PLATFORM.getValue());
            entity.setPlatformList(platformList);
            List<Part> exportList = partService.findList(entity.getMobile(), Part.PartType.EXPORT.getValue());
            entity.setExportList(exportList);
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
        }
        return "/security/basic/person/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Person entity) throws IOException {
        return personService.create(entity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Person entity) throws IOException {
        List<String> permIdList = new ArrayList<String>();
        if (entity.getPermIds() != null) {
            String[] str = StringUtils.split(entity.getPermIds(), ",");
            for (int i = 0; i < str.length; i++) {
                if (i != 0 && i != str.length - 1) {
                    permIdList.add(str[i].trim());
                }else{
                    permIdList.add(str[i].replace("[", "").replace("]", "").trim());
                }
            }
        }
        entity.setPermIdList(permIdList);
        return personService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        personService.delete(id);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        Person entity = personService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("pushTypeEnum", ConstEnum.PushType.values());
        }
        return "/security/basic/person/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_person.htm")
    public void selectPerson(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "photo_path.htm", method = RequestMethod.GET)
    public void portrait() {
    }

    @RequestMapping(value = "photo_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = YhdgUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(url + UPLOAD_FILE_URL, targetFile, Collections.EMPTY_MAP); //upload to static server
        if(httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>)list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/basic/person/photo_path_response";
    }

    @RequestMapping("find_by_agent.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public List<Person> findByAgent(Integer agentId) {
        return personService.findByAgent(agentId);
    }
}
