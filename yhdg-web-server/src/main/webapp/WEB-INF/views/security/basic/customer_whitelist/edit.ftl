<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">商户：</td>
                    <td>
                        <input name="partnerId" id="partner_id_${pid}" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false" required="true"
                               data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'partnerName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                   onLoadSuccess:function() {
                           $('#partner_id_${pid}').combobox('setValue', '${(entity.partnerId)!''}');
                       }"
                        />
                    </td>
                </tr>
                <tr>
                    <td align="right">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false" value="${(entity.agentId)!''}"
                               data-options="url:'${contextPath}/security/basic/agent/agent_list.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'agentName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                    }"
                        />
                    </td>
                </tr>
                <tr>
                    <td align="right">手机号码：</td>
                    <td><input type="text" id="mobile" class="text" name="mobile" maxlength="11" validType="mobile" required
                               value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">备注：</td>
                    <td colspan="3"><textarea style="width:260px;height:50px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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
                url: '${contextPath}/security/basic/customer_whitelist/update.htm',
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
