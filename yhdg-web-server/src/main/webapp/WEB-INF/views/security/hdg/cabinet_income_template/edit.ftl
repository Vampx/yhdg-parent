<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td align="right">
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="true" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:true,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_agent();
                                    }" value="${(entity.agentId)!''}"/>
                    </td>
                    <td align="right">押金金额（元）：</td>
                    <td>
                        <input type="text" class="easyui-numberspinner" id="foregift_money_${pid}" data-options="min:0.00, precision:2" style="width: 184px; height: 28px;" value="${(entity.foregiftMoney/100) !'0'}" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">租金金额（元）：</td>
                    <td><input type="text" class="easyui-numberspinner" id="rent_money_${pid}" data-options="min:0.00, precision:2" style="width: 184px; height: 28px;" maxlength="10"
                               value="${(entity.rentMoney/100) !'0'}"/></td>
                    <td width="70" align="right">租金周期（月）：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="periodType" value="${(entity.periodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>

                <tr>
                    <td width="70" align="right">租金截至时间：</td>
                    <td><input type="text" class="easyui-datebox" style="width: 184px; height: 28px;" value="<#if entity.rentExpireTime?? >${app.format_date_time(entity.rentExpireTime)}</#if>" name="rentExpireTime"/></td>
                    <td align="right">是否免审：</td>
                    <td>
                        <select name="isReview" class="easyui-combobox" editable="false"
                                style="width: 184px; height: 28px;">
                        <#list isReviewEnum as e>
                            <option value="${e.getValue()}" <#if entity.isReview?? && entity.isReview == e.getValue()>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
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

    function swich_agent() {
        var agentId = $('#agentId').combotree('getValue');
        var modelId = $('#model_id');
        modelId.combotree({
            url: "${contextPath}/security/hdg/vehicle_model/tree.htm?agentId=" + agentId + ""
        });
        modelId.combotree('reload');
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/cabinet_income_template/update.htm',
                onSubmit: function (param) {
                    var foregiftMoney = $('#foregift_money_${pid}').numberspinner('getValue');
                    param.foregiftMoney = parseInt(Math.round(foregiftMoney * 100));
                    var rentMoney = $('#rent_money_${pid}').numberspinner('getValue');
                    param.rentMoney = parseInt(Math.round(rentMoney * 100));
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
    })();
</script>
