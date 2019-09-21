<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">登录名称：</td>
                    <td><input type="text" name="loginName" readonly class="text easyui-validatebox" required="true" validType="unique[${entity.id}]" value="${(entity.loginName)!''}"/></td>
                    <td width="70" align="right" valign="top" rowspan="2" style="padding-top:10px;">头像：</td>
                    <td rowspan="2">
                        <div class="portrait">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.photoPath?? && entity.photoPath?length gt 0>${staticUrl}${entity.photoPath}<#else>${app.imagePath}/user.jpg</#if>" /></a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text" readonly maxlength="10" name="fullname" value="${(entity.fullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                    <td align="right">手机号码：</td>
                    <td><input type="text" class="text" name="mobile" readonly validType="mobile" value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">运营商：</td>
                    <td><input type="text" maxlength="40" readonly class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                    <td align="right">备注：</td>
                    <td><input type="text" class="text" readonly name="memo" value="${(entity.memo)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>