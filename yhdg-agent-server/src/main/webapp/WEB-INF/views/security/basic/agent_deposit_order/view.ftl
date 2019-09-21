<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">

            <table cellpadding="0" cellspacing="0">
<#--                <tr>-->
<#--                    <td align="right">商户：</td>-->
<#--                    <td>-->
<#--                        <input name="partnerId" id="partner_id" style="width: 182px; height: 30px;" class="easyui-combobox"  editable="false" readonly="readonly"-->
<#--                               data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',-->
<#--                                        method:'get',-->
<#--                                        valueField:'id',-->
<#--                                        textField:'partnerName',-->
<#--                                        editable:false,-->
<#--                                        multiple:false,-->
<#--                                        value:'${(entity.partnerId)!}'"-->
<#--                        />-->
<#--                    </td>-->
<#--                </tr>-->
                <tr>
                    <td width="100" align="right">运营商名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="agentName" readonly="readonly"
                               value="${(entity.agentName)!''}"/></td>
                    <td width="200" align="right">运营商编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="agentCode" readonly="readonly"
                               value="${(entity.agentCode)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">充值金额：</td>
                    <td><input type="text" class="text easyui-validatebox" name="money" readonly="readonly"
                               value="${((entity.money)!0)/100}"/></td>
                    <td width="200" align="right">状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly" id="orderStatus_${pid}" name="status">
                            <#list StatusEnum as s>
                                <option value="${s.getValue()}" <#if entity.status?? && entity.status==s.getValue()>selected</#if> >${s.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">支付类型：</td>
                    <td><input type="text" class="text easyui-validatebox" name="payType" readonly="readonly"
                               value="${(entity.payTypeName)!''}"/></td>
                    <td width="200" align="right">支付时间：</td>
                    <td><input type="text" class="text easyui-validatebox" name="handleTime" readonly="readonly"
                               value="${(entity.handleTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" name="operator" readonly="readonly"
                               value="${(entity.operator)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
            win = $('#' + pid),
            form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/weixinmp/update.htm',
                onSubmit: function(param) {
                    var isValid = $(this).form('validate');
                    if (!isValid) {
                        return false;
                    }

                    return true;
                },
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

    })()
</script>
