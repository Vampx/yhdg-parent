<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="cabinetId" value="${entity.cabinetId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td align="right">
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;" readonly="readonly"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200'"
                               value="${(entity.agentId)!''}"/>
                    </td>
                    <td align="right">押金金额：</td>
                    <td>
                        <input type="text" class="easyui-numberspinner" data-options="min:0" style="width: 184px; height: 28px;" name="foregiftMoney" value="${(entity.foregiftMoney) !'0'}" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">租金金额：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" name="rentMoney" maxlength="20"  data-options="min:0"
                               value="${(entity.rentMoney) !'0'}"/></td>
                    <td width="70" align="right">租金周期(月)：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="rentPeriodType" value="${(entity.rentPeriodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>

                <tr>
                    <td width="70" align="right">租金截至：</td>
                    <td><input type="text" class="easyui-datebox" style="width: 184px; height: 28px;" value="<#if entity.rentExpireTime?? >${app.format_date_time(entity.rentExpireTime)}</#if>" name="rentExpireTime"/></td>
                    <td align="right">审核状态：</td>
                    <td>
                        <input type="radio" name="status" value="2" checked>通过&nbsp;&nbsp;
                        <input type="radio" name="status" value="3">不通过
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

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/cabinet_install_record/update_up_line.htm',
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
