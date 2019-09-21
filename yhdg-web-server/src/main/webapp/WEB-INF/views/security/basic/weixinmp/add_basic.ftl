<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" id="attentionImagePath" name="attentionImagePath" value="${(entity.attentionImagePath)!''}">
            <input type="hidden" id="logoImagePath" name="logoImagePath" value="${(entity.logoImagePath)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">商户：</td>
                    <td>
                        <input name="partnerId" id="partner_id" style="width: 182px; height: 30px;" class="easyui-combobox"  editable="false"
                               data-options="url:'${contextPath}/security/basic/partner/list.htm',
                                        method:'get',
                                        valueField:'id',
                                        textField:'partnerName',
                                        editable:false,
                                        multiple:false,
                                        value:'${(entity.partnerId)!}',
                                        onLoadSuccess:function(){
                                        var val = $(this).combobox('getData');
                                            for (var item in val[0]){
                                                if (item == 'id'){
                                                     $(this).combobox('select', val[0][item]);
                                                }
                                            }
                                        }
                                        "

                        />
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">公众号ID：</td>
                    <td><input type="text" class="text easyui-validatebox" name="appId"
                               value="${(entity.appId)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">微信号ID：</td>
                    <td><input type="text" class="text easyui-validatebox" name="weiXinId"
                               value="${(entity.weiXinId)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">公众号名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="appName"
                               value="${(entity.appName)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">公众号秘钥：</td>
                    <td><input type="text" class="text easyui-validatebox" name="appSecret"
                               value="${(entity.appSecret)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">公众号商户：</td>
                    <td><input type="text" class="text easyui-validatebox" name="partnerCode"
                               value="${(entity.partnerCode)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">公众号商户秘钥：</td>
                    <td><input type="text" class="text easyui-validatebox" name="partnerKey"
                               value="${(entity.partnerKey)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" name="systemTel"
                               value="${(entity.systemTel)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">关注URL：</td>
                    <td><input type="text" class="text easyui-validatebox" name="subscribeUrl" style="width: 400px"
                               value="${(entity.subscribeUrl)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">页面类型：</td>
                    <td>
                        <select name="pageType">
                            <option value="1" ${((entity.pageType==1)?string('selected',''))!}>默认</option>
                            <option value="2" ${((entity.pageType==2)?string('selected',''))!}>定制</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">认证方式：</td>
                    <td>
                        <select name="authType">
                            <option value="1" ${((entity.authType==1)?string('selected',''))!}>自动认证</option>
                            <option value="2" ${((entity.authType==2)?string('selected',''))!}>人工认证</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">关注图片：</td>
                    <td >
                        <div id="portrait1" class="attentionpath">
                            <a href="javascript:void(0)">
                                <img id="image1_${pid}" style="background-color: transparent;" src="${app.imagePath}/user.jpg" />
                                <span>上传图片</span>
                            </a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">logo图片：</td>
                    <td >
                        <div id="portrait2" class="logopath">
                            <a href="javascript:void(0)"><img id="image2_${pid}" src="${app.imagePath}/user.jpg" /><span>上传图片</span></a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var pid = '${pid}',
            win = $('#' + pid), jform = win.find('form');
        var form = jform[0];
        var ok = function () {
            var success = true;
            var values = {
                partnerId: form.partnerId.value,
                appId: form.appId.value,
                appName: form.appName.value,
                weiXinId:form.weiXinId.value,
                appSecret: form.appSecret.value,
                systemTel: form.systemTel.value,
                partnerCode: form.partnerCode.value,
                partnerKey: form.partnerKey.value,
                subscribeUrl: form.subscribeUrl.value,
                pageType: form.pageType.value,
                authType: form.authType.value,
                attentionImagePath: form.attentionImagePath.value,
                logoImagePath: form.logoImagePath.value
            };
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/weixinmp/create.htm',
                dataType: 'json',
                data: values,
                success: function (json) {
                <@app.json_jump/>
                    if (json.success) {
                        win.data('entityId', json.message);
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                        success = false;
                    }
                }
            });
            return success;
        }

        win.data('ok', ok);
        win.find('#portrait1').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传图片',
                href: "${contextPath}/security/basic/weixinmp/photo_path.htm?typs=1",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

        win.find('#portrait2').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传图片',
                href: "${contextPath}/security/basic/weixinmp/photo_path.htm?typs=2",
                event: {
                    onClose: function() {
                    }
                }
            });
        });
    })()
    function set_portrait (param) {
        if(param.typs=='1'){
            $('#image1_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
            $("#attentionImagePath").val(param.filePath);
        }else{
            $('#image2_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
            $('#logoImagePath').val(param.filePath);
        }
        $('#' + param.pid).window('close');
    }
</script>
