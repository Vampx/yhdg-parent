<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">订单编号：</td>
                    <td><input class="text easyui-validatebox" name="id" value="${(entity.id)!''}"
                               style="width:182px;height:28px " required="required"></td>
                </tr>
                <tr>
                    <td align="right">退款金额：</td>
                    <td><input type="text" class="text" style="width:182px;height:28px " name="refundMoney"
                               value="<#if (entity.refundMoney)??>${((entity.refundMoney) / 100 + "元")!''}<#else></#if>"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">退款类型：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="refundType"
                               value="${(entity.refundTypeName)!''}" style="width:182px;height:28px "></td>
                </tr>
                <tr>
                    <td align="right">退款人：</td>
                    <td><input id="duration" class="text easyui-validatebox" name="refundOperator"
                               value="${(entity.refundOperator)!''}" style="width:182px;height:28px "></td>
                </tr>
                <tr>
                    <td align="right">退款时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" style="width:195px;height:28px "
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"
                               name="createTime">
                    </td>
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






