<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">排序号：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="orderNum" id="order_num_${pid}" maxlength="3"
                               data-options="min:0, max:1000" style="width: 184px; height: 28px;"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button id="extend_btn" class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            var orderNum = $('#order_num_${pid}').numberspinner('getValue');
            if(orderNum == null || orderNum == '') {
                $.messager.alert('提示信息', '请输入排序号', 'info');
                return;
            }
            if(orderNum == 0) {
                $.messager.alert('提示信息', '排序号不能为0', 'info');
                return;
            }
            $("#extend_btn").attr("disabled",true);
            var url ='${contextPath}/security/yms/playlist_detail/order_num.htm';
            form.form('submit', {
                url: url,
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                        win.window('close');
                    }
                }
            });
        });

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
