<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">门状态：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(doorState)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">烟雾传感器：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(smokeAlarm)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">插座电源：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(socketPower)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">格口风扇：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(boxFan)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">格口通讯：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(boxCommu)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">充电控制：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(chargeControl)!''}"></td>
                </tr>
                <tr>
                    <td width="70" align="right">NFC识别状态：</td>
                    <td><input type="text" class="text easyui-validatebox" value="${(boxNFC)!''}"></td>
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
