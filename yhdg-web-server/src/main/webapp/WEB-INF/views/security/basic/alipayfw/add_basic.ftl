<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" id="attentionImagePath" name="attentionImagePath" value="${(entity.attentionImagePath)!''}">
            <input type="hidden" id="logoImagePath" name="logoImagePath" value="${(entity.logoImagePath)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">商户：</td>
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
                    <td align="right">生活号名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="appName" value="${(entity.appName)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">appId：</td>
                    <td><input type="text" class="text easyui-validatebox" name="appId" value="${(entity.appId)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">生活号Id：</td>
                    <td><input type="text" class="text easyui-validatebox" name="alipayId" value="${(entity.alipayId)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">生活号公钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="fwPubKey"
                                  style="width: 560px;height: 80px;">${(entity.pubKey)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">生活号私钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="fwPriKey"
                                  style="width: 560px;height: 80px;">${(entity.priKey)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">支付宝公钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="aliKey"
                                  style="width: 560px;height: 80px;">${(entity.aliKey)!}</textarea></td>
                </tr>
                <tr>
                    <td width="100" align="right">联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" name="systemTel"
                               value="${(entity.systemTel)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">关注URL：</td>
                    <td><input type="text" class="text easyui-validatebox" name="subscribeUrl" style="width: 560px"
                               value="${(entity.subscribeUrl)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">用户版本：</td>
                    <td>
                    <select name="userinfoVersion">
                        <option value="1" ${((entity.userinfoVersion==1)?string('selected',''))!}>版本1.0</option>
                        <option value="2" ${((entity.userinfoVersion==2)?string('selected',''))!}>版本2.0</option>
                    </select>
                    </td>
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
                            <a href="javascript:void(0)"><img id="image1_${pid}" src="${app.imagePath}/user.jpg" /><span>上传图片</span></a>
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
                PubKey: form.fwPubKey.value,
                PriKey: form.fwPriKey.value,
                aliKey: form.aliKey.value,
                systemTel: form.systemTel.value,
                subscribeUrl: form.subscribeUrl.value,
                userinfoVersion: form.userinfoVersion.value,
                pageType: form.pageType.value,
                authType: form.authType.value,
                alipayId:form.alipayId.value,
                attentionImagePath: form.attentionImagePath.value,
                logoImagePath: form.logoImagePath.value
            };
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/alipayfw/create.htm',
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
                href: "${contextPath}/security/basic/alipayfw/photo_path.htm?typs=1",
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
                href: "${contextPath}/security/basic/alipayfw/photo_path.htm?typs=2",
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
