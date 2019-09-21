<div class="tab_item" style="display:block;padding-left: 30px;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left">期数：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly value="${(entity.num)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.fullname)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">手机号码：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td align="left" width="170px;">本次支付押金金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${((entity.foregiftMoney)!0)/100}"/></td>
                </tr>
                <tr>
                    <td align="left">本次支付租金金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${((entity.packetMoney)!0)/100}"/></td>
                </tr>
                <tr>
                    <td align="left">本次支付保险金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${((entity.insuranceMoney)!0)/100}"/></td>
                </tr>
                <tr>
                    <td align="left">本次支付总金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${((entity.money)!0)/100}"/></td>
                </tr>
                <tr>
                    <td align="left">分期总金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${((entity.totalMoney)!0)/100}"/></td>
                </tr>
                <tr>
                    <td align="left">支付状态：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.statusName)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">支付时间：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="<#if (entity.payTime)?? >${app.format_date_time(entity.payTime)}</#if>"/></td>
                </tr>
                <tr>
                    <td align="left">过期时间：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="<#if (entity.expireTime)?? >${app.format_date_time(entity.expireTime)}</#if>"/></td>
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