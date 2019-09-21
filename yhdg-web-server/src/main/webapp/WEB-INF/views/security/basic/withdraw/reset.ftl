<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                <td width="100" align="right">账户类型：</td>
                <td>
                    <#if entity.accountType==1>
                        公众号
                    <#elseif entity.accountType==2>
                        支付宝
                    <#elseif entity.accountType==3>
                        微信
                    </#if>
                </td>
                </tr>
                <tr>
                    <td width="100" align="right">账户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="accountName" value="${(entity.accountName)!''}"/></td>
                </tr>
                <tr>
                <td width="100" align="right">账户号：</td>
                <td>
                    <#if entity.accountType==1>
                        <input type="text" class="text easyui-validatebox" name="weixinAccount" value="${(entity.weixinAccount)!''}"/>
                    </#if>
                    <#if entity.accountType==2>
                        <input type="text" class="text easyui-validatebox" name="alipayAccount" value="${(entity.alipayAccount)!''}"/>
                    </#if>
                    <#if entity.accountType==3>
                        <input type="text" class="text easyui-validatebox" name="wxOpenId" value="${(entity.wxOpenId)!''}"/>
                    </#if>
                </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/withdraw/update_reset.htm',
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
