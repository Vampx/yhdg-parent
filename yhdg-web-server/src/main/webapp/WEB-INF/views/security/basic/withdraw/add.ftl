<div class="popup_body">
    <div class="ui_table" style="height: 170px;">
        <form method="post">
            <input type="hidden" name="partnerId" value="${(platformAccount.id)!}">
            <input type="hidden" name="mpOpenId" value="${(platformAccount.mpOpenId)!}">
            <table cellpadding="0" cellspacing="0" style="height: 140px;">
                <tr>
                    <td align="right">收款账户类型 ：</td>
                    <td>
                        <select class="easyui-validatebox" style="width: 120px;height: 25px" name="accountType" onchange="showMpOrAli($(this).val())">
                            <#list AccountTypeEnum as a>
                                <option value="${a.getValue()}"
                                    ${(a.getValue()==2 && ((!platformAccount.mpAccountName??) || platformAccount.mpAccountName==""))?string('selected','')}
                                >${a.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr id="mp" style="display: ${((!platformAccount.mpAccountName??) || platformAccount.mpAccountName=="")?string('none','')}">
                    <td align="right">收款客户 ：</td>
                    <td>
                        <input type="text" class="easyui-validatebox" style="width: 160px; height: 25px;" placeholder="请选择客户" name="mpAccountName" value="${(platformAccount.mpAccountName)!}"/>
                        <a href="javascript:selectCustomer()" style="font-weight: bold;padding-left: 3px;">+选择</a>
                    </td>
                </tr>
                <tr id="ali" style="display: ${((!platformAccount.mpAccountName??) || platformAccount.mpAccountName=="")?string('','none')}">
                    <td align="right">支付宝户名 ：</td>
                    <td>
                        <input type="text" class="easyui-validatebox" name="alipayAccountName" style="height: 25px;width: 150px;" placeholder="请输入支付宝账户名称" value="${(platformAccount.alipayAccountName)!}"/>
                    </td>
                    <td align="right" width="90px;">支付宝账号 ：</td>
                    <td>
                        <input type="text" class="easyui-validatebox" name="alipayAccount" style="height: 25px;width: 150px;" placeholder="请输入支付宝账号" value="${(platformAccount.alipayAccount)!}"/>
                    </td>
                </tr>
                <tr>
                    <td width="100" align="right">提现金额（元）：</td>
                    <td colspan="3">
                        <input type="text" class="easyui-numberbox" style="height: 30px;width: 120px;" data-options="min:0,precision:2" required="true" name="dMoney" id="dMoney"/>
                        <span style="margin-left: 5px;">余额：${platformAccount.balance/100}元</span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="padding-bottom: 100px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/withdraw/create.htm',
                onSubmit: function(param) {
                    if(!form.form('validate')) {
                        return false;
                    }
                    if($("#dMoney").val() > ${platformAccount.balance/100}){
                        $.messager.alert('提示信息', '最大提现金额为${platformAccount.balance/100}元', 'error');
                        return false;
                    }
                    return true;
                },
                success: function(text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if(json.success) {
                        win.window('close');
                        $.messager.alert('提示信息', '操作成功', 'info');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();

    function showMpOrAli(val) {
        if(val==1){
            $("#mp").show();
            $("#ali").hide();
        }else {
            $("#mp").hide();
            $("#ali").show();
        }
    }
    function selectCustomer() {
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择客户',
            href: "${contextPath}/security/basic/customer/select_customer.htm",
            windowData: {
                ok: function(config) {
                    $('#${pid}').find('input[name=mpAccountName]').val(config.customer.fullname);
                    $('#${pid}').find('input[name=mpOpenId]').val(config.customer.mpOpenId);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }
</script>