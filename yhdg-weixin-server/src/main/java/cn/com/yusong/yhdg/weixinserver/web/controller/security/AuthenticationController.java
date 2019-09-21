package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.web.controller.AbstractController;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import cn.com.yusong.yhdg.weixinserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.weixinserver.utils.DownloadFileUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cloudauth.model.v20180916.GetMaterialsRequest;
import com.aliyuncs.cloudauth.model.v20180916.GetMaterialsResponse;
import com.aliyuncs.cloudauth.model.v20180916.GetStatusRequest;
import com.aliyuncs.cloudauth.model.v20180916.GetStatusResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 实名认证完成跳转controller
* */
@Controller
@RequestMapping(value = "/authentication")
public class AuthenticationController extends AbstractController {
    private static final Logger log = LogManager.getLogger(AuthenticationController.class);

    @Autowired
    AppConfig appConfig;
    @Autowired
    CustomerService customerService;

    @NotLogin
    @RequestMapping(value = "/ok.htm")
    public void ok(long customerId, String ticketId, HttpServletResponse httpResponse) throws IOException {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",             //默认
                "LTAIKVjonjAmq60b",         //您的Access Key ID
                "zHfMILqrDGqiaLKa2rbnADetVGEsaK");    //您的Access Key Secret
        IAcsClient client = new DefaultAcsClient(profile);
        String biz = "h5-customer-register"; //您在控制台上创建的、采用RPH5BioOnly认证方案的认证场景标识, 创建方法：https://help.aliyun.com/document_detail/59975.html

//2. 服务端将认证URL(带token)传递给H5前端
//3. H5前端跳转认证URL
//4. 用户按照认证H5流程页面的指引，提交认证资料
//5. 认证流程结束跳转指定的重定向URL(指定方法参看：https://help.aliyun.com/document_detail/58644.html?#H5Server)
//6. 服务端查询认证状态(建议以服务端调接口确认的为准)
//GetStatus接口文档：https://help.aliyun.com/document_detail/57049.html
        String facePic = null;
        String fullname = null;
        String idCard = null;
        String msg = null;
        int statusCode = 0; //-1 未认证, 0 认证中, 1 认证通过, 2 认证不通过
        GetStatusRequest getStatusRequest = new GetStatusRequest();
        getStatusRequest.setBiz(biz);
        getStatusRequest.setTicketId(ticketId);

        if (log.isDebugEnabled()) {
            log.debug("GetStatusRequest: {}", AppUtils.encodeJson2(getStatusRequest));
        }

        try {
            GetStatusResponse response = client.getAcsResponse(getStatusRequest);
            if (log.isDebugEnabled()) {
                log.debug("GetStatusResponse: {}", AppUtils.encodeJson2(response));
            }

            if (response != null && response.getSuccess()) {
                statusCode = response.getData().getStatusCode();
                if (statusCode == 2) {
                    msg = response.getData().getAuditConclusions();
                }

            } else {
                log.debug("GetStatusResponse error: {}", AppUtils.encodeJson2(response));
            }

        } catch (ServerException e) {
            log.error("GetStatusRequest ServerException", e);
        } catch (ClientException e) {
            log.error("GetStatusRequest ClientException", e);
        }

//7. 服务端获取认证资料
//GetMaterials接口文档：https://help.aliyun.com/document_detail/57641.html
        GetMaterialsRequest getMaterialsRequest = new GetMaterialsRequest();
        getMaterialsRequest.setBiz(biz);
        getMaterialsRequest.setTicketId(ticketId);
        if(1 == statusCode || 2 == statusCode ) { //认证通过or认证不通过
            if (log.isDebugEnabled()) {
                log.debug("GetMaterialsRequest: {}", AppUtils.encodeJson2(getMaterialsRequest));
            }

            try {
                GetMaterialsResponse response = client.getAcsResponse(getMaterialsRequest);
                if (log.isDebugEnabled()) {
                    log.debug("GetMaterialsResponse: {}", AppUtils.encodeJson2(response));
                }

                if (response != null && response.getSuccess()) {
                    facePic = response.getData().getFacePic();
                    idCard = response.getData().getIdentificationNumber();
                    fullname = response.getData().getName();

                } else {
                    log.debug("GetMaterialsResponse error: {}", AppUtils.encodeJson2(response));
                }

                //后续业务处理
            } catch (ServerException e) {
                log.error("GetMaterialsResponse ServerException", e);
            } catch (ClientException e) {
                log.error("GetMaterialsResponse ClientException", e);
            }
        }

        if (statusCode == 1) { //认证通过
            customerService.updateCertification(customerId, fullname, idCard, Customer.AuthStatus.AUDIT_PASS.getValue());

            try {
                if (StringUtils.isNotEmpty(facePic)) { //图片有
                    File tempFile = new File(appConfig.tempDir, IdUtils.uuid() + ".jpg");
                    DownloadFileUtils.download(facePic, tempFile);
                    if (tempFile.exists()) {
                        Map<String, File> fileMap = new HashMap<String, File>();
                        fileMap.put(tempFile.getName(), tempFile);

                        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(appConfig.staticUrl + "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_AUTH_FACE.getValue(), fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
                        if (httpResp.status / 100 == 2) {
                            List<Map> list = (List<Map>) ((Map) AppUtils.decodeJson2(httpResp.content, Map.class)).get("data");
                            String authFacePath = (String) list.get(0).get("filePath");
                            customerService.updateAuthFacePath(customerId, authFacePath);

                        } else {
                            log.error("上传附件错误1, {}", httpResp.toString());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("上传附件错误2", e);
            }

        }

        String url = appConfig.getDomainUrl() + "/v_vee/myPage/faceRecognition?status=%d&msg=%s";
        //-1 未认证, 0 认证中, 1 认证通过, 2 认证不通过
        if (statusCode == -1) {
            url = String.format(url, 1, AppUtils.encodeUrl("未认证", Constant.ENCODING_UTF_8));

        } else if (statusCode == 0) {
            url = String.format(url, 2, AppUtils.encodeUrl("认证中", Constant.ENCODING_UTF_8));

        } else if (statusCode == 1) {
            url = String.format(url, 3, "");

        } else if (statusCode == 2) {
            if (StringUtils.isEmpty(msg)) {
                msg = "认证失败";
            }
            customerService.updateCertification(customerId, null, null, Customer.AuthStatus.AUTO_FAIL.getValue());

            url = String.format(url, 4, AppUtils.encodeUrl(msg, Constant.ENCODING_UTF_8));
        }

        httpResponse.sendRedirect(url);



//常见问题：https://help.aliyun.com/document_detail/57640.html
    }
}
