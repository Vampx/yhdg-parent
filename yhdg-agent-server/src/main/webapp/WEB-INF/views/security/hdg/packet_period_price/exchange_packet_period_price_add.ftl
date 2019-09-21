<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <input type="hidden" name="foregiftId" value="${(foregiftId)!''}">
                <input type="hidden" name="batteryType" value="${(batteryType)!''}">
                <input type="hidden" name="agentId" value="${(agentId)!''}">
                <tr>
                    <td align="right">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                               <#if (agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td><input type="text" readonly maxlength="20" class="text easyui-validatebox" required="true" name="typeName" value="${(typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">价格：</td>
                    <td><input class="easyui-numberspinner" required="true" id="price_${pid}" maxlength="5" style="width:184px;height: 28px;" data-options="min:0.01,precision:2"></td>
                </tr>
                <tr>
                    <td align="right">天数：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" id="day_count_${pid}" name="dayCount" maxlength="3"
                               data-options="min:0, max:365" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">限制次数：</td>
                    <td><input type="text"  class="easyui-numberbox" name="limitCount" maxlength="4"
                               data-options="min:0" style="width: 185px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">每日限制次数：</td>
                    <td><input type="text"  class="easyui-numberbox" name="dayLimitCount" maxlength="4"
                               data-options="min:0" style="width: 185px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" maxlength="200"></textarea></td>
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
            var dayCount = $('#day_count_${pid}').numberspinner('getValue');
            if (dayCount == '') {
                $.messager.alert('提示信息', "请填写天数", 'info');
                return false;
            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price_create.htm',
                onSubmit: function(param) {
                    param.price = parseInt(Math.round(price * 100));
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