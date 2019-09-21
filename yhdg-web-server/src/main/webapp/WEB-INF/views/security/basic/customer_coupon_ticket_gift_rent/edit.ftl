<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="type" value="${entity.type}">
            <input type="hidden" name="agentId" value="${entity.agentId}">
            <input type="hidden" name="category" value="${entity.category}">

            <table cellpadding="0" cellspacing="0">
            <#if entity.agentId!=0>
                <tr>
                    <td width="80" align="right">赠送类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" name="newType" readonly="readonly"
                                style="width:180px;height: 30px ">
                            <#list TypeEnum as s>
                                <option value="${s.getValue()}"
                                        <#if entity.type?? && entity.type == s.getValue()>selected</#if>>${s.getName()}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
            <#else>
                <input type="hidden" name="newType" value="${entity.type}">
            </#if>
                <tr id="pay_count_${pid}">
                    <td width="70" align="right">购买天数：</td>
                    <td><input id="count_${pid}" type="text" maxlength="6" class="easyui-numberspinner" value="${(entity.payCount)!''}"
                               style="width: 180px; height: 28px;" name="payCount" data-options="min:0, precision:0"/>
                    </td>
                </tr>
                <tr id="wages_day_${pid}">
                    <td width="70" align="right">工资日：</td>
                    <td><input id="wages_${pid}" type="text" maxlength="2" class="easyui-numberspinner" value="${(entity.wagesDay)!''}"
                               style="width: 180px; height: 28px;" name="wagesDay" data-options="min:1,max:31, precision:0"/>&nbsp;号
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">有效天数：</td>
                    <td><input type="text" maxlength="6" class="easyui-numberspinner" value="${(entity.dayCount)!''}"
                               style="width: 180px; height: 28px;" name="dayCount" data-options="min:1, precision:0"/>
                    </td>
                </tr>

                <tr>
                    <td width="80" align="right">金额：</td>
                    <td><input id="money_${pid}" class="easyui-numberspinner" value="${((entity.money)/ 100 + "元")!''}"
                               style="width:180px;height: 30px " required="required"
                               data-options="min:0.01,precision:2">&nbsp;&nbsp;元
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">是否启用：</td>
                    <td>
                            <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_1"
                                       <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label
                                    for="is_active_1">启用</label>
                            </span>
                        <span class="radio_box">
                                <input type="radio" class="radio" name="isActive" id="is_active_0"
                                       <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if>
                                       value="0"/><label for="is_active_0">禁用</label>
                            </span>
                    </td>
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
    (function () {
        var win = $('#${pid}'),
                form = win.find('form');
        var type = win.find('input[name=type]').val();
        if(type == 3){
            $("#pay_count_${pid}").show();
        } else {
            $("#pay_count_${pid}").hide();
        }
        if(type == 4){
            $("#wages_day_${pid}").show();
        }else{
            $("#wages_day_${pid}").hide();
        }
        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/basic/customer_coupon_ticket_gift_rent/update.htm',
                onSubmit: function (param) {
                    var money = $('#money_${pid}').numberspinner('getValue');
                    param.money = parseInt(Math.round(money * 100));
                    return true;
                },
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });

        win.find('.close').click(function () {
            win.window('close');
        });
    })();
</script>