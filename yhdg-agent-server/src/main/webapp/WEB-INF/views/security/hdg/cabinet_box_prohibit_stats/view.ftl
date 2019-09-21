<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="cabinetId" value="${(entity.cabinetId)!''}">
            <input type="hidden" name="boxNum" value="${(entity.boxNum)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="agentName" value="${(entity.cabinet.agentName)!''}"></td>
                    <td width="70" align="right">换电柜编号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="cabinetId" value="${(entity.cabinetId)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">换电柜名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly name="cabinetName" value="${(entity.cabinet.cabinetName)!''}"></td>
                    <td width="70" align="right">地址：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="address" value="${(entity.cabinet.address)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">禁用格口：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="boxNum" value="${(entity.boxNum)!''}"></td>
                    <td width="70" align="right">禁用格口类型：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="type" value="${(entity.type)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">禁用人：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="operator" value="${(entity.operator)!''}"></td>
                    <td width="70" align="right">禁用时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly name="operatorTime" value="${(entity.operatorTime?string('yyyy-MM-dd HH:mm:ss'))!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">是否在线：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isOnline" readonly
                                   <#if entity.isActive?? && entity.isOnline == 1>checked</#if> id="is_online_1" disabled
                                   value="1"/><label for="is_active_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isOnline" readonly
                                   <#if entity.isActive?? && entity.isOnline == 0>checked</#if> id="is_online_0" disabled
                                   value="0"/><label for="is_active_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">禁用原因：</td>
                    <td colspan="3"><textarea style="width:260px;height:50px;" readonly name="forbiddenCause" maxlength="200">${(entity.forbiddenCause)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>
