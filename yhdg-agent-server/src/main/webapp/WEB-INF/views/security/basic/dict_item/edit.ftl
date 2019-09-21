<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">字典分类：</td>
                    <td>
                        <input class="text easyui-combobox" id="category_id" required="true" readonly="true" name="categoryId" style="width:180px; height: 28px;"
                               data-options="
                               valueField:'id',
                               textField:'text',
                               editable: false,
                               url:'${contextPath}/security/basic/dict_item/find_category.htm',
                               onLoadSuccess:function() {
                                $('#category_id').combobox('setValue','${(entity.categoryId)!''}');
                               }" value="${(entity.categoryId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">字典名称：</td>
                    <td>
                        <input type="text" maxlength="41" class="text easyui-validatebox" required="true" name="itemName" value="${(entity.itemName)!''}" maxlength="40"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" id="itemValue">(值类型：${(valueType)!''})值：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="41" required="true" name="itemValue" value="${(entity.itemValue)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">排序：</td>
                    <td><input type="text" maxlength="6" class="easyui-numberspinner" style="width:180px; height: 28px;" required="true" name="orderNum"  data-options="min:0,max:100" value="${(entity.orderNum)!''}"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/dict_item/update.htm',
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

    })();

    $('#category_id').combobox({disabled: true});


</script>