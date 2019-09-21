<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!0}">
            <input type="hidden" name="agentId" value="${(entity.agentId)!''}">
            <input type="hidden" name="batteryType" value="${(entity.batteryType)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" readonly maxlength="20" class="text easyui-validatebox" required="true" name="agentName" value="${(agentName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td><input type="text" readonly maxlength="20" class="text easyui-validatebox" required="true" name="typeName" value="${(typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">保险名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="insuranceName" value="${(entity.insuranceName)!''}" required="true" maxlength="40" /></td>
                </tr>
                <tr>
                    <td align="right">价格：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" maxlength="5" id="price_${pid}"
                               data-options="min:0, precision:2" style="width: 184px; height: 28px;" value="${((entity.price)/100)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">保额：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" maxlength="5" id="paid_${pid}"
                               data-options="min:0, precision:2" style="width: 184px; height: 28px;" value="${((entity.paid)/100)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">月数：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" id="month_count_${pid}" name="monthCount" maxlength="3"
                               data-options="min:0, max:365" style="width: 184px; height: 28px;" value="${(entity.monthCount)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">是否有效：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>  value="1"/><label for="is_active_1">有效</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else >checked</#if>  value="0"/><label for="is_active_0">无效</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            var price = $('#price_${pid}').numberspinner('getValue');
            if (price == '') {
                $.messager.alert('提示信息', "请填写价格", 'info');
                return false;
            }
            var paid = $('#paid_${pid}').numberspinner('getValue');
            if (paid == '') {
                $.messager.alert('提示信息', "请填写保额", 'info');
                return false;
            }
            var monthCount = $('#month_count_${pid}').numberspinner('getValue');
            if (monthCount == '') {
                $.messager.alert('提示信息', "请填写月数", 'info');
                return false;
            }
            var lastPrice = parseInt(Math.round(${((entity.price)!0)/100} * 100));
            var nextPrice = parseInt(Math.round(price * 100));
            if(lastPrice == nextPrice) {
                form.form('submit', {
                    url: '${contextPath}/security/hdg/insurance/exchange_insurance_update.htm',
                    onSubmit: function(param) {
                        param.price = parseInt(Math.round(price * 100));
                        param.paid = parseInt(Math.round(paid * 100));
                        return true;
                    },
                    success: function(text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if(json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                            $.post('${contextPath}/security/hdg/insurance/exchange_insurance.htm', {
                                batteryType: ${(entity.batteryType)!''},
                                agentId: ${(entity.agentId)!''}
                            }, function (html) {
                                $("#exchange_insurance").html(html);
                            }, 'html');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }else{
                $.messager.confirm('提示信息', '修改保险价格会删除对应的分期设置，确认修改?', function(ok) {
                    if(ok) {
                        form.form('submit', {
                            url: '${contextPath}/security/hdg/insurance/exchange_insurance_update.htm',
                            onSubmit: function(param) {
                                param.price = parseInt(Math.round(price * 100));
                                param.paid = parseInt(Math.round(paid * 100));
                                return true;
                            },
                            success: function(text) {
                                var json = $.evalJSON(text);
                            <@app.json_jump/>
                                if(json.success) {
                                    $.messager.alert('提示信息', '操作成功', 'info');
                                    win.window('close');
                                    $.post('${contextPath}/security/hdg/insurance/exchange_insurance.htm', {
                                        batteryType: ${(entity.batteryType)!''},
                                        agentId: ${(entity.agentId)!''}
                                    }, function (html) {
                                        $("#exchange_insurance").html(html);
                                    }, 'html');
                                } else {
                                    $.messager.alert('提示信息', json.message, 'info');
                                }
                            }
                        });
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>