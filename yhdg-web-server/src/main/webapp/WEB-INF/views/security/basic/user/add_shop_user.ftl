<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="photoPath" id="photo_path_${pid}">
            <input type="hidden" name="portrait" id="portrait_${pid}">
            <input type="hidden" name="accountType" value="${(accountType)!''}">
            <input type="hidden" name="agentId" value="${(agentId)!''}">
            <input type="hidden" name="shopId" value="${(shopId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">登录名称：</td>
                    <td><input type="text" name="loginName" maxlength="40" class="text easyui-validatebox"
                               required="true" validType="unique[]"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg"/><span>修改头像</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" maxlength="10" name="fullname"/></td>
                </tr>
                <tr>
                    <td align="right">密码：</td>
                    <td><input type="password" maxlength="40" class="text" name="password"
                               id="password_${pid}" required="true"/></td>
                    <td align="right">确认密码：</td>
                    <td><input type="password" maxlength="40" class="text" name="password2"
                               required="true" validType="equals['#password_${pid}']"/></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1" checked
                                       value="1"/><label for="is_active_1">启用</label>
                            </span>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0" value="0"/><label
                                    for="is_active_0">禁用</label>
                            </span>
                    </td>
                    <td align="right">手机号码：</td>
                    <td><input type="text" class="text" name="mobile" maxlength="11" validType="mobile"/></td>
                </tr>
                <tr>
                    <td align="right">备注：</td>
                    <td><input type="text" class="text" maxlength="120" name="memo"/></td>
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
            $.messager.confirm('提示信息', '确定新建?', function (ok) {
                if (ok) {
                    form.form('submit', {
                        url: '${contextPath}/security/basic/user/create_shop_user.htm',
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
                }
            })
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
        win.find('.portrait').click(function () {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/user/photo_path.htm",
                event: {
                    onClose: function () {
                    }
                }
            });
        });

    })()
</script>