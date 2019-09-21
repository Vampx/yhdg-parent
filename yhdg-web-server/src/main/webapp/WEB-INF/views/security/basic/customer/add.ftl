<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="photoPath" id="photo_path_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" name="fullname" required="required"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg" /><span>上传头像</span></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">昵称：</td>
                    <td><input type="text" name="nickname" maxlength="40" class="text easyui-validatebox" /></td>
                </tr>
                <tr>
                    <td align="right">手机号：</td>
                    <td><input type="text" name="mobile" maxlength="11" id="mobile_${pid}" class="text easyui-validatebox" required="true" validType="mobile[]" /></td>
                    <td align="right">卡号：</td>
                    <td><input type="text" class="text" maxlength="40" name="icCard"/></td>
                </tr>
                <tr>
                    <td align="right">身份证卡号：</td>
                    <td><input type="text" class="text" maxlength="40" name="idCard"/></td>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="isActive_1" checked value="1"/><label for="isActive_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="isActive_0" value="0"/><label for="isActive_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">密码：</td>
                    <td><input type="password" class="text" name="password" id="password_${pid}" required="true" /></td>
                    <td align="right">确认密码：</td>
                    <td><input type="password" class="text" name="password2" required="true" validType="equals['#password_${pid}']" /></td>
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
    function set_portrait (param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#photo_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    function swich_agent_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var priceGroupCombobox = $('#price_group_id_${pid}');
        priceGroupCombobox.combobox({
            url: "${contextPath}/security/hdg/price_group/find_by_agent.htm?agentId=" + agentId + ""
        });
        priceGroupCombobox.combobox('reload');
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/customer/create.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
        win.find('.portrait').click(function() {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传头像',
                href: "${contextPath}/security/basic/customer/photo_path.htm",
                event: {
                    onClose: function() {
                    }
                }
            });
        });

    })()
</script>