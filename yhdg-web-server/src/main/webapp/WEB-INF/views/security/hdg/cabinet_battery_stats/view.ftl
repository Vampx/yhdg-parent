<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <input type="hidden" name="agentId" value="${entity.agentId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">运营商：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.agentName)!''}"/>
                    </td>
                    <td width="80" align="right">换电柜名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.cabinetName)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">电池数：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly
                    value="${(entity.batteryNum)!''}"/></td>
                    <td align="right" width="80">换电柜编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly
                               value="${entity.cabinetId}"/></td>

                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;width: 90px;">箱门电池信息：</td>
                    <td colspan="4"><textarea style="width:433px; height: 110px;" name="content"
                                              readonly>${entity.boxBatteryMessage}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;width: 80px;">箱门信息：</td>
                    <td colspan="4"><textarea style="width:433px; height: 110px;" name="content"
                                              readonly>${entity.boxMessage}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function () {
            win.window('close');
        });

    })()
</script>
