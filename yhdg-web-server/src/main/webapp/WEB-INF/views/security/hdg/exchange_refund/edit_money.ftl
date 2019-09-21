<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="refundable_money_${pid}" value="${(refundableMoney/100)!0}">
            <table cellpadding="0" cellspacing="0">
                </tr>
                <td align="right">金额：</td>
                <td>
                    <input type="text"  class="easyui-numberbox" data-options="min:0,precision:2" id="real_refund_money_${pid}" value="${(realRefundMoney/100)!0}" style="width:130px;height: 28px;" maxlength="10">
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


    function select_${pid}() {
        var customer = datagrid.datagrid('getSelected');
        if(customer) {
            windowData.ok({
                customer: customer
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择用户');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                 windowData = win.data('windowData');

        win.find('button.ok').click(function () {
            var realRefundMoney = Number($('#real_refund_money_${pid}').val() * 100);
            var refundableMoney = Number($('#refundable_money_${pid}').val() * 100);
            if(realRefundMoney > refundableMoney) {
                $.messager.alert('提示信息', '退款金额不能大于可退金额', 'info');
                return;
            }
            // if(realRefundMoney==0) {
            //     $.messager.alert('提示信息', '退款金额不能为0', 'info');
            //     return;
            // }
            windowData.ok(realRefundMoney/100);
            win.window('close');
        })

        win.find('button.close').click(function () {
            win.window('close');
        });
    })()

</script>






