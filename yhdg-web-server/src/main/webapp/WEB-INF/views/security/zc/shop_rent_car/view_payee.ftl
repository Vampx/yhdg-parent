<div class="tab_item" style="display:block;height: 460px">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="120" align="right">收款人姓名 ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleName" readonly value="${(entity.payPeopleName)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">微信openId ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleMpOpenId" readonly value="${(entity.payPeopleMpOpenId)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">支付宝账号 ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleFwOpenId" readonly value="${(entity.payPeopleFwOpenId)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">手机号 ：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" name="payPeopleMobile" readonly value="${(entity.payPeopleMobile)!}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">提现密码 ：</td>
                    <td>
                        <input type="password" class="text easyui-validatebox" readonly name="payPassword1"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="90px;">提现密码确认 ：</td>
                    <td>
                        <input type="password" class="text easyui-validatebox" readonly name="payPassword2"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            var success = true;
            return success;
        };

        win.data('ok', ok);
    })();


</script>
