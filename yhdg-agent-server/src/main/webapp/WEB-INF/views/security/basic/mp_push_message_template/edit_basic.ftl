<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="fromAppId" value="${(entity.appId)!''}">
            <table cellpadding="0" cellspacing="0">
                <#--<tr>-->
                    <#--<td width="70" align="right">平台：</td>-->
                    <#--<td><select name="toAppId" id="app_id_${pid}" style="height: 28px;width: 185px;" class="easyui-combobox" editable="false">-->
                    <#--<#list agentList as s>-->
                        <#--<option value="${s.getId()}" <#if entity.appId?? && entity.appId == s.getId()>selected</#if> >${s.getAgentName()}</option>-->
                    <#--</#list>-->
                    <#--</select></td>-->
                <#--</tr>-->
                <tr>
                    <td width="70" align="right">模板名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true"name="templateName" value="${(entity.templateName)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">公众号：</td>
                    <td><input type="text" class="text easyui-validatebox"   name="mpCode" value="${(entity.mpCode)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <#--<tr>-->
                    <#--<td width="70" align="right">生活号：</td>-->
                    <#--<td><input type="text" class="text easyui-validatebox"   name="fwCode" value="${(entity.fwCode)!''}" style="width: 172px; height: 28px;"/></td>-->
                <#--</tr>-->
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">变量：</td>
                    <td><textarea style="width:330px;height: 100px;" name="variable" maxlength="512">${(entity.variable)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;" name="memo" maxlength="512">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>  value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"  <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var snapshot = $.toJSON({
            id: '${(entity.id)!''}',
            <#--appId: '${(entity.appId)!''}',-->
            templateName: "${(entity.templateName)!''}",
            variable: '${(entity.variable?js_string)!''}',
            mpCode: '${(entity.mpCode)!''}',
            isActive: '${(entity.isActive)!''}',
            memo:'${(entity.memo)!''}'
        });
        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            var values = {
                id: '${(entity.id)!''}',
                <#--appId: $('#app_id_${pid}').combobox('getValue'),-->
                templateName: form.templateName.value,
                variable: form.variable.value,
                mpCode: form.mpCode.value,
                isActive: $('#is_active_1').attr('checked') ? 1 : 0,
                memo: form.memo.value
            };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/mp_push_message_template/update_basic.htm',
                    dataType: 'json',
                    data: {
                        id: '${(entity.id)!''}',
                        <#--fromAppId: '${(entity.appId)!''}',-->
                        <#--toAppId: $('#app_id_${pid}').combobox('getValue'),-->
                        appId: '${(entity.appId)!''}',
                        templateName: form.templateName.value,
                        variable: form.variable.value,
                        mpCode: form.mpCode.value,
                        isActive: $('#is_active_1').attr('checked') ? 1 : 0,
                        memo: form.memo.value
                    },
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }

            return success;
        };

        win.data('ok', ok);
    })();
</script>
