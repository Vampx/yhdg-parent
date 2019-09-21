<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly 
                               value="${(entity.customerFullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.customerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="right" width="100">转让客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.transferCustomerFullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">转让客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.transferCustomerMobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">押金订单编号：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.foregiftOrderId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">转让押金(元)：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.foregiftMoney/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">租金订单编号：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.packetPeriodOrderId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">换电订单编号：</td>
                    <td><input type="text" class="text easyui-validatebox"  readonly
                               value="${(entity.batteryOrderId)!''}"/></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>