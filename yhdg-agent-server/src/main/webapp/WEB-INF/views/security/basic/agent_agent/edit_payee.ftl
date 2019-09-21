<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="120" align="right">收款人姓名 ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleName" value="${(entity.payPeopleName)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">微信openId ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleMpOpenId" value="${(entity.payPeopleMpOpenId)!}"/>
                        <a href="javascript:selectPayeeCustomer()" style="font-weight: bold;padding-left: 3px;">+选择</a>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">支付宝账号 ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleFwOpenId" value="${(entity.payPeopleFwOpenId)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">手机号 ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleMobile" value="${(entity.payPeopleMobile)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">提现密码 ：</td>
                    <td>
                        <input type="password" class="text easyui-validatebox" name="payPassword1"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">提现密码确认 ：</td>
                    <td>
                        <input type="password" class="text easyui-validatebox" name="payPassword2" onblur="checkPassword()"/>
                    </td>
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
                id:form.id.value,
                payPeopleName: form.payPeopleName.value,
                payPeopleMpOpenId: form.payPeopleMpOpenId.value,
                payPeopleFwOpenId: form.payPeopleFwOpenId.value,
                payPeopleMobile: form.payPeopleMobile.value,
                payPassword: form.payPassword2.value
            };

            if(!checkPassword()){
                success = false;
            }else {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/agent/update_pay_people.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                        <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    },
                    error: function (text) {
                        $.messager.alert('提示信息', test, 'info');
                        success = false;
                    }
                });
            }
            return success;
        };

        win.data('ok', ok);
    })();

    function selectPayeeCustomer() {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择收款人',
            href: "${contextPath}/security/basic/customer/select_payee_customer.htm",
            windowData: {
                ok: function(config) {
                    $('#${pid}').find('input[name=payPeopleName]').val(config.customer.fullname);
                    $('#${pid}').find('input[name=payPeopleMobile]').val(config.customer.mobile);
                    $('#${pid}').find('input[name=payPeopleMpOpenId]').val(config.customer.mpOpenId);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }
    function checkPassword() {
        if($('[name=payPassword2]').val()!=''&&$('[name=payPassword2]').val()!=$('[name=payPassword1]').val()){
            $.messager.alert('提示信息', '输入密码有误', 'error');
            return false;
        }
        return true;
    }

</script>
