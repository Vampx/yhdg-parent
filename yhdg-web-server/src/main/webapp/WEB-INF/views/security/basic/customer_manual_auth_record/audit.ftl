<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="customerId" value="${(entity.customerId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="120" align="right">姓名：</td>
                    <td><input type="text" maxlength="20" readonly="readonly" class="text easyui-validatebox"  name="fullname" value="${(entity.fullname)!''}"/></td>
                </tr>
                <tr>
                    <td  height="20"align="right">实名照片：</td>
                    <td>
                    <img width="90" height="90"
                          name="authFacePath"  src=<#if entity.authFacePath ?? && entity.authFacePath != ''>'${staticUrl}${(entity.authFacePath)!''}'
                         onclick="preview_${pid}('${(entity.authFacePath)!""}')"<#else>
                        '${app.imagePath}/user.jpg'</#if> />
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right">身份证号码：</td>
                    <td><input type="text" maxlength="20"  readonly="readonly"  class="text easyui-validatebox"  name="idCard" value="${(entity.idCard)!''}"/></td>
                </tr>
                <tr>
                    <td  height="20"align="right">身份证正面照片：</td>
                    <td>
                    <img width="150" height="90"
                         name="idCardFace"  src=<#if entity.idCardFace ?? && entity.idCardFace != ''>'${staticUrl}${(entity.idCardFace)!''}'
                         onclick="preview_${pid}('${(entity.idCardFace)!""}')"<#else>
                        '${app.imagePath}/user.jpg'</#if> />
                    </td>
                    <td  height="20"align="right">身份证反面照片：</td>
                    <td>
                    <img width="150" height="90"
                         name="idCardRear" src=<#if entity.idCardRear ?? && entity.idCardRear != ''>'${staticUrl}${(entity.idCardRear)!''}'
                         onclick="preview_${pid}('${(entity.idCardRear)!""}')"<#else>
                        '${app.imagePath}/user.jpg'</#if> />
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right">审核用户：</td>
                    <td><input type="text" maxlength="20" readonly="readonly" class="text easyui-validatebox"  name="auditUser" value="${(entity.auditUser)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">是否审核成功：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="status"  <#if entity.status?? && entity.status == 2>checked</#if> value="2"/><label for="status_2">审核通过</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="status" <#if entity.status?? && entity.status == 2><#else>checked</#if> value="3"/><label for="status_3">审核未通过</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea  style="width:400px;" maxlength="20" name="auditMemo">${(entity.auditMemo)!''}</textarea></td>
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
    (function () {
        var win = $('#${pid}');
        var list = $('#tab_container_${pid} li');
        list.click(function() {
            var me = $(this);
            if(me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }

            var ok = win.data('ok')();
            if(ok) {
                list.removeClass('selected');
                me.addClass('selected');
                $('#page_${pid}').panel('refresh', me.attr('url'));
            }
        });

        win.find('.ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });
        win.find('.close').click(function() {
            win.window('close');
        });
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];
        var ok = function () {
            var success = true;
            var values = {
                id: form.id.value,
                status: form.status.value,
                auditMemo: form.auditMemo.value
        };
            function check() {
                return true;
            }

            if(!check()){
                success = false;
            }else {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/customer_manual_auth_record/audit.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                            $.messager.alert('提示信息', "操作成功", 'info');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    },
                    error: function (text) {
                        $.messager.alert('提示信息', text, 'info');
                        success = false;
                    }
                });
            }
            return success;
        };

        win.data('ok', ok);
    })();
    function preview_${pid}(path) {
        App.dialog.show({
            options: 'maximized:true',
            title: '查看',
            href: "${controller.appConfig.staticUrl}/security/material/preview.htm?path=" + path
        });
    }
</script>
