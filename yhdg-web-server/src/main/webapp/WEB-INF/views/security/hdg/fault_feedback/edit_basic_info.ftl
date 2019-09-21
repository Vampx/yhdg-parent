<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="agentId" value="${entity.agentId}">
            <input type="hidden" name="customerId" value="${entity.customerId}">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" name="customerName" readonly value="${(entity.customerName)!''}" readonly="readonly" required="true"/></td>
                    <td align="right">手机号码：</td>
                    <td><input type="text"  class="text easyui-validatebox" maxlength="11" name="customerMobile" readonly value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">故障名称：</td>
                    <td><input type="text"  class="text easyui-validatebox" name="faultName" readonly value="${(entity.faultName)!''}"/></td>
                    <td align="right">故障类型：</td>
                    <td>
                        <select class="easyui-combobox" readonly="readonly" id="type_${pid}" name="type" style="width:180px; height: 30px;">
                        <#list typeList as type>
                            <option value="${type.value!}" <#if (entity.faultType == type.value)> selected="selected" </#if>>${type.getName()!}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">处理状态：</td>
                    <td>
                        <select class="easyui-combobox"  id="handleStatus_id_${pid}" name="handleStatus" style="width:180px; height: 30px;">
                        <#list handleStatusList as handleStatus>
                            <option value="${handleStatus.value!}" <#if (entity.handleStatus == handleStatus.value)> selected="selected" </#if>>${handleStatus.getName()!}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">处理时间：</td>
                    <td>
                        <input type="text" id="handle_time_${pid}" editable="false" class="text easyui-datetimebox" name="handleTime" required="true"  value="${(entity.handleTime?string('yyyy-MM-dd HH:mm:ss'))!''}" style="width: 184px; height: 28px;"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">故障内容：</td>
                    <td colspan="3"><textarea style="width:430px;" name="content" readonly>${(entity.content)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">处理结果：</td>
                    <td colspan="3"><textarea style="width:430px;" name="handleResult">${(entity.handleResult)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>

    (function() {
        $("#handle_time_${pid}").datetimebox().datetimebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        var pid = '${pid}',
                win = $('#${pid}'),
                jform = win.find('form'),
                form = jform[0];

        var snapshot = $.toJSON({
            id: '${entity.id}',
            customerId: '${(entity.customerId)!''}',
            customerName: '${(entity.customerName)!''}',
            customerMobile: '${(entity.customerMobile)!''}',
            handleStatus: '${(entity.handleStatus)!''}',
            type: '${(entity.type)!''}',
            content: '${(entity.content?js_string)!''}'
        });

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;
            var values = {
                id: '${entity.id}',
                customerId: form.customerId.value,
                customerName: form.customerName.value,
                customerMobile: form.customerMobile.value,
                handleStatus: $('#handleStatus_id_${pid}').combobox('getValue'),
                type: $('#type_${pid}').combobox('getValue'),
                handleTime: form.handleTime.value,
                handleResult: form.handleResult.value,
                content: form.content.value
            };
            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/fault_feedback/update_basic_info.htm',
                    dataType: 'json',
                    data: values,
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
