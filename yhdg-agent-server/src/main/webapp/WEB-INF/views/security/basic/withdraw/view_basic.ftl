<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">提现机构：</td>
                    <td>
                        <select class="easyui-combobox" style="height: 28px;" readonly="readonly">
                            <#list TypeEnum as t>
                                <option value="${t.getValue()}" <#if entity.type?? && entity.type==t.getValue()>selected</#if> >${t.getName()}</option>
                            </#list>
                        </select>
                    </td>
                    <td align="right">余额：</td>
                    <td><input type="text" class="text easyui-validatebox" name="balance"
                               value="${(entity.balance/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">所属商户：</td>
                    <td>
                        <input name="partnerId" id="partner_id" style="width: 182px; height: 30px;" class="easyui-combobox"  editable="false" readonly="readonly"
                               data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                        method:'get',
                                        valueField:'id',
                                        textField:'partnerName',
                                        editable:false,
                                        multiple:false,
                                        value:'${(entity.partnerId)!}'"
                        />
                    </td>
                    <td align="right">所属运营商：</td>
                    <td><input type="text" class="text easyui-validatebox" name="belongAgentName"
                               value="${(entity.belongAgentName)!''}"/></td>
                </tr>
                <tr style="display:${((entity.type!=1)?string('none',''))!'none'}">
                    <td align="right">客户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="customerFullname"
                               value="${(entity.customerFullname)!''}"/></td>
                    <td align="right">客户手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="customerMobile"
                               value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr style="display:${((entity.type!=2)?string('none',''))!'none'}">
                    <td align="right">运营商名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="agentName"
                               value="${(entity.agentName)!''}"/></td>
                    <td align="right">运营商编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="agentCode"
                               value="${(entity.agentCode)!''}"/></td>
                </tr>
                <tr style="display:${((entity.type!=3)?string('none',''))!'none'}">
                    <td align="right">门店ID：</td>
                    <td><input type="text" class="text easyui-validatebox" name="shopId"
                               value="${(entity.shopId)!''}"/></td>
                    <td align="right">门店名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="shopName"
                               value="${(entity.shopName)!''}"/></td>
                </tr>
                <tr style="display:${((entity.type!=4)?string('none',''))!'none'}">
                    <td align="right">平台账户ID：</td>
                    <td><input type="text" class="text easyui-validatebox" name="platformAccountId"
                               value="${(entity.platformAccountId)!''}"/></td>
                    <td align="right">平台账户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="platformAccountName"
                               value="${(entity.platformAccountName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">收款账户类型：</td>
                    <td><input type="text" class="text easyui-validatebox" name="accountTypeName"
                               value="${(entity.accountTypeName)!''}"/></td>
                    <td align="right">收款账户名：</td>
                    <td><input type="text" class="text easyui-validatebox" name="accountName"
                               value="${(entity.accountName)!''}"/></td>
                </tr>
                <tr style="display:${((entity.accountType!=1)?string('none',''))!'none'}">
                    <td align="right">收款账户号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="weixinAccount"
                               value="${(entity.weixinAccount)!''}"/></td>
                </tr>
                <tr style="display:${((entity.accountType!=2)?string('none',''))!'none'}">
                    <td align="right">收款账户号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="alipayAccount"
                               value="${(entity.alipayAccount)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">申请提现金额(元)：</td>
                    <td><input type="text" class="text easyui-validatebox" name="money"
                               value="${(entity.money/100)!}"/></td>
                    <td align="right">实际提现金额(元)：</td>
                    <td><input type="text" class="text easyui-validatebox" name="realMoney"
                               value="${(entity.realMoney/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">手续费(元)：</td>
                    <td><input type="text" class="text easyui-validatebox" name="serviceMoney"
                               value="${(entity.serviceMoney/100)!''}"/></td>
                    <td align="right">提现状态：</td>
                    <td><input type="text" class="text easyui-validatebox" name="status"
                               value="${(entity.statusName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">申请时间：</td>
                    <td><input type="text" class="text easyui-validatebox" name="createTime"
                               value="${(entity.createTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/></td>
                </tr>
                <tr>
                    <td align="right">审核时间：</td>
                    <td><input type="text" class="text easyui-validatebox" name="auditTime"
                               value="${(entity.auditTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/></td>
                    <td align="right">取消时间：</td>
                    <td><input type="text" class="text easyui-validatebox" name="cancelTime"
                               value="${(entity.cancelTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/></td>
                </tr>
                <tr>
                    <td align="right">处理人：</td>
                    <td><input type="text" class="text easyui-validatebox" name="auditUser"
                               value="${(entity.auditUser)!''}"/></td>
                    <td align="right">处理时间：</td>
                    <td><input type="text" class="text easyui-validatebox" name="handleTime"
                               value="${(entity.handleTime?string('yyyy-MM-dd HH:mm:ss'))!''}"/></td>
                </tr>
                <tr>
                    <td align="right">处理人意见：</td>
                    <td colspan="3"><textarea type="text" class="text easyui-validatebox" name="operatorMemo"
                                              style="width: 560px;height: 40px;">${(entity.operatorMemo)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">提示消息：</td>
                    <td colspan="3"><textarea type="text" class="text easyui-validatebox" name="statusMessage"
                                              style="width: 560px;height: 40px;">${(entity.statusMessage)!}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function () {
            var success = true;
            var values = {
                partnerName: form.partnerName.value
            };

            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/partner/create.htm',
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
        };

        win.data('ok', ok);
    })();
</script>

