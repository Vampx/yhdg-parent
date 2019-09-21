import cn.com.yusong.yhdg.common.utils.AppUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cloudauth.model.v20180916.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RPH5BioOnlyTest {
    public static void main_2(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",             //默认
                "LTAIKVjonjAmq60b",         //您的Access Key ID
                "zHfMILqrDGqiaLKa2rbnADetVGEsaK");    //您的Access Key Secret
        IAcsClient client = new DefaultAcsClient(profile);
        String biz = "h5-customer-register"; //您在控制台上创建的、采用RPH5BioOnly认证方案的认证场景标识, 创建方法：https://help.aliyun.com/document_detail/59975.html
        String ticketId = UUID.randomUUID().toString(); //认证ID, 由使用方指定, 发起不同的认证任务需要更换不同的认证ID
        System.out.println(ticketId);
        String token = null; //认证token, 表达一次认证会话
        int statusCode = -1; //-1 未认证, 0 认证中, 1 认证通过, 2 认证不通过
//1. 服务端发起认证请求, 获取到token
//GetVerifyToken接口文档：https://help.aliyun.com/document_detail/57050.html
        GetVerifyTokenRequest getVerifyTokenRequest = new GetVerifyTokenRequest();
        getVerifyTokenRequest.setBiz(biz);
        getVerifyTokenRequest.setTicketId(ticketId);
        getVerifyTokenRequest.setMethod(MethodType.POST);
//通过binding参数传入业务已经采集的认证资料，其中姓名、身份证号为必要字段
//若需要binding图片资料，请控制单张图片大小在 2M 内，避免拉取超时
        getVerifyTokenRequest.setBinding("{\"Name\": \"xxx\",\"IdentificationNumber\": \"xxx\"}");
        try {
            GetVerifyTokenResponse response = client.getAcsResponse(getVerifyTokenRequest);
            token = response.getData().getVerifyToken().getToken(); //token默认30分钟时效，每次发起认证时都必须实时获取
            System.out.println("GetVerifyTokenResponse: " + AppUtils.encodeJson2(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",             //默认
                "LTAIKVjonjAmq60b",         //您的Access Key ID
                "zHfMILqrDGqiaLKa2rbnADetVGEsaK");    //您的Access Key Secret
        IAcsClient client = new DefaultAcsClient(profile);
        String biz = "h5-customer-register"; //您在控制台上创建的、采用RPH5BioOnly认证方案的认证场景标识, 创建方法：https://help.aliyun.com/document_detail/59975.html
//        String ticketId = UUID.randomUUID().toString(); //认证ID, 由使用方指定, 发起不同的认证任务需要更换不同的认证ID
//        System.out.println(ticketId);
//        String token = null; //认证token, 表达一次认证会话
//        int statusCode = -1; //-1 未认证, 0 认证中, 1 认证通过, 2 认证不通过
////1. 服务端发起认证请求, 获取到token
////GetVerifyToken接口文档：https://help.aliyun.com/document_detail/57050.html
//        GetVerifyTokenRequest getVerifyTokenRequest = new GetVerifyTokenRequest();
//        getVerifyTokenRequest.setBiz(biz);
//        getVerifyTokenRequest.setTicketId(ticketId);
//        getVerifyTokenRequest.setMethod(MethodType.POST);
////通过binding参数传入业务已经采集的认证资料，其中姓名、身份证号为必要字段
////若需要binding图片资料，请控制单张图片大小在 2M 内，避免拉取超时
//        getVerifyTokenRequest.setBinding("{\"Name\": \"徐晓蕾\",\"IdentificationNumber\": \"410381198312056511\"}");
//        try {
//            GetVerifyTokenResponse response = client.getAcsResponse(getVerifyTokenRequest);
//            token = response.getData().getVerifyToken().getToken(); //token默认30分钟时效，每次发起认证时都必须实时获取
//            System.out.println(response.getData().getVerifyToken().getToken());
//            System.out.println(response.getData().getCloudauthPageUrl());
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }

        String ticketId = "0745ce51-9932-4161-bcc8-cc81eb38eb97";
        //https://cloudauth.alibaba.com/h5/h5verify.html?token=6f696da7f4c745caa7e6e7f58a79a77b&tokenSource=getVerifyToken

//2. 服务端将认证URL(带token)传递给H5前端
//3. H5前端跳转认证URL
//4. 用户按照认证H5流程页面的指引，提交认证资料
//5. 认证流程结束跳转指定的重定向URL(指定方法参看：https://help.aliyun.com/document_detail/58644.html?#H5Server)
//6. 服务端查询认证状态(建议以服务端调接口确认的为准)
//GetStatus接口文档：https://help.aliyun.com/document_detail/57049.html
        int statusCode = 0;
        GetStatusRequest getStatusRequest = new GetStatusRequest();
        getStatusRequest.setBiz(biz);
        getStatusRequest.setTicketId(ticketId);
        try {
            GetStatusResponse response = client.getAcsResponse(getStatusRequest);
            statusCode = response.getData().getStatusCode();
            System.out.println("GetStatusResponse: " + AppUtils.encodeJson2(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
//7. 服务端获取认证资料
//GetMaterials接口文档：https://help.aliyun.com/document_detail/57641.html
        GetMaterialsRequest getMaterialsRequest = new GetMaterialsRequest();
        getMaterialsRequest.setBiz(biz);
        getMaterialsRequest.setTicketId(ticketId);
        if( 1 == statusCode || 2 == statusCode ) { //认证通过or认证不通过
            try {
                GetMaterialsResponse response = client.getAcsResponse(getMaterialsRequest);
                System.out.println("GetMaterialsResponse: " + AppUtils.encodeJson2(response));
                //后续业务处理
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
//常见问题：https://help.aliyun.com/document_detail/57640.html


    }
}
