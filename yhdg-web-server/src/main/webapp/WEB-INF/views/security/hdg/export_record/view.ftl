<div class="tab_item" style="display:block;padding-left:50px;padding-top: 30px;font-size: 14px;min-height: 77%;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left">运营商名称：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.batteryId)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">外壳编号：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.shellCode)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">IMEI：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.code)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">柜子编号：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.cabinetId)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">柜子名称：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.cabinetName)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">发货时间：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="height: 10%">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>