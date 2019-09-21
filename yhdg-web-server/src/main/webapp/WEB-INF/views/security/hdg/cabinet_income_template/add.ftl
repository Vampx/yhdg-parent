<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected">基本信息</li>
    </ul>
    <div class="tab_con">
        <div class="tab_item" style="display:block;">
            <div class="ui_table">
                <form method="post">
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
                                "
                                >
                            </td>
                            <td align="right">押金金额（元）：</td>
                            <td>
                                <input type="text" class="easyui-numberspinner" data-options="min:0.00, precision:2" style="width: 184px; height: 28px;" id="foregift_money_${pid}" value="0" maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">租金金额（元）：</td>
                            <td><input type="text" class="easyui-numberspinner" data-options="min:0.00, precision:2" style="width: 184px; height: 28px;" id="rent_money_${pid}" maxlength="20"
                                       value="0"/></td>
                            <td width="70" align="right">租金周期(月)：</td>
                            <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="periodType" value="${(entity.periodType)!''}"
                                       style="width: 184px; height: 28px;"/></td>
                        </tr>

                        <tr>
                            <td width="70" align="right">租金截至时间：</td>
                            <td><input type="text" class="easyui-datebox" style="width: 184px; height: 28px;" name="rentExpireTime"/></td>
                            <td align="right">是否免审：</td>
                            <td>
                                <select name="isReview" class="easyui-combobox" editable="false"
                                        style="width: 184px; height: 28px;">
                                <#list isReviewEnum as e>
                                    <option value="${e.getValue()}">${e.getName()}</option>
                                </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script>
    (function () {
        var win = $('#${pid}'), form = win.find('form');

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/hdg/cabinet_income_template/create.htm',
                onSubmit: function (param) {
                    var foregiftMoney = $('#foregift_money_${pid}').numberspinner('getValue');
                    param.foregiftMoney = parseInt(Math.round(foregiftMoney * 100));
                    var rentMoney = $('#rent_money_${pid}').numberspinner('getValue');
                    param.rentMoney = parseInt(Math.round(rentMoney * 100));
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
        win.find('button.close').click(function () {
            win.window('close');
        });
    })();
</script>


