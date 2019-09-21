<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="photoPath" id="photo_path_${pid}">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="balance" value="${(entity.balance)!''}">
            <input type="hidden" name="registerType" value="${(entity.registerType)!''}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" maxlength="40" name="fullname" value="${(entity.fullname)!''}"/>
                    </td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img
                                    id="image_${pid}" src=<#if entity.photoPath ?? && entity.photoPath != ''>'${staticUrl}${(entity.photoPath)!''}' <#else>
                                '${app.imagePath}/user.jpg'</#if> /><span>修改头像</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">昵称：</td>
                    <td><input type="text" name="nickname" maxlength="40" class="text easyui-validatebox"
                               value="${(entity.nickname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">手机号码：</td>
                    <td><input type="text" id="mobile" class="text" name="mobile" maxlength="11" validType="mobile" required
                               value="${(entity.mobile)!''}"/></td>
                    <td align="right">卡号：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" name="icCard"
                               value="${(entity.icCard)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">身份证卡号：</td>
                    <td><input type="text" class="text" maxlength="18" name="idCard" value="${(entity.idCard)!''}"/>
                    </td>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="isActive_1"
                                   <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label
                                for="isActive_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="isActive_0"
                                   <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label
                                for="isActive_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">密码：</td>
                    <td><input type="password" class="text" name="password" id="password_${pid}"/></td>
                    <td align="right">确认密码：</td>
                    <td><input type="password" class="text" name="password2" id="password2_${pid}"
                               validType="equals['#password_${pid}']"/></td>
                </tr>
                <tr>
                    <td align="right">所属站点：</td>
                    <td><input onclick="selectCabinet_${pid}()" value="${(entity.belongCabinetName)!''}" type="text" class="text" readonly
                               name="belongCabinetName"/><input
                            type="hidden" name="belongCabinetId" value="${(entity.belongCabinetId)!''}"/></td>
                    <td align="right">认证状态：</td>
                    <td>
                        <select id="auth_status_${pid}" name="authStatus" class="easyui-combobox" style="width: 182px; height: 28px;">
                        <#if authStatusEnum??>
                            <#list authStatusEnum as e>
                                <option <#if entity.authStatus?? && entity.authStatus == e.value>selected</#if>
                                        value="${(e.value)!''}">${(e.getName())!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function selectCabinet_${pid}() {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择换电柜',
            href: "${contextPath}/security/hdg/cabinet/select_cabinets.htm?agentId=${(entity.agentId)!''}",
            windowData: {
                ok: function (config) {
                    $("input[name='belongCabinetId']").val(config.cabinet.id);
                    $("input[name='belongCabinetName']").val(config.cabinet.cabinetName);
                }
            },
            event: {
                onClose: function () {
                }
            }
        });
    }

    function set_portrait(param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#photo_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            var password = $('#password_${pid}').val();
            var password2 = $('#password2_${pid}').val();
            if (password != "" && password2 == "") {
                $.messager.alert('提示信息', '请确认密码', 'info');
                return false;
            }
            form.form('submit', {
                url: '${contextPath}/security/basic/customer/update.htm',
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
        win.find('.portrait').click(function () {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/customer/photo_path.htm",
                event: {
                    onClose: function () {
                    }
                }
            });
        });
    })()
</script>